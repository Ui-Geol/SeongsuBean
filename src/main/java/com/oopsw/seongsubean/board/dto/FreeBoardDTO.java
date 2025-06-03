package com.oopsw.seongsubean.board.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreeBoardDTO {
  private Integer freeBoardId;
  private String title;
  private String headWord;
  private String content;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
  private LocalDateTime createdDate;
  private String email;
  private List<FreeBoardCommentDTO> comments;
  private List<String> images;
  private String nickName;
  private String thumbnailImage;
}
