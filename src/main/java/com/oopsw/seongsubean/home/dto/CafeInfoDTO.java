package com.oopsw.seongsubean.home.dto;

import lombok.Data;

@Data
public class CafeInfoDTO {
  private Long   cafeId;
  private String cafeName;
  private String address;
  private String detailAddress;
  private String zipCode;
  private String status;
  private String callNumber;
  private String introduction;
  private String mainImage;
}