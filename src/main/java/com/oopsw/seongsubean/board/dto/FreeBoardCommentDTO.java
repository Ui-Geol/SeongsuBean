package com.oopsw.seongsubean.board.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreeBoardCommentDTO {
  private Integer freeBoardId;
  private String content;
  private LocalDateTime createdDate;
  private Integer freeBoardCommentId;
  private String email;
}
