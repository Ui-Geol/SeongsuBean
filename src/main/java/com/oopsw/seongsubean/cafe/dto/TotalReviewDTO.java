package com.oopsw.seongsubean.cafe.dto;

import com.oopsw.seongsubean.cafe.domain.ReviewImage;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TotalReviewDTO {

  private String userNickname;
  private String userImage;
  private ReviewDTO reviewDTO;
  private List<ReviewImage> reviewImage;
}
