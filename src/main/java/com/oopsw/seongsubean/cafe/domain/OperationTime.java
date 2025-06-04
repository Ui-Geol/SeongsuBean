package com.oopsw.seongsubean.cafe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class OperationTime {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer operationTimeId;
  @Column(nullable = false)
  private String weekday;
  @Column(nullable = false)
  private LocalTime openTime;
  @Column(nullable = false)
  private LocalTime closeTime;
  @Column(nullable = false)
  private Integer cafeId;

  public boolean isOpenNow() {
    if (LocalTime.now().isAfter(openTime) && LocalTime.now().isBefore(closeTime)) {
      return true;
    }

    return false;
  }

}
