package com.oopsw.seongsubean.cafe.repository.mybatisrepository;

import com.oopsw.seongsubean.cafe.dto.ReviewDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

@Mapper
public interface ReviewRepository {

  Boolean addReview(ReviewDTO reviewDTO);

  Integer getReviewIdByCafeIdAndEmail(ReviewDTO reviewDTO);

  List<ReviewDTO> getTwoReviews(Integer cafeId, @Param("pageable") Pageable pageable);

  Boolean removeReview(@Param("reviewId") Integer reviewId);

}
