package com.oopsw.seongsubean.cafe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class cafeDTO {

  private Integer cafeId;
  private String cafeName;
  private String address;
  private String detailAdress;
  private String zipCode;
  private String callNumber;
  private String phoneNumber;
  private String introduction;
  private String status;
  private String mainImage;
  private String pageCreatedDate;
  private String email;

}
