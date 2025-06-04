package com.oopsw.seongsubean.cafe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OperationTimeDTO {

  // 요일 (예: "월", "화", ...)
  private String weekday;

  /**
   * "09:30" → LocalTime.of(9, 30) 형태로 매핑
   */
  @JsonFormat(pattern = "HH:mm")
  private String openTime;

  /**
   * "18:00" → LocalTime.of(18, 0) 형태로 매핑
   */
  @JsonFormat(pattern = "HH:mm")
  private String closeTime;
}
