package com.oopsw.seongsubean.board.repository;

import com.oopsw.seongsubean.board.dto.ReportBoardDTO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportBoardRepository {
  public boolean addReportBoard(ReportBoardDTO dto);
  public boolean addReportBoardImage(Map<String, Object> map);
  public List<ReportBoardDTO> getReportBoardList(); //게시판 조회
  public ReportBoardDTO getReportBoardDetail(int reportBoardId); //게시글 조회
  public List<String> getReportBoardDetailImages(int reportBoardId);
  public boolean setReportBoardDetail(ReportBoardDTO dto);
  public boolean removeReportBoard(int reportBoardId);
  public boolean removeReportBoardImages(int reportBoardId);
}
