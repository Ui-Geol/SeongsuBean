package com.oopsw.seongsubean.board.service;

import com.oopsw.seongsubean.board.dto.ReportBoardDTO;
import com.oopsw.seongsubean.board.repository.ReportBoardRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ReportBoardService {
  private final ReportBoardRepository reportBoardRepository;
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
  public boolean removeReportBoard(Integer reportBoardId) {
    reportBoardRepository.removeReportBoardImages(reportBoardId);
    int deletedCount = reportBoardRepository.removeReportBoard(reportBoardId);
    return deletedCount > 0;
  }
  public List<ReportBoardDTO> getReportBoardList() {
    return reportBoardRepository.getReportBoardList();
  }
  public List<ReportBoardDTO> getReportBoardList(int size, int offset) {
    return reportBoardRepository.getReportBoardList(size, offset);
  }
  public ReportBoardDTO getReportBoardDetail(Integer reportBoardId) {
    ReportBoardDTO dto = reportBoardRepository.getReportBoardDetail(reportBoardId);
    if (dto == null) return null; //nullpointexception
    List<String> images = reportBoardRepository.getReportBoardDetailImages(reportBoardId);
    dto.setImages(images.stream().distinct().toList());
    return dto;
  }

  public String getReportBoardOwnerEmail(Integer reportBoardId) {
    return reportBoardRepository.getReportBoardOwnerEmail(reportBoardId);
  }
  public int getTotalReportBoardCount() {
    return reportBoardRepository.getTotalReportBoardCount();
  }
}
