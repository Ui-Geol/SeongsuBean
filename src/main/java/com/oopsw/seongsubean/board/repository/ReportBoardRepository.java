package com.oopsw.seongsubean.board.repository;

import com.oopsw.seongsubean.board.dto.ReportBoardDTO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReportBoardRepository {
  public boolean addReportBoard(ReportBoardDTO dto);
  public boolean addReportBoardImages(Map<String, Object> map);
  public List<ReportBoardDTO> getReportBoardList(); //게시판 조회
  public List<ReportBoardDTO> getReportBoardList(@Param("offset") int offset, @Param("size") int size); //게시판 페이징용
  public ReportBoardDTO getReportBoardDetail(Integer reportBoardId); //게시글 조회
  public List<String> getReportBoardDetailImages(Integer reportBoardId);
  public boolean setReportBoardDetail(ReportBoardDTO dto);
  public Integer removeReportBoard(Integer reportBoardId);
  public boolean removeReportBoardImages(Integer reportBoardId);
  public String getReportBoardOwnerEmail(Integer freeBoardCommentId);
  public int getTotalReportBoardCount();
}
