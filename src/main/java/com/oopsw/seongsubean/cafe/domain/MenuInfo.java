package com.oopsw.seongsubean.cafe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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
  private String image;
  @Column(nullable = false)
  private Integer cafeId;

}