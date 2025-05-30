package com.oopsw.seongsubean.cafe.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import com.oopsw.seongsubean.cafe.repository.mybatisrepository.CafeRepository;
import com.oopsw.seongsubean.config.PropertyConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@MybatisTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PropertyConfig.class)
@Transactional
public class CafeRespositoryTest {

  @Autowired
  CafeRepository cafeRepository;

  @Test
  public void getCafeNamebyCafeIdTest() {
    System.out.println(cafeRepository.getCafeNameByCafeId(3));
  }

  @Test
  public void addCafeTest() {
    CafeDTO cafe = CafeDTO.builder()
        .cafeName("아루수 카페")
        .address("서울시 성수동 1동")
        .detailAddress("2층")
        .zipCode("24313")
        .callNumber("010-0000-0000")
        .introduction("여기는 건물 지하에서 올라오는 지하수를 바탕으로 맛있는 물로 커피를 우려냅니다")
        .mainImage("/images/cafe/Cafe12.png")
        .email("taebin@gmail.com").build();
    assertThat(cafeRepository.addCafe(cafe)).isTrue();
  }

  @Test
  public void getCafeIdByCafeNameAndAddressTest() {
    CafeDTO cafe = CafeDTO.builder().cafeName("대림창고").address("서울특별시 성동구 성수이로 78").build();

    assertThat(cafeRepository.getCafeIdByCafeNameAndAddress(cafe)).isEqualTo(1);
  }

  //  4) 상세페이지 공통
//# 1. 카페 이름, 평균 별점, 리뷰 개수, 메인 이미지 -> 뷰로 구현
  @Test
  public void getCafeSummaryViewByCafeIdTest() {
    assertThat(cafeRepository.getCafeHeaderByCafeId(3).getCafeName()).isEqualTo("오우드");
  }

  //  5) 상세페이지 개요 조회
//# 1. 아이디 값으로 (주소, 상세주소, 상태, 전화번호, 소개)
  @Test
  public void getAllByCafeIdTest() {
    log.info(cafeRepository.getAllByCafeId(3).toString());
    assertThat(cafeRepository.getAllByCafeId(3)).isNotNull();
  }

  //# 2. 카페 업데이트
//  UPDATE CAFE_INFO SET CAFE_NAME = '테스트 카페', ADDRESS = '서울시 성수로 3번지', DETAIL_ADDRESS='3층', ZIP_CODE='34323', CALL_NUMBER='010-2432-4523', INTRODUCTION='여기 맛도리입니다', MAIN_IMAGE='/images/cafe/Cafe4.png'
//  WHERE CAFE_ID = 3;
  @Test
  public void setCafeTest() {
    CafeDTO cafe = cafeRepository.getAllByCafeId(1);
    cafe.setCafeName("테스트");
    assertThat(cafeRepository.setCafe(cafe)).isTrue();
  }

  //13) 카페 삭제
//  DELETE FROM CAFE_INFO WHERE CAFE_ID = 52;
  @Test
  public void removeCafeTest() {
    assertThat(cafeRepository.removeCafe(1)).isTrue();
  }

}
