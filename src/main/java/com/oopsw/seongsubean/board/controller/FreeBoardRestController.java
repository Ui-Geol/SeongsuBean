package com.oopsw.seongsubean.board.controller;

import com.oopsw.seongsubean.account.dto.UserDTO;
import com.oopsw.seongsubean.auth.AccountDetails;
import com.oopsw.seongsubean.board.dto.FreeBoardCommentDTO;
import com.oopsw.seongsubean.board.dto.FreeBoardDTO;
import com.oopsw.seongsubean.board.service.FreeBoardService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import java.util.UUID;
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
          @AuthenticationPrincipal AccountDetails accountDetails,
          @RequestParam String title,
          @RequestParam String content,
          @RequestParam String headWord,
          @RequestParam(required = false) List<MultipartFile> images) throws IOException {
    UserDTO user = accountDetails.getUser();
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
    }
    String email = user.getEmail();
    FreeBoardDTO dto = FreeBoardDTO.builder()
        .title(title)
        .content(content)
        .email(email)
        .headWord(headWord)
        .build();
    List<String> imagePaths = new ArrayList<>();
    if (images != null) {
      String uploadDir = new File("src/main/resources/static/images/upload/free/" + email).getAbsolutePath();
      Path uploadPath = Paths.get(uploadDir);
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }
      for (MultipartFile file : images) {
        if (!file.isEmpty()) {
          String originalFilename = file.getOriginalFilename();
          String newFilename = UUID.randomUUID() + "_" + originalFilename;
          Path filePath = uploadPath.resolve(newFilename);
          try {
            file.transferTo(filePath.toFile());
            imagePaths.add("/images/upload/free/" + email + "/" + newFilename);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
    boolean success = freeBoardService.addFreeBoard(dto, imagePaths);
    return ResponseEntity.ok(Map.of("success", success, "id", dto.getFreeBoardId()));
  }

  @GetMapping("/list")
  public ResponseEntity<Map<String, Object>> getFreeBoardList(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "12") int size) {
    int offset = (page - 1) * size;
    List<FreeBoardDTO> list = freeBoardService.getFreeBoardList(offset, size);
    int totalCount = freeBoardService.getTotalFreeBoardCount();
    int totalPages = (int) Math.ceil((double) totalCount / size);
    Map<String, Object> result = Map.of(
            "content", list,
            "currentPage", page,
            "totalPages", totalPages
    );
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getFreeBoardDetail(
          @PathVariable("id") Integer id) {
    FreeBoardDTO dto = freeBoardService.getFreeBoardDetail(id);
    if (dto == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(Map.of("error", "해당 게시글이 존재하지 않습니다."));
    }
    return ResponseEntity.ok(dto);
  }

  @PutMapping("/post/{id}")
  public ResponseEntity<Map<String, Object>> setFreeBoard(
          @AuthenticationPrincipal AccountDetails accountDetails,
          @PathVariable("id") Integer id,
          @RequestBody FreeBoardDTO dto) {
    if (accountDetails == null || accountDetails.getUser() == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body(Map.of("updated", false, "message", "로그인이 필요합니다."));
    }
    String loginEmail = accountDetails.getUser().getEmail();
    String postOwnerEmail = freeBoardService.getFreeBoardOwnerEmail(id);
    if (!loginEmail.equals(postOwnerEmail)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
              .body(Map.of("updated", false, "message", "본인의 게시글만 수정할 수 있습니다."));
    }
    dto.setFreeBoardId(id);
    boolean result = freeBoardService.setFreeBoard(dto, List.of());
    return ResponseEntity.ok(Map.of("updated", result));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteFreeBoard(
          @AuthenticationPrincipal AccountDetails accountDetails,
          @PathVariable("id") Integer id) {
    if (accountDetails == null || accountDetails.getUser() == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body(Map.of("deleted", false, "message", "로그인이 필요합니다."));
    }
    String loginEmail = accountDetails.getUser().getEmail();
    String postOwnerEmail = freeBoardService.getFreeBoardOwnerEmail(id);
    if (!loginEmail.equals(postOwnerEmail)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
              .body(Map.of("deleted", false, "message", "본인의 게시글만 삭제할 수 있습니다."));
    }
    boolean result = freeBoardService.removeFreeBoard(id);
    return ResponseEntity.ok(Map.of("deleted", result));
  }

  /* comment */
  @PostMapping("/comment")
  public ResponseEntity<?> addComment(
          @AuthenticationPrincipal AccountDetails accountDetails,
          @RequestParam String comment,
          @RequestParam Integer freeBoardId) {
    if (accountDetails == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "로그인이 필요합니다."));
    }
    String email = accountDetails.getUser().getEmail();
    FreeBoardCommentDTO dto = FreeBoardCommentDTO.builder()
        .content(comment)
        .freeBoardId(freeBoardId)
        .email(email)
        .build();
    boolean result = freeBoardService.addFreeBoardComment(dto);
    return ResponseEntity.ok(Map.of("success", result));
  }

  @GetMapping("/comment/{id}")
  public ResponseEntity<List<FreeBoardCommentDTO>> getComments(
          @PathVariable("id") Integer boardId) {
    List<FreeBoardCommentDTO> comments = freeBoardService.getFreeBoardComments(boardId);
    return ResponseEntity.ok(comments);
  }

  @GetMapping("/auth/email")
  public ResponseEntity<?> getCurrentUserEmail(
          @AuthenticationPrincipal AccountDetails accountDetails) {
    if (accountDetails == null) {
      return ResponseEntity.ok(Map.of(
              "success", false,
              "email", "",
              "message", "비회원입니다."
      ));
    }
    String email = accountDetails.getUser().getEmail();
    return ResponseEntity.ok(Map.of(
            "success", true,
            "email", email
    ));
  }

  @DeleteMapping("/comment/{commentId}")
  public ResponseEntity<?> removeComment(
          @PathVariable("commentId") Integer freeBoardCommentId,
          @AuthenticationPrincipal AccountDetails accountDetails) {
    if (accountDetails == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "로그인이 필요합니다."));
    }
    String loginEmail = accountDetails.getUser().getEmail();
    String commentOwnerEmail = freeBoardService.getCommentOwnerEmail(freeBoardCommentId);
    if (!loginEmail.equals(commentOwnerEmail)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("success", false, "message", "본인의 댓글만 삭제할 수 있습니다."));
    }
    boolean result = freeBoardService.removeFreeBoardComment(freeBoardCommentId);
    return ResponseEntity.ok(Map.of("success", result));
  }

  /* search */
  @GetMapping("/search")
  public ResponseEntity<Map<String, Object>> getFreeBoardListSearch(
          @RequestParam("type") String type,
          @RequestParam("keyword") String keyword,
          @RequestParam("page") int page,
          @RequestParam("size") int size) {
    if (keyword == null || keyword.trim().isEmpty()) {
      throw new IllegalArgumentException("검색어는 null이거나 공백일 수 없습니다."); //서비스는 간단하게 유지
    }
    List<FreeBoardDTO> all = freeBoardService.searchFreeBoard(type, keyword);
    int total = all.size();
    int from = Math.min((page-1) * size, total);
    int to = Math.min(from + size, total);
    List<FreeBoardDTO> paged = all.subList(from, to);
    return ResponseEntity.ok(Map.of(
            "content", paged,
            "totalPages", (int) Math.ceil((double) total / size),
            "currentPage", page
    ));
  }
}
