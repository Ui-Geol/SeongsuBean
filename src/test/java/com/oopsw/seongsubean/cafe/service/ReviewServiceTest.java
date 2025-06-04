package com.oopsw.seongsubean.cafe.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.oopsw.seongsubean.cafe.domain.ReviewImage;
import com.oopsw.seongsubean.cafe.dto.ReviewDTO;
import com.oopsw.seongsubean.cafe.dto.TotalReviewDTO;
import com.oopsw.seongsubean.cafe.repository.mybatisrepository.ReviewRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Slf4j
@SpringBootTest
public class ReviewServiceTest {

  @Autowired
  private ReviewService reviewService;
  @Autowired
  private ReviewRepository reviewRepository;

  //리뷰 생성
  @Test
  public void addReviewTest() {
    ReviewDTO reviewDTO = ReviewDTO.builder()
        .reviewContents("쌉꿀마 맛꿀마")
        .starScore(4)
        .cafeId(3)
        .email("isa0123@gmail.com").build();

    List<ReviewImage> reviewImageList = new ArrayList<>();
    reviewImageList.add(ReviewImage.builder().image("/images/cafe/Cafe4.png").reviewId(3).build());
    reviewImageList.add(ReviewImage.builder().image("/images/cafe/Cafe5.png").reviewId(3).build());
    reviewImageList.add(ReviewImage.builder().image("/images/cafe/Cafe6.png").reviewId(3).build());

    assertThat(reviewService.addReview(reviewDTO, reviewImageList)).isTrue();

  }

  //리뷰 삭제
//  public boolean removeReview(Integer reviewId) {
//    return reviewRepository.removeReview(reviewId);
//  }
  @Test
  public void removeReviewTest() {
    assertThat(reviewService.removeReview(3)).isTrue();
  }

  //리뷰 조회
//  public List<ReviewDTO> getReviews(Pageable pageable) {
//    return reviewRepository.getTwoReviews(pageable);
//  }
  @Test
  public void getReviewTest() {
    Pageable pageable = PageRequest.of(0, 2);
    List<TotalReviewDTO> totalReviewDTOList = reviewService.getReviews(3, pageable);

    assertThat(totalReviewDTOList.size()).isEqualTo(2);
  }
}
