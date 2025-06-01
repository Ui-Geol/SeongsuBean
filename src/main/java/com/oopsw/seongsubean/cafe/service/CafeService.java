package com.oopsw.seongsubean.cafe.service;

import com.oopsw.seongsubean.cafe.domain.OperationTime;
import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import com.oopsw.seongsubean.cafe.dto.CafeHeaderDTO;
import com.oopsw.seongsubean.cafe.repository.jparepository.OperationTimeRepository;
import com.oopsw.seongsubean.cafe.repository.mybatisrepository.CafeRepository;
import java.util.List;
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
  public boolean addCafe(CafeDTO cafeDTO, List<OperationTime> operationTimes) {
    boolean result = cafeRepository.addCafe(cafeDTO);
    Integer cafeId = cafeRepository.getCafeIdByCafeNameAndAddress(cafeDTO);
    operationTimeRepository.saveAll(operationTimes);

    return result;

  }
  //상세 페이지 공통
  public CafeHeaderDTO getCafeHeader(Integer cafeId) {
    return cafeRepository.getCafeHeaderByCafeId(cafeId);

  }
  //상세 페이지 개요 조회
  public CafeDTO getCafeDTO(Integer cafeId) {
    return cafeRepository.getAllByCafeId(cafeId);
  }
  //카페 수정
  public boolean setCafe(CafeDTO cafeDTO) {
    return cafeRepository.setCafe(cafeDTO);
  }
  //카페 삭제
  public boolean removeCafe(Integer cafeId) {
    return cafeRepository.removeCafe(cafeId);
  }

}
