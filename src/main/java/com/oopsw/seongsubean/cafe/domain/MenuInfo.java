package com.oopsw.seongsubean.cafe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MenuInfo {

  @Id
  private Integer menuId;
  @Column(nullable = false)
  private String menuCategory;
  @Column(nullable = false)
  private String menuName;
  @Column(nullable = false)
  private Integer price;
  @Column
  private String description;
  @Column
  private String imageUrl;
  @Column(nullable = false)
  private Integer cafeId;

}