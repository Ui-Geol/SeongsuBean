package com.oopsw.seongsubean.cafe.dto;

import java.time.LocalDateTime;
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
public class ReviewDTO {

  private Integer reviewId;
  private String reviewContents;
  private LocalDateTime reviewDate;
  private Integer starScore;
  private Integer cafeId;
  private String email;
}
