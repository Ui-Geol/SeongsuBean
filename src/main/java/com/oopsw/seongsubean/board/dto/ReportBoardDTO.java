package com.oopsw.seongsubean.board.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportBoardDTO {
  private Integer reportBoardId;
  private String title;
  private String content;
  private LocalDateTime createdDate;
  private String email;
  private List<String> images;
}
