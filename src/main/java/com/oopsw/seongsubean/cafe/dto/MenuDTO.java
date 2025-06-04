package com.oopsw.seongsubean.cafe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MenuDTO {

  private Integer menuId;
  private String menuCategory;
  private String menuName;
  private Integer price;
  private String description;
  private String image;
  private Integer cafeId;


}
