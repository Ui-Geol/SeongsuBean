package com.oopsw.seongsubean.board.service;

import com.oopsw.seongsubean.board.dto.FreeBoardCommentDTO;
import com.oopsw.seongsubean.board.dto.FreeBoardDTO;
import com.oopsw.seongsubean.board.repository.FreeBoardRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FreeBoardService {
  private final FreeBoardRepository freeBoardRepository;
  public FreeBoardService(FreeBoardRepository freeBoardRepository) {
    this.freeBoardRepository = freeBoardRepository;
  }
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
    freeBoardRepository.removeFreeBoardComments(freeBoardId);
    return freeBoardRepository.removeFreeBoard(freeBoardId);
  }
  public List<FreeBoardDTO> getFreeBoardList() {
    return freeBoardRepository.getFreeBoardList();
  }
  public FreeBoardDTO getFreeBoardDetail(Integer freeBoardId) {
    FreeBoardDTO dto = freeBoardRepository.getFreeBoardDetail(freeBoardId);
    dto.setImages(freeBoardRepository.getFreeBoardDetailImages(freeBoardId));
    dto.setComments(freeBoardRepository.getFreeBoardDetailComments(freeBoardId));
    return dto;
  }
  public boolean addFreeBoardComment(FreeBoardCommentDTO dto) {
    return freeBoardRepository.addFreeBoardComment(dto);
  }
  public boolean removeFreeBoardComments(Integer freeBoardId) {
    return freeBoardRepository.removeFreeBoardComments(freeBoardId);
  }
}
