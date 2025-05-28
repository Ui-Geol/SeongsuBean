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
  private Long menuId;
  @Column(nullable = false)
  private String menuCategory;
  @Column(nullable = false)
  private String menuName;
  @Column(nullable = false)
  private Long price;
  @Column
  private String description;
  @Column
  private String imageUrl;
  @Column(nullable = false)
  private Long cafeId;

}