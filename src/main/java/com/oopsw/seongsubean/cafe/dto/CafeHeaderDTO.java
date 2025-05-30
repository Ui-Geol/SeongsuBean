package com.oopsw.seongsubean.cafe.dto;

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
public class CafeHeaderDTO {

  private String cafeName;
  private Float avgRating;
  private Integer totalCount;
  private String mainImage;

}
