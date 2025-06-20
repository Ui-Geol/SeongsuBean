package com.oopsw.seongsubean.cafe.service;

import com.oopsw.seongsubean.cafe.domain.OperationTime;
import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import com.oopsw.seongsubean.cafe.dto.CafeHeaderDTO;
import com.oopsw.seongsubean.cafe.dto.OperationTimeDTO;
import com.oopsw.seongsubean.cafe.repository.jparepository.OperationTimeRepository;
import com.oopsw.seongsubean.cafe.repository.mybatisrepository.CafeRepository;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CafeService {

  private final CafeRepository cafeRepository;
  private final OperationTimeRepository operationTimeRepository;

  @Transactional
  public Integer addCafe(CafeDTO cafeDTO) {
    try {
      boolean cafeResult = cafeRepository.addCafe(cafeDTO);
      if (!cafeResult) {
        throw new RuntimeException("카페 정보 저장 실패");
      }

      Integer cafeId = cafeRepository.getCafeIdByCafeNameAndAddressAndDetailAddress(cafeDTO);
      if (cafeId == null) {
        throw new RuntimeException("카페 ID 조회 실패");
      }

      List<OperationTimeDTO> operationTimeDTOs = cafeDTO.getOperationTimes();
      if (operationTimeDTOs == null || operationTimeDTOs.isEmpty()) {
        throw new RuntimeException("영업시간 정보가 없습니다");
      }

      List<OperationTime> operationTimes = new ArrayList<>();
      for (OperationTimeDTO operationTimeDTO : operationTimeDTOs) {
        try {
          if (operationTimeDTO == null) {
            continue;
          }
          OperationTime operationTime = OperationTime.builder()
              .weekday(operationTimeDTO.getWeekday())
              .openTime(LocalTime.parse(operationTimeDTO.getOpenTime()))
              .closeTime(LocalTime.parse(operationTimeDTO.getCloseTime()))
              .build();
          operationTime.setCafeId(cafeId);
          operationTimes.add(operationTime);
        } catch (Exception e) {
          throw new RuntimeException("영업시간 파싱 오류: " + e.getMessage(), e);
        }
      }

      if (!operationTimes.isEmpty()) {
        operationTimeRepository.saveAll(operationTimes);
      }

      System.out.println(cafeId);

      return cafeId;

    } catch (Exception e) {
      // 로그 남기기
      System.err.println("카페 저장 중 오류: " + e.getMessage());
      throw new RuntimeException("카페 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
    }
  }

  //상세 페이지 공통
  public CafeHeaderDTO getCafeHeader(Integer cafeId) {
    return cafeRepository.getCafeHeaderByCafeId(cafeId);

  }

  //상세 페이지 개요 카페 정보 조회
  public CafeDTO getCafeDTO(Integer cafeId) {
    return cafeRepository.getAllByCafeId(cafeId);
  }

  //상세 페이지 개요 카페 운영시간 조회
  public List<OperationTimeDTO> getOperationTimes(Integer cafeId) {
    List<OperationTime> operationTimes = operationTimeRepository.findAllByCafeId(cafeId);

    List<OperationTimeDTO> operationTimeDTOList = new ArrayList<>();

    for (OperationTime operationTime : operationTimes) {
      OperationTimeDTO operationTimeDTO = OperationTimeDTO.builder()
          .operationTimeId(operationTime.getOperationTimeId())
          .cafeId(operationTime.getCafeId())
          .openTime(String.valueOf(operationTime.getOpenTime()))
          .closeTime(String.valueOf(operationTime.getCloseTime()))
          .weekday(operationTime.getWeekday()).build();
      operationTimeDTOList.add(operationTimeDTO);
    }

    // 요일 순서 정의
    Map<String, Integer> dayOrder = Map.of(
        "월요일", 1, "화요일", 2, "수요일", 3, "목요일", 4,
        "금요일", 5, "토요일", 6, "일요일", 7
    );

    // 요일 순서로 정렬
    operationTimeDTOList.sort((a, b) -> {
      int orderA = dayOrder.getOrDefault(a.getWeekday(), 8);
      int orderB = dayOrder.getOrDefault(b.getWeekday(), 8);
      return Integer.compare(orderA, orderB);
    });

    return operationTimeDTOList;
  }


  //카페 수정
  @Transactional
  public boolean setCafe(CafeDTO updatedCafeDTO) {
    boolean result = cafeRepository.setCafe(updatedCafeDTO);
    if (!result) {
      return false;
    }

    List<OperationTime> operationTimes = updatedCafeDTO.convertToOperationTimeList(
        updatedCafeDTO.getOperationTimes(), updatedCafeDTO.getCafeId());

    operationTimeRepository.deleteAllByCafeId(updatedCafeDTO.getCafeId());
    operationTimeRepository.flush();

    if (operationTimes != null && !operationTimes.isEmpty()) {
      operationTimes.forEach(operationTime -> {
        if (operationTime != null) {
          operationTime.setCafeId(updatedCafeDTO.getCafeId());
        }
      });
      System.out.println(operationTimes);
      operationTimeRepository.saveAll(operationTimes);
    }

    return result;
  }

  //카페 삭제
  public boolean removeCafe(Integer cafeId) {
    return cafeRepository.removeCafe(cafeId);
  }

  //휴무일 설정
  public boolean setCafeStatus(Integer cafeId, String status) {
    return cafeRepository.setCafeStatus(cafeId, status);
  }

}
