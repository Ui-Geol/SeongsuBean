package com.oopsw.seongsubean.cafe.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.oopsw.seongsubean.cafe.domain.ReviewImage;
import com.oopsw.seongsubean.cafe.repository.jparepository.ReviewImageRepository;
import com.oopsw.seongsubean.config.PropertyConfig;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PropertyConfig.class)
@TestMethodOrder(OrderAnnotation.class)
@Transactional
class ReviewImageRepositoryTest {

  @Autowired
  private PropertyConfig propertyConfig;

  @Autowired
  private ReviewImageRepository reviewImageRepository;

  @Test
  @DisplayName("리뷰 이미지 추가 테스트")
  @Order(2)
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

  @Test
  @DisplayName("리뷰 이미지 조회 테스트")
  @Order(1)
  public void getReviewImageByReviewIdTest() {
    Optional<ReviewImage> savedReviewImage = reviewImageRepository.findById(3);
    assertThat(savedReviewImage).isPresent();
    assertThat(savedReviewImage.get().getImage()).isEqualTo("/images/cafe/Cafe34.png");

  }

  @Test
  @DisplayName("리뷰 이미지 삭제 테스트")
  @Order(3)
  public void removeReviewImageByReviewIdTest() {
    reviewImageRepository.deleteById(3);
    assertThat(reviewImageRepository.findById(3)).isNotPresent();
  }
}