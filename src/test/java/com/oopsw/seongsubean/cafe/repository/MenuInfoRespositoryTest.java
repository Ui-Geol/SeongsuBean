package com.oopsw.seongsubean.cafe.repository;

import com.oopsw.seongsubean.config.PropertyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PropertyConfig.class)
public class MenuInfoRespositoryTest {

  @Autowired
  private MenuInfoRepository menuInfoRepository;

//  # 1. 카페 테이블에 데이터 추가(카페 이름, 카페 주소, 카페 상세 주소, 카페 우편번호, 카페 전화번호, 카페 소개, 카폐 대표이미지)

//  # 2. 카페 이름과 주소를 통해 ID값을 가져오기

//  # 3. 카페 ID 값을 사용하여 영업 시간에 값 삽입

//  6) 상세페이지 메뉴 조회

//  9) 메뉴 수정

//  # 2. 메뉴 업데이트

//  12) 메뉴 삭제


}
