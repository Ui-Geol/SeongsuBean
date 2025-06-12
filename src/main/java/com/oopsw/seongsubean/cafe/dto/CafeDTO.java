package com.oopsw.seongsubean.cafe.dto;

import com.oopsw.seongsubean.cafe.domain.OperationTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;

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
  /**
   * 클라이언트에서 JSON으로 전송한 영업시간 리스트를 직접 매핑 (LocalTime 역직렬화를 위해 OperationTimeDTO에 @JsonFormat 사용)
   */
  private List<OperationTimeDTO> operationTimes;

  @JsonIgnore
  public String getFullAddress() {
    return address + ", " + detailAddress;
  }

  @JsonIgnore
  public List<OperationTime> convertToOperationTimeList(List<OperationTimeDTO> operationTimes,
      Integer cafeId) {
    if (operationTimes == null) {
      return null;
    }
    List<OperationTime> operationTimesList = new ArrayList<>();

    for (OperationTimeDTO operationTimeDTO : operationTimes) {
      operationTimesList.add(OperationTime.builder()
          .weekday(operationTimeDTO.getWeekday())
          .openTime(LocalTime.parse(operationTimeDTO.getOpenTime()))
          .closeTime(LocalTime.parse(operationTimeDTO.getCloseTime()))
          .cafeId(cafeId)
          .build());
    }

    return operationTimesList;
  }

}