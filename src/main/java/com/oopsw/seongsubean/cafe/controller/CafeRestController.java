package com.oopsw.seongsubean.cafe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import com.oopsw.seongsubean.cafe.dto.OperationTimeDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/cafe")
public class CafeRestController {

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
          .build();

      if (image != null && !image.isEmpty()) {
        // 이미지 저장 로직 구현
        System.out.println("업로드된 이미지: " + image.getOriginalFilename());
        System.out.println("이미지 크기: " + image.getSize() + " bytes");
        System.out.println("이미지 타입: " + image.getContentType());
      }

      // 서버 콘솔에 로깅
      System.out.println("등록할 카페 정보: " + cafeDTO);
      System.out.println("영업시간 JSON: " + businessHoursJson);

      // 성공 응답
      Map<String, Object> response = new HashMap<>();
      response.put("success", true);
      response.put("id", 1); // 실제로는 생성된 카페 ID
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
}