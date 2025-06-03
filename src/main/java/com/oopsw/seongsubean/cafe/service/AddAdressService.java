package com.oopsw.seongsubean.cafe.service;

import com.oopsw.seongsubean.cafe.dto.ResponseAdressDTO;
import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import com.oopsw.seongsubean.cafe.repository.mybatisrepository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AddAdressService {
  private final CafeRepository cafeRepository;



  public void saveAddress(ResponseAdressDTO request) {
    // CafeDTO에 주소 정보 설정해서 저장
    CafeDTO cafeDTO = CafeDTO.builder()
        .address(request.getAddress())
        .detailAddress(request.getDetailAddress())
        .zipCode(request.getZipCode())
        .status("TEMP") // 임시 상태
        .pageCreatedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        .build();

    // 데이터베이스에 저장
    cafeRepository.addCafe(cafeDTO);
  }
}
