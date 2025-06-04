package com.oopsw.seongsubean.board.repository;

import com.oopsw.seongsubean.board.dto.FreeBoardCommentDTO;
import com.oopsw.seongsubean.board.dto.FreeBoardDTO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FreeBoardRepository {
  public boolean addFreeBoard(FreeBoardDTO dto);
  public boolean addFreeBoardImages(Map<String, Object> map);
  public List<FreeBoardDTO> getFreeBoardList(); //게시판 조회
  public List<FreeBoardDTO> getFreeBoardList(@Param("offset") int offset, @Param("size") int size); //게시판 페이징용
  public FreeBoardDTO getFreeBoardDetail(Integer freeBoardId); //게시글 조회
  public List<String> getFreeBoardDetailImages(Integer freeBoardId);
  public List<FreeBoardCommentDTO> getFreeBoardDetailComments(Integer freeBoardId);
  public boolean setFreeBoardDetail(FreeBoardDTO dto);
  public Integer removeFreeBoard(Integer freeBoardId);
  public boolean removeFreeBoardImages(Integer freeBoardId);
  public Integer removeFreeBoardComment(Integer freeBoardId);
  public boolean addFreeBoardComment(FreeBoardCommentDTO dto);
  public String getCommentOwnerEmail(Integer freeBoardCommentId);
  public String getFreeBoardOwnerEmail(Integer freeBoardCommentId);
  public int getTotalFreeBoardCount();
  public List<FreeBoardDTO> searchFreeBoard(@Param("type") String type, @Param("keyword") String keyword);
}
