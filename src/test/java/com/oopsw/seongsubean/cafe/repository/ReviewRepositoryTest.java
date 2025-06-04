package com.oopsw.seongsubean.cafe.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.oopsw.seongsubean.cafe.dto.ReviewDTO;
import com.oopsw.seongsubean.cafe.repository.mybatisrepository.ReviewRepository;
import com.oopsw.seongsubean.config.PropertyConfig;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@MybatisTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PropertyConfig.class)
@Transactional
public class ReviewRepositoryTest {

  @Autowired
  private ReviewRepository reviewRepository;

  //  # 3. 카페 리뷰 데이터 추가(내용, 날짜, 별점, 카페 ID, 이메일)
//  INSERT INTO REVIEW (REVIEW_CONTENTS, STAR_SCORE, CAFE_ID, EMAIL, REVIEW_DATE) VALUES ('친구랑 가기 좋은 카페예요.',  5, 24, 'ANYUJIN0901@GMAIL.COM', NOW());
  @Test
  public void addReviewTest() {
    ReviewDTO reviewDTO = ReviewDTO.builder()
        .reviewContents("쌉꿀마 맛꿀마")
        .starScore(4)
        .cafeId(3)
        .email("isa0123@gmail.com").build();

    assertThat(reviewRepository.addReview(reviewDTO)).isTrue();
  }

  //# 4. 카페 ID, 이메일, 최신 날짜의 데이터를 리뷰 ID 받아오기
//  SELECT REVIEW_ID FROM REVIEW WHERE CAFE_ID = '3' AND EMAIL ='ANYUJIN0901@GMAIL.COM' ORDER BY REVIEW_DATE LIMIT 1;
  @Test
  public void getReviewIdbyCafeIdAndEmailTest() {
    ReviewDTO reviewDTO = ReviewDTO.builder()
        .email("rei0203@gmail.com")
        .cafeId(42).build();

    assertThat(reviewRepository.getReviewIdByCafeIdAndEmail(reviewDTO)).isEqualTo(6);
  }

//  7) 상세페이지 리뷰 조회
//# 1. 최신 날짜의 리뷰 2개 불러오기
//  SELECT REVIEW_CONTENTS, REVIEW_DATE, STAR_SCORE FROM REVIEW WHERE CAFE_ID = 3 LIMIT 2 OFFSET 2;

  @Test
  public void getTwoReviewsTest() {
    List<ReviewDTO> reviewDTOList = reviewRepository.getTwoReviews(3, PageRequest.of(0, 2));

    assertThat(reviewDTOList.size()).isEqualTo(2);
  }

  //  10) 리뷰 삭제
//  DELETE FROM REVIEW WHERE REVIEW_ID = 3;
  @Test
  public void removeReviewTest() {

    assertThat(reviewRepository.removeReview(3)).isTrue();
  }
}
