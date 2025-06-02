package com.oopsw.seongsubean.cafe.repository.jparepository;

import com.oopsw.seongsubean.cafe.domain.ReviewImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Integer> {

  List<ReviewImage> findReviewImageByReviewId(Integer reviewId);
}
