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

  //카페 생성
  @Transactional
  public Integer addCafe(CafeDTO cafeDTO) {
    try {
      boolean cafeResult = cafeRepository.addCafe(cafeDTO);
      if (!cafeResult) {
        return null;
      }

      Integer cafeId = cafeRepository.getCafeIdByCafeNameAndAddress(cafeDTO);

      List<OperationTime> operationTimes = new ArrayList<>();

      for (OperationTimeDTO operationTimeDTO : cafeDTO.getOperationTimes()) {
        OperationTime operationTime = OperationTime.builder()
            .weekday(operationTimeDTO.getWeekday())
            .openTime(LocalTime.parse(operationTimeDTO.getOpenTime()))
            .closeTime(LocalTime.parse(operationTimeDTO.getCloseTime())).build();

        operationTimes.add(operationTime);

      }

      if (operationTimes != null && !operationTimes.isEmpty()) {
        operationTimes.forEach(operationTime -> {
          if (operationTime != null) {
            operationTime.setCafeId(cafeId);
          }
        });
        operationTimeRepository.saveAll(operationTimes);
      }

      return cafeId;

    } catch (Exception e) {
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
  public List<OperationTime> getOperationTimes(Integer cafeId) {
    List<OperationTime> operationTimes = operationTimeRepository.findAllByCafeId(cafeId);

    // 요일 순서 정의
    Map<String, Integer> dayOrder = Map.of(
        "월요일", 1, "화요일", 2, "수요일", 3, "목요일", 4,
        "금요일", 5, "토요일", 6, "일요일", 7
    );

    // 요일 순서로 정렬
    operationTimes.sort((a, b) -> {
      int orderA = dayOrder.getOrDefault(a.getWeekday(), 8);
      int orderB = dayOrder.getOrDefault(b.getWeekday(), 8);
      return Integer.compare(orderA, orderB);
    });

    return operationTimes;
  }


  //카페 수정
  @Transactional
  public boolean setCafe(CafeDTO updatedCafeDTO, List<OperationTime> operationTimes) {
    boolean result = cafeRepository.setCafe(updatedCafeDTO);
    if (!result) {
      return false;
    }

    operationTimeRepository.deleteAllByCafeId(updatedCafeDTO.getCafeId());

    if (operationTimes != null && !operationTimes.isEmpty()) {
      operationTimes.forEach(operationTime -> {
        if (operationTime != null) {
          operationTime.setCafeId(updatedCafeDTO.getCafeId());
        }
      });
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
