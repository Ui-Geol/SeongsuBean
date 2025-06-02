package com.oopsw.seongsubean.cafe.dto;

import com.oopsw.seongsubean.cafe.domain.OperationTime;
import java.util.List;
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


}
