package com.oopsw.seongsubean.board.controller;

import com.oopsw.seongsubean.board.dto.FreeBoardCommentDTO;
import com.oopsw.seongsubean.board.dto.FreeBoardDTO;
import com.oopsw.seongsubean.board.service.FreeBoardService;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/free")
public class FreeBoardRestController {
  private final FreeBoardService freeBoardService;
  public FreeBoardRestController(FreeBoardService freeBoardService) {
    this.freeBoardService = freeBoardService;
  }

  @GetMapping("/list")
  public List<FreeBoardDTO> getFreeBoardList() {
    return freeBoardService.getFreeBoardList();
  }

  @GetMapping("/detail/{id}")
  public FreeBoardDTO getFreeBoardDetail(@PathVariable int id) {
    return freeBoardService.getFreeBoardDetail(id);
  }

  @PostMapping
  public ResponseEntity<?> addFreeBoard(@RequestBody FreeBoardDTO dto) {
    boolean success = freeBoardService.addFreeBoard(dto, null); // 이미지 업로드 안 하면 null
    return ResponseEntity.ok(Map.of("success", success));
  }

  @PostMapping("/comment")
  public ResponseEntity<?> addComment(@RequestBody FreeBoardCommentDTO comment) {
    boolean result = freeBoardService.addFreeBoardComment(comment);
    return ResponseEntity.ok(Map.of("success", result));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> removeFreeBoard(@PathVariable int id) {
    boolean result = freeBoardService.removeFreeBoard(id);
    return ResponseEntity.ok(Map.of("success", result));
  }
}
