package com.oopsw.seongsubean.board.controller;

import com.oopsw.seongsubean.account.dto.UserDTO;
import com.oopsw.seongsubean.board.dto.FreeBoardCommentDTO;
import com.oopsw.seongsubean.board.dto.FreeBoardDTO;
import com.oopsw.seongsubean.board.service.FreeBoardService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/free")
public class FreeBoardRestController {
  private final FreeBoardService freeBoardService;
  public FreeBoardRestController(FreeBoardService freeBoardService) {
    this.freeBoardService = freeBoardService;
  }
  @PostMapping
  public ResponseEntity<?> addFreeBoard(
          @AuthenticationPrincipal UserDTO loginUser,
          @ModelAttribute FreeBoardDTO dto,
          @RequestParam(required = false) List<MultipartFile> images) throws IOException {
    if (loginUser == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
    }

    String email = loginUser.getEmail(); // 로그인된 유저의 이메일
    List<String> imagePaths = new ArrayList<>();
    if (images != null) {
      for (MultipartFile file : images) {
        if (!file.isEmpty()) {
          String fileName = file.getOriginalFilename();
          Path savePath = Paths.get("/images/board/free", fileName);
          Files.copy(file.getInputStream(), savePath);
          imagePaths.add("/images/board/free" + fileName);
        }
      }
    }
    boolean success = freeBoardService.addFreeBoard(dto, imagePaths);
    return ResponseEntity.ok(Map.of("success", success, "id", dto.getFreeBoardId()));
  }

  @GetMapping("/list")
  public ResponseEntity<List<FreeBoardDTO>> getFreeBoardList(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "12") int size) {
    HttpHeaders headers = new HttpHeaders();
    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
    int offset = (page - 1) * size;
    List<FreeBoardDTO> list = freeBoardService.getFreeBoardList(offset, size);
    return new ResponseEntity<>(list, headers, HttpStatus.OK);
  }
  @GetMapping("/{id}")
  public ResponseEntity<?> getFreeBoardDetail(@PathVariable("id") Integer id) {
    FreeBoardDTO dto = freeBoardService.getFreeBoardDetail(id);
    if (dto == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(Map.of("error", "해당 게시글이 존재하지 않습니다."));
    }
    return ResponseEntity.ok(dto);
  }

  @PutMapping("/post/{id}")
  public Map<String, Object> setFreeBoard(@PathVariable("id") Integer id,
                                          @RequestBody FreeBoardDTO dto) {
    dto.setFreeBoardId(id);
    boolean result = freeBoardService.setFreeBoard(dto, List.of());
    return Map.of("updated", result);
  }
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteFreeBoard(@PathVariable("id") Integer id) {
    boolean result = freeBoardService.removeFreeBoard(id);
    return ResponseEntity.ok(Map.of("deleted", result));
  }


  /* comment */
  @PostMapping("/comment")
  public ResponseEntity<?> addComment(@RequestBody FreeBoardCommentDTO comment) {
    boolean result = freeBoardService.addFreeBoardComment(comment);
    return ResponseEntity.ok(Map.of("success", result));
  }
  @GetMapping("/comment/{id}")
  public ResponseEntity<List<FreeBoardCommentDTO>> getComments(@PathVariable("id") Integer boardId) {
    List<FreeBoardCommentDTO> comments = freeBoardService.getFreeBoardComments(boardId);
    return ResponseEntity.ok(comments);
  }
  @DeleteMapping("/comment/{id}")
  public ResponseEntity<?> removeComment(@PathVariable("id") Integer commentId){
    boolean result = freeBoardService.removeFreeBoardComment(commentId);
    return ResponseEntity.ok(Map.of("success", result));
  }
}
