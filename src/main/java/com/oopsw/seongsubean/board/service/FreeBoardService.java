package com.oopsw.seongsubean.board.service;

import com.oopsw.seongsubean.board.dto.FreeBoardCommentDTO;
import com.oopsw.seongsubean.board.dto.FreeBoardDTO;
import com.oopsw.seongsubean.board.repository.FreeBoardRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class FreeBoardService {
  private final FreeBoardRepository freeBoardRepository;
  @Transactional
  public boolean addFreeBoard(FreeBoardDTO dto, List<String> imagePaths) {
    boolean result = freeBoardRepository.addFreeBoard(dto);
    if (!result) return false;
    for (String image : imagePaths) {
      Map<String, Object> map = new HashMap<>();
      map.put("freeBoardId", dto.getFreeBoardId());
      map.put("image", image);
      freeBoardRepository.addFreeBoardImages(map);
    }
    return true;
  }
  @Transactional
  public boolean setFreeBoard(FreeBoardDTO dto, List<String> newImages) {
    freeBoardRepository.removeFreeBoardImages(dto.getFreeBoardId());
    boolean result = freeBoardRepository.setFreeBoardDetail(dto);
    for (String image : newImages) {
      Map<String, Object> map = new HashMap<>();
      map.put("freeBoardId", dto.getFreeBoardId());
      map.put("image", image);
      freeBoardRepository.addFreeBoardImages(map);
    }
    return result;
  }
  @Transactional
  public boolean removeFreeBoard(Integer freeBoardId) {
    freeBoardRepository.removeFreeBoardImages(freeBoardId);
    int deletedCount = freeBoardRepository.removeFreeBoard(freeBoardId);
    return deletedCount > 0;
  }
  public List<FreeBoardDTO> getFreeBoardList() {
    return freeBoardRepository.getFreeBoardList();
  }
  public List<FreeBoardDTO> getFreeBoardList(int size, int offset) {
    return freeBoardRepository.getFreeBoardList(size, offset);
  }
  public FreeBoardDTO getFreeBoardDetail(Integer freeBoardId) {
    FreeBoardDTO dto = freeBoardRepository.getFreeBoardDetail(freeBoardId);
    if (dto == null) return null;
    List<String> images = freeBoardRepository.getFreeBoardDetailImages(freeBoardId);
    dto.setImages(images.stream().distinct().toList());
    return dto;
  }
  public boolean addFreeBoardComment(FreeBoardCommentDTO dto) {
    return freeBoardRepository.addFreeBoardComment(dto);
  }
  public List<FreeBoardCommentDTO> getFreeBoardComments(Integer freeBoardId) {
    return freeBoardRepository.getFreeBoardDetailComments(freeBoardId);
  }
  public boolean removeFreeBoardComment(Integer freeBoardCommentId) {
    int deletedCount = freeBoardRepository.removeFreeBoardComment(freeBoardCommentId);
    return deletedCount > 0;
  }
  public String getCommentOwnerEmail(Integer freeBoardCommentId) {
    return freeBoardRepository.getCommentOwnerEmail(freeBoardCommentId);
  }
  public String getFreeBoardOwnerEmail(Integer freeBoardId) {
    return freeBoardRepository.getFreeBoardOwnerEmail(freeBoardId);
  }
  public int getTotalFreeBoardCount() {
    return freeBoardRepository.getTotalFreeBoardCount();
  }
  public List<FreeBoardDTO> searchFreeBoard(String type, String keyword) {
    return freeBoardRepository.searchFreeBoard(type, keyword.trim());
  }
}
