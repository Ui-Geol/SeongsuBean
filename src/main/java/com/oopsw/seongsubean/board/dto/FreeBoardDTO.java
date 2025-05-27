package com.oopsw.seongsubean.board.dto;


import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreeBoardDTO {
  private String freeBoardId;
  private String freeBoardTitle;
  private String freeBoardHeadWord;
  private Long content;
  private Date createdDate;
}
