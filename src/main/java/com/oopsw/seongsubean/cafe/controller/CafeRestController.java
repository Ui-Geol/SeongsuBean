package com.oopsw.seongsubean.cafe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import com.oopsw.seongsubean.cafe.dto.MenuDTO;
import com.oopsw.seongsubean.cafe.dto.OperationTimeDTO;
import com.oopsw.seongsubean.cafe.service.CafeService;
import com.oopsw.seongsubean.cafe.service.MenuService;
import com.oopsw.seongsubean.cafe.service.ReviewService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafe")
public class CafeRestController {

  private final CafeService cafeService;
  private final ReviewService reviewService;
  private final MenuService menuService;

  /**
   * 클라이언트에서 JSON으로 보낸 CafeDTO 전체를 받아서, 그대로 ResponseBody로 다시 내려주는 테스트용 엔드포인트
   */
  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * 등록: FormData로 받기 (이미지 파일 포함)
   */
  @PostMapping
  public ResponseEntity<Map<String, Object>> registerCafe(
      @RequestParam("cafeName") String cafeName,
      @RequestParam("address") String address,
      @RequestParam("zipCode") String zipCode,
      @RequestParam("detailAddress") String detailAddress,
      @RequestParam("phone") String callNumber,
      @RequestParam("description") String introduction,
      @RequestParam("businessHours") String businessHoursJson,
      @RequestParam(value = "image", required = false) MultipartFile image) {

    try {
      // 영업시간 JSON 파싱
      List<OperationTimeDTO> operationTimes = null;
      if (businessHoursJson != null && !businessHoursJson.trim().isEmpty()) {
        operationTimes = objectMapper.readValue(businessHoursJson,
            objectMapper.getTypeFactory()
                .constructCollectionType(List.class, OperationTimeDTO.class));
      }

      // CafeDTO 생성
      CafeDTO cafeDTO = CafeDTO.builder()
          .cafeName(cafeName)
          .address(address)
          .zipCode(zipCode)
          .detailAddress(detailAddress)
          .callNumber(callNumber)
          .introduction(introduction)
          .status("영업중")
          .operationTimes(operationTimes)
          .email("anyujin0901@gmail.com")
          .build();

      if (image == null) {
        System.out.println("동작 안됨");
      }

      if (image != null && !image.isEmpty()) {
        System.out.println("동작됨");

        try {
          String originalFilename = image.getOriginalFilename();

          // 파일 안전성 검증
          if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("파일명이 유효하지 않습니다.");
          }

          // 프로젝트 루트 경로 기준으로 절대 경로 생성
          String projectRoot = System.getProperty("user.dir");
          String uploadDir =
              projectRoot + "/src/main/resources/static/images/cafe/";

          System.out.println("업로드 디렉토리: " + uploadDir);

          // 디렉토리 생성
          Path uploadPath = Paths.get(uploadDir);
          if (!Files.exists(uploadPath)) {
            System.out.println("디렉토리 생성: " + uploadPath);
            Files.createDirectories(uploadPath);
          }

          // 파일 확장자 추출 및 안전한 파일명 생성
          String extension = "";
          if (originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
          }

          // UUID를 사용한 고유 파일명 생성 (한글 파일명 문제 해결)
          String safeFileName = UUID.randomUUID().toString() + extension;

          // 최종 파일 경로
          Path filePath = uploadPath.resolve(safeFileName);

          System.out.println("저장될 파일 경로: " + filePath);

          // 파일 저장
          Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

          // DTO에 저장된 파일명 설정
          cafeDTO.setMainImage("/images/cafe/" + safeFileName);

          System.out.println("파일 저장 완료: " + safeFileName);

        } catch (IOException e) {
          System.err.println("파일 저장 중 오류 발생: " + e.getMessage());
          e.printStackTrace();
          throw new RuntimeException("이미지 저장에 실패했습니다.", e);
        } catch (Exception e) {
          System.err.println("예상치 못한 오류: " + e.getMessage());
          e.printStackTrace();
          throw new RuntimeException("이미지 처리 중 오류가 발생했습니다.", e);
        }
      }

      Integer result = cafeService.addCafe(cafeDTO);
      // 서버 콘솔에 로깅
      System.out.println("등록할 카페 정보: " + cafeDTO);
      System.out.println("영업시간 JSON: " + businessHoursJson);

      // 성공 응답
      Map<String, Object> response = new HashMap<>();
      response.put("success", true);
      response.put("id", result); // 실제로는 생성된 카페 ID
      response.put("message", "카페가 성공적으로 등록되었습니다.");

      return ResponseEntity.ok(response);

    } catch (Exception e) {
      System.err.println("카페 등록 중 오류 발생: " + e.getMessage());

      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("success", false);
      errorResponse.put("message", "카페 등록에 실패했습니다.");

      return ResponseEntity.badRequest().body(errorResponse);
    }
  }

  @PostMapping("/menu")
  public ResponseEntity<Map<String, Object>> registerMenu(
      @RequestParam("menuCategory") String menuCategory,
      @RequestParam("menuName") String menuName,
      @RequestParam("menuDescription") String menuDescription,
      @RequestParam("price") String price,
      @RequestParam("cafeId") String cafeId,
      @RequestParam(value = "image", required = false) MultipartFile image) {

    System.out.println(menuCategory);
    System.out.println(menuName);
    System.out.println(menuDescription);
    System.out.println(price);
    System.out.println(cafeId);
    System.out.println("파일명: " + image.getOriginalFilename());
    System.out.println("파일 크기: " + image.getSize() + " bytes");
    System.out.println("파일 타입: " + image.getContentType());
    System.out.println("비어있는가: " + image.isEmpty());
    MenuDTO menuDTO = MenuDTO.builder().menuCategory(menuCategory).menuName(menuName)
        .price(Integer.parseInt(price))
        .description(menuDescription).cafeId(Integer.parseInt(cafeId)).build();

    if (image != null && !image.isEmpty()) {
      System.out.println("동작됨");

      try {
        String originalFilename = image.getOriginalFilename();

        // 파일 안전성 검증
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
          throw new IllegalArgumentException("파일명이 유효하지 않습니다.");
        }

        // 프로젝트 루트 경로 기준으로 절대 경로 생성
        String projectRoot = System.getProperty("user.dir");
        String uploadDir =
            projectRoot + "/src/main/resources/static/images/menu/";

        System.out.println("업로드 디렉토리: " + uploadDir);

        // 디렉토리 생성
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
          System.out.println("디렉토리 생성: " + uploadPath);
          Files.createDirectories(uploadPath);
        }

        // 파일 확장자 추출 및 안전한 파일명 생성
        String extension = "";
        if (originalFilename.contains(".")) {
          extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // UUID를 사용한 고유 파일명 생성 (한글 파일명 문제 해결)
        String safeFileName = UUID.randomUUID().toString() + extension;

        // 최종 파일 경로
        Path filePath = uploadPath.resolve(safeFileName);

        System.out.println("저장될 파일 경로: " + filePath);

        // 파일 저장
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // DTO에 저장된 파일명 설정
        menuDTO.setImage("/images/menu/" + safeFileName);

        System.out.println("파일 저장 완료: " + safeFileName);

      } catch (IOException e) {
        System.err.println("파일 저장 중 오류 발생: " + e.getMessage());
        e.printStackTrace();
        throw new RuntimeException("이미지 저장에 실패했습니다.", e);
      } catch (Exception e) {
        System.err.println("예상치 못한 오류: " + e.getMessage());
        e.printStackTrace();
        throw new RuntimeException("이미지 처리 중 오류가 발생했습니다.", e);
      }
    }

    boolean result = menuService.addMenu(menuDTO);

    Map<String, Object> response = new HashMap<>();
    response.put("success", result);
    response.put("message", result ? "메뉴가 성공적으로 등록되었습니다." : "메뉴 등록에 실패했습니다.");

    if (result) {
      response.put("id", cafeId);
      return ResponseEntity.ok(response);
    } else {
      return ResponseEntity.badRequest().body(response);
    }


  }
}