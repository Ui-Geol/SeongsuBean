package com.oopsw.seongsubean.cafe.service;

import com.oopsw.seongsubean.account.dto.UserDTO;
import com.oopsw.seongsubean.account.repository.AccountRepository;
import com.oopsw.seongsubean.cafe.domain.ReviewImage;
import com.oopsw.seongsubean.cafe.dto.ReviewDTO;
import com.oopsw.seongsubean.cafe.dto.TotalReviewDTO;
import com.oopsw.seongsubean.cafe.repository.jparepository.ReviewImageRepository;
import com.oopsw.seongsubean.cafe.repository.mybatisrepository.ReviewRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final ReviewImageRepository reviewImageRepository;
  private final AccountRepository accountRepository;

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
  public List<TotalReviewDTO> getReviews(Integer cafeId, Pageable pageable) {
    System.out.println(cafeId);
    List<ReviewDTO> reviewDTOList = reviewRepository.getTwoReviews(cafeId, pageable);
    List<TotalReviewDTO> totalReviewDTOList = new ArrayList<>();
    for (ReviewDTO reviewDTO : reviewDTOList) {
      UserDTO userDTO = accountRepository.findByEmail(reviewDTO.getEmail());
      totalReviewDTOList.add(TotalReviewDTO.builder().userNickname(userDTO.getNickName()).userImage(
              userDTO.getImage()).reviewDTO(reviewDTO)
          .reviewImage(reviewImageRepository.findReviewImageByReviewId(
              reviewDTO.getReviewId())).build());

    }
    return totalReviewDTOList;
  }

}
