package com.oopsw.seongsubean.cafe.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
public class OperationTimeDTO {
  private String weekday;     // ex) "월", "화", …
  private LocalTime openTime; // ex) "09:30" → LocalTime으로 변환
  private LocalTime closeTime; // ex) "18:00"
}