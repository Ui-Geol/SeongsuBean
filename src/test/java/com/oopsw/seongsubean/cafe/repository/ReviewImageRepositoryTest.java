package com.oopsw.seongsubean.cafe.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.oopsw.seongsubean.cafe.domain.ReviewImage;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@Transactional
class ReviewImageRepositoryTest {

  @Autowired
  private ReviewImageRepository reviewImageRepository;

  //ReviewImage 쓰기

  //  # 5. 리뷰 ID를 사용해서 리뷰 이미지 추가
//  INSERT INTO REVIEW_IMAGE (IMAGE, REVIEW_ID) VALUES ('/images/cafe/Cafe39.png', (SELECT REVIEW_ID FROM REVIEW WHERE CAFE_ID = 3 AND EMAIL ='ANYUJIN0901@GMAIL.COM' ORDER BY REVIEW_DATE LIMIT 1));
  @Test
  @DisplayName("리뷰 이미지 추가 테스트")
  public void addReviewImageTest() {
    ReviewImage newReviewImage = ReviewImage.builder()
        .image("/images/cafe/Cafe2.png")
        .reviewId(3)
        .build();
    ReviewImage savedReviewImage = reviewImageRepository.save(newReviewImage);

    assertThat(savedReviewImage).isNotNull();
    assertThat(savedReviewImage.getReviewId()).isEqualTo(newReviewImage.getReviewId());
    assertThat(savedReviewImage.getReviewImageId()).isEqualTo(newReviewImage.getReviewImageId());
    log.info(String.valueOf(savedReviewImage.getReviewImageId()));
  }

  //  # 3. 리뷰 ID를 통해 이미지 불러오기
//  SELECT IMAGE FROM REVIEW_IMAGE WHERE REVIEW_ID = 3;
  @Test
  @DisplayName("리뷰 이미지 조회 테스트")
  public void getReviewImageByReviewIdTest() {
    Optional<ReviewImage> savedReviewImage = reviewImageRepository.findById(3);
    assertThat(savedReviewImage).isPresent();
    assertThat(savedReviewImage.get().getImage()).isEqualTo("/images/cafe/Cafe34.png");

  }

  //  11) 리뷰 이미지 삭제
//  DELETE FROM REVIEW_IMAGE WHERE REVIEW_ID = 3;
  @Test
  @DisplayName("리뷰 이미지 삭제 테스트")
  public void removeReviewImageByReviewIdTest() {
    reviewImageRepository.deleteById(3);
    assertThat(reviewImageRepository.findById(3)).isNotPresent();
  }
}