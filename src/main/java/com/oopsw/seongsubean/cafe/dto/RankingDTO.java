package com.oopsw.seongsubean.cafe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RankingDTO {
  private Integer cafeId;
  private String cafeName;
  private Integer avgStarScore;
  private Integer reviewCount;

}
