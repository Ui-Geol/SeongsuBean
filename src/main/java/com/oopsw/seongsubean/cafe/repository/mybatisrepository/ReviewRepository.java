package com.oopsw.seongsubean.cafe.repository.mybatisrepository;

import com.oopsw.seongsubean.cafe.dto.ReviewDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

@Mapper
public interface ReviewRepository {

  public Boolean addReview(ReviewDTO reviewDTO);

  public Integer getReviewIdByCafeIdAndEmail(ReviewDTO reviewDTO);

  List<ReviewDTO> getTwoReviews(@Param("pageable") Pageable pageable);

  public Boolean removeReview(@Param("reviewId") Integer reviewId);

}
