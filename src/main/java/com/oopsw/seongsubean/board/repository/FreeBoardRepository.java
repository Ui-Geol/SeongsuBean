package com.oopsw.seongsubean.board.repository;

import com.oopsw.seongsubean.board.dto.FreeBoardCommentDTO;
import com.oopsw.seongsubean.board.dto.FreeBoardDTO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FreeBoardRepository {
  public boolean addFreeBoard(FreeBoardDTO dto);
  public boolean addFreeBoardImages(Map<String, Object> map);
  public List<FreeBoardDTO> getFreeBoardList(); //게시판 조회
  public FreeBoardDTO getFreeBoardDetail(int freeBoardId); //게시글 조회
  public List<String> getFreeBoardDetailImages(int freeBoardId);
  public List<FreeBoardCommentDTO> getFreeBoardDetailComments(int freeBoardId);
  public boolean setFreeBoardDetail(FreeBoardDTO dto);
  public boolean removeFreeBoard(int freeBoardId);
  public boolean removeFreeBoardImages(int freeBoardId);
  public boolean removeFreeBoardComments(int freeBoardId);
  public boolean addFreeBoardComment(FreeBoardCommentDTO dto);
}
