package com.oopsw.seongsubean.cafe.dto;
import lombok.Data;

//주소를 받아서 json을 수동으로 넘기지 않기 위해 추가로 만든 DTO
@Data
public class ResponseAdressDTO {
  private String address;      // 다음에서 받은 주소
  private String zipCode;      // 다음에서 받은 우편번호
  private String detailAddress;
}
