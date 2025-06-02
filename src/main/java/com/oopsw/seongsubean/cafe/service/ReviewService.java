package com.oopsw.seongsubean.cafe.service;

import com.oopsw.seongsubean.cafe.domain.ReviewImage;
import com.oopsw.seongsubean.cafe.dto.ReviewDTO;
import com.oopsw.seongsubean.cafe.repository.jparepository.ReviewImageRepository;
import com.oopsw.seongsubean.cafe.repository.mybatisrepository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
  private final ReviewRepository reviewRepository;
  private final ReviewImageRepository reviewImageRepository;

  //리뷰 생성
  public boolean addReview(ReviewDTO reviewDTO, List<ReviewImage> reviewImageList) {
    boolean result = reviewRepository.addReview(reviewDTO);
    for (ReviewImage reviewImage : reviewImageList) {
      reviewImageRepository.save(reviewImage);
    }

    return result;
  }
  //리뷰 삭제
  public boolean removeReview(Integer reviewId) {
    return reviewRepository.removeReview(reviewId);
  }
  //리뷰 조회
  public List<ReviewDTO> getReviews(Pageable pageable) {
    return reviewRepository.getTwoReviews(pageable);
  }

}
