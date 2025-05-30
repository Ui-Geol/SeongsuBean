package com.oopsw.seongsubean.board.service;

import com.oopsw.seongsubean.board.dto.ReportBoardDTO;
import com.oopsw.seongsubean.board.repository.ReportBoardRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportBoardService {
  private final ReportBoardRepository reportBoardRepository;
  public ReportBoardService(ReportBoardRepository reportBoardRepository) {
    this.reportBoardRepository = reportBoardRepository;
  }
  @Transactional
  public boolean addReportBoard(ReportBoardDTO dto, List<String> imagePaths) {
    boolean result = reportBoardRepository.addReportBoard(dto);
    if (!result) return false;
    for (String image : imagePaths) {
      Map<String, Object> map = new HashMap<>();
      map.put("reportBoardId", dto.getReportBoardId());
      map.put("image", image);
      reportBoardRepository.addReportBoardImages(map);
    }
    return true;
  }
  @Transactional
  public boolean setReportBoard(ReportBoardDTO dto, List<String> newImages) {
    reportBoardRepository.removeReportBoardImages(dto.getReportBoardId());
    boolean result = reportBoardRepository.setReportBoardDetail(dto);
    for (String image : newImages) {
      Map<String, Object> map = new HashMap<>();
      map.put("reportBoardId", dto.getReportBoardId());
      map.put("image", image);
      reportBoardRepository.addReportBoardImages(map);
    }
    return result;
  }
  @Transactional
  public boolean deleteReportBoard(Integer ReportBoardId) {
    reportBoardRepository.removeReportBoardImages(ReportBoardId);
    return reportBoardRepository.removeReportBoard(ReportBoardId);
  }
  public List<ReportBoardDTO> getReportBoardList() {
    return reportBoardRepository.getReportBoardList();
  }
  public ReportBoardDTO getReportBoardDetail(Integer ReportBoardId) {
    ReportBoardDTO dto = reportBoardRepository.getReportBoardDetail(ReportBoardId);
    if (dto == null) return null; //nullpointexception
    dto.setImages(reportBoardRepository.getReportBoardDetailImages(ReportBoardId));
    return dto;
  }
}
