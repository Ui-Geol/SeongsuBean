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
public class CafeDTO {

  private Integer cafeId;
  private String cafeName;
  private String address;
  private String detailAddress;
  private String zipCode;
  private String callNumber;
  private String introduction;
  private String status;
  private String mainImage;
  private String pageCreatedDate;
  private String email;

  public String getFullAddress() {
    return address + ", " + detailAddress;
  }

  public boolean isRestDay() {
    if (status.equals("휴무일")) {
      return true;
    }

    return false;
  }

}
