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

  @Test
  public void getCafeSummaryViewByCafeIdTest() {
    assertThat(cafeRepository.getCafeHeaderByCafeId(3).getCafeName()).isEqualTo("오우드");
  }

  @Test
  public void getAllByCafeIdTest() {
    log.info(cafeRepository.getAllByCafeId(3).toString());
    assertThat(cafeRepository.getAllByCafeId(3)).isNotNull();
  }

  @Test
  public void setCafeTest() {
    CafeDTO cafe = cafeRepository.getAllByCafeId(1);
    cafe.setCafeName("테스트");
    assertThat(cafeRepository.setCafe(cafe)).isTrue();
  }

  @Test
  public void removeCafeTest() {
    assertThat(cafeRepository.removeCafe(1)).isTrue();
  }

  @Test
  public void setCafeStatusTest() {
    assertThat(cafeRepository.setCafeStatus(3, "휴무일")).isTrue();

  }

}
