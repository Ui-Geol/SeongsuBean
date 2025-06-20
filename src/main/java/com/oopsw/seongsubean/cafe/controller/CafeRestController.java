package com.oopsw.seongsubean.cafe.controller;

import com.oopsw.seongsubean.account.dto.UserDTO;
import com.oopsw.seongsubean.auth.AccountDetails;
import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import com.oopsw.seongsubean.cafe.dto.CafeHeaderDTO;
import com.oopsw.seongsubean.cafe.dto.OperationTimeDTO;
import com.oopsw.seongsubean.cafe.service.CafeService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafe")
public class CafeRestController {

  private final CafeService cafeService;

  @PostMapping
  public ResponseEntity<?> addCafe(
      Authentication auth,
      @Valid @RequestBody CafeDTO cafeDto) {

    UserDTO userDTO = ((AccountDetails) auth.getPrincipal()).getUser();

    if (userDTO == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다");
    }

    cafeDto.setEmail(userDTO.getEmail());
    cafeDto.setMainImage("/images/cafe/" + cafeDto.getMainImage());

    System.out.println(cafeDto);
    try {
      Integer cafeId = cafeService.addCafe(cafeDto);
      if (cafeId == null) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카페 생성이 실패하였습니다");
      }
      return ResponseEntity.status(HttpStatus.CREATED).body(cafeId);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카페 생성이 실패하였습니다");
    }

  }

  @GetMapping("/{cafeId}")
  public ResponseEntity<Map<String, Object>> getCafeById(@PathVariable("cafeId") Integer cafeId) {
    //카페 헤더
    CafeHeaderDTO cafeHeaderDTO = cafeService.getCafeHeader(cafeId);
    //카페 개요
    CafeDTO cafeDto = cafeService.getCafeDTO(cafeId);

    //카페 영업 시간
    List<OperationTimeDTO> operationTimeDTOList = cafeService.getOperationTimes(cafeId);

    return ResponseEntity.ok(
        Map.of("cafeHeaderDTO", cafeHeaderDTO, "cafe", cafeDto, "operationTimes",
            operationTimeDTOList));
  }

  @GetMapping("/{cafeId}/cafeHeader")
  public ResponseEntity<Map<String, Object>> getCafeHeader(@PathVariable("cafeId") Integer cafeId) {
    CafeHeaderDTO cafeHeaderDTO = cafeService.getCafeHeader(cafeId);

    return ResponseEntity.status(HttpStatus.OK).body(Map.of("cafeHeaderDTO", cafeHeaderDTO));
  }

  @GetMapping("/{cafeId}/cafeDTO")
  public ResponseEntity<Map<String, Object>> getCafeDTO(@PathVariable("cafeId") Integer cafeId) {
    CafeDTO cafeDTO = cafeService.getCafeDTO(cafeId);
    List<OperationTimeDTO> operationTimes = cafeService.getOperationTimes(cafeId);
    cafeDTO.setOperationTimes(operationTimes);
    System.out.println(operationTimes);

    return ResponseEntity.status(HttpStatus.OK).body(Map.of("cafeDTO", cafeDTO));
  }


  @PutMapping("/{cafeId}")
  public ResponseEntity<String> setCafe(@Valid @RequestBody CafeDTO cafeDto) {

    try {
      cafeService.setCafe(cafeDto);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카페 업데이트를 실패하였습니다");
    }
  }

  @DeleteMapping("/{cafeId}")
  public ResponseEntity<String> removeCafe(@PathVariable Integer cafeId) {
    try {
      cafeService.removeCafe(cafeId);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카페 삭제를 실패하였습니다");
    }
  }

}