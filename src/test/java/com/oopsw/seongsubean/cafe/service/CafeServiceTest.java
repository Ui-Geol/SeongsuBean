package com.oopsw.seongsubean.cafe.service;

import com.oopsw.seongsubean.cafe.domain.OperationTime;
import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@Transactional
public class CafeServiceTest {

  @Autowired
  private CafeService cafeService;

  static CafeDTO cafeDTO;
  static List<OperationTime> operationTimes;

  @BeforeAll
  static void beforeAll() {
    cafeDTO = CafeDTO.builder()
        .cafeName("아루수 카페")
        .address("서울시 성수동 1동")
        .detailAddress("2층")
        .zipCode("24313")
        .callNumber("010-0000-0000")
        .introduction("여기는 건물 지하에서 올라오는 지하수를 바탕으로 맛있는 물로 커피를 우려냅니다")
        .mainImage("/images/cafe/Cafe12.png")
        .email("taebin@gmail.com").build();

    operationTimes = new ArrayList<>();

    operationTimes.add(
        OperationTime.builder().cafeId(3).weekday("월요일").openTime(LocalTime.of(8, 22))
            .closeTime(LocalTime.of(3, 44)).build());
    operationTimes.add(
        OperationTime.builder().cafeId(3).weekday("화요일").openTime(LocalTime.of(8, 22))
            .closeTime(LocalTime.of(4, 33)).build());
    operationTimes.add(
        OperationTime.builder().cafeId(3).weekday("수요일").openTime(LocalTime.of(8, 22))
            .closeTime(LocalTime.of(4, 5)).build());
    operationTimes.add(
        OperationTime.builder().cafeId(3).weekday("목요일").openTime(LocalTime.of(8, 22))
            .closeTime(LocalTime.of(5, 6)).build());
  }

//  @Test
//  public void failAddCafeTest1() {
//    cafeDTO.setCafeName(null);
//
//    assertThatThrownBy(() -> cafeService.addCafe(cafeDTO, operationTimes))
//        .isInstanceOf(RuntimeException.class);
//  }
//
//  @Test
//  public void failAddCafeTest2() {
//    assertThatThrownBy(() -> cafeService.addCafe(cafeDTO, operationTimes))
//        .isInstanceOf(RuntimeException.class);
//  }
//
//  @Test
//  public void successAddCafeTest() {
//    assertThat(cafeService.addCafe(cafeDTO, operationTimes));
//  }
//
//  @Test
//  public void successGetCafeHeaderTest2() {
//    CafeHeaderDTO cafeHeaderDTO = cafeService.getCafeHeader(3);
//
//    assertThat(cafeHeaderDTO).isNotNull();
//  }
//
//  @Test
//  public void successGetCafeDTOTest() {
//    CafeDTO cafeDTO = cafeService.getCafeDTO(3);
//  }
//
//  @Test
//  public void successGetOperationTimesTest() {
//    List<OperationTime> operationTimes = cafeService.getOperationTimes(3);
//    assertThat(operationTimes.size()).isEqualTo(6);
//  }
//
//  @Test
//  public void successSetCafe() {
//    CafeDTO cafeDTO = cafeService.getCafeDTO(3);
//    List<OperationTime> operationTimes = cafeService.getOperationTimes(cafeDTO.getCafeId());
//    operationTimes.add(
//        OperationTime.builder().cafeId(3).weekday("월요일").openTime(LocalTime.of(8, 22))
//            .closeTime(LocalTime.of(3, 44)).build());
//    operationTimes.add(
//        OperationTime.builder().cafeId(3).weekday("화요일").openTime(LocalTime.of(8, 22))
//            .closeTime(LocalTime.of(4, 33)).build());
//    operationTimes.add(
//        OperationTime.builder().cafeId(3).weekday("수요일").openTime(LocalTime.of(8, 22))
//            .closeTime(LocalTime.of(4, 5)).build());
//    operationTimes.add(
//        OperationTime.builder().cafeId(3).weekday("목요일").openTime(LocalTime.of(8, 22))
//            .closeTime(LocalTime.of(5, 6)).build());
//
//    cafeDTO.setCafeName("할로카페");
//
//    cafeService.setCafe(cafeDTO, operationTimes);
//
//    CafeDTO updatedCafeDTO = cafeService.getCafeDTO(3);
//    List<OperationTime> updatedOperationtimes = cafeService.getOperationTimes(3);
//
//    assertThat(updatedCafeDTO.getCafeName()).isEqualTo("할로카페");
//    assertThat(updatedOperationtimes.size()).isEqualTo(3);
//
//  }
//
//  @Test
//  public void successRemoveCafeTest() {
//    assertThat(cafeService.removeCafe(3));
//
//    assertThat(cafeService.getCafeDTO(3)).isNull();
//  }
//
//  @Test
//  public void successSetStatusTest() {
//    cafeService.setCafeStatus(3, "휴무일");
//
//    CafeDTO updatedCafeDTO = cafeService.getCafeDTO(3);
//
//    assertThat(updatedCafeDTO.getStatus()).isEqualTo("휴무일");
//
//  }

}
