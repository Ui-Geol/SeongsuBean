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
          @AuthenticationPrincipal AccountDetails accountDetails,
          @RequestParam String title,
          @RequestParam String content,
          @RequestParam String headWord,
          @RequestParam(required = false) List<MultipartFile> images) throws IOException {
    UserDTO user = accountDetails.getUser();
    System.out.println(accountDetails);
    System.out.println("login" + user.getEmail());
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
    }

    String email = user.getEmail(); // 로그인된 유저의 이메일
    FreeBoardDTO dto = FreeBoardDTO.builder()
        .title(title)
        .content(content)
        .email(email)
        .headWord(headWord)
        .build();
    List<String> imagePaths = new ArrayList<>();
    if (images != null) {
      for (MultipartFile file : images) {
        if (!file.isEmpty()) {
          String originalFilename = file.getOriginalFilename();
          String uploadDir = "/path/to/static/images/upload/free/" + email; // 임시 경로 (ID 아직 없음)
          File dir = new File(uploadDir);
          if (!dir.exists())
            dir.mkdirs();
          Path filePath = Paths.get(uploadDir, originalFilename);
          try {
            Files.copy(file.getInputStream(), filePath);
            imagePaths.add(originalFilename);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
      boolean success = freeBoardService.addFreeBoard(dto, imagePaths);
      return ResponseEntity.ok(Map.of("success", success, "id", dto.getFreeBoardId()));
      //return null;
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
  public ResponseEntity<?> addComment(@AuthenticationPrincipal AccountDetails accountDetails,
      @RequestParam String comment,
      @RequestParam Integer freeBoardId) {
    if (accountDetails == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "로그인이 필요합니다."));
    }
    String email = accountDetails.getUser().getEmail(); // 로그인한 사용자의 이메일
    FreeBoardCommentDTO dto = FreeBoardCommentDTO.builder()
        .content(comment)
        .freeBoardId(freeBoardId)
        .email(email)
        .build();
    boolean result = freeBoardService.addFreeBoardComment(dto);
    return ResponseEntity.ok(Map.of("success", result));
  }
  @GetMapping("/comment/{id}")
  public ResponseEntity<List<FreeBoardCommentDTO>> getComments(@PathVariable("id") Integer boardId) {
    List<FreeBoardCommentDTO> comments = freeBoardService.getFreeBoardComments(boardId);
    return ResponseEntity.ok(comments);
  }
//  @DeleteMapping("/comment/{id}")
//  public ResponseEntity<?> removeComment(@PathVariable("id") Integer commentId){
//    boolean result = freeBoardService.removeFreeBoardComment(commentId);
//    return ResponseEntity.ok(Map.of("success", result));
//  }
  @GetMapping("/auth/email")
  public ResponseEntity<?> getCurrentUserEmail(@AuthenticationPrincipal AccountDetails accountDetails) {
    // 로그인 안 된 사용자도 접근 가능하게 처리
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
  public ResponseEntity<?> removeComment(@PathVariable("commentId") Integer freeBoardCommentId,
                                         @AuthenticationPrincipal AccountDetails accountDetails) {

    if (accountDetails == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "로그인이 필요합니다."));
    }

    String loginEmail = accountDetails.getUser().getEmail();
    String commentOwnerEmail = freeBoardService.getCommentOwnerEmail(freeBoardCommentId);

    System.out.println("✔ 로그인 사용자 email: " + loginEmail);
    System.out.println("✔ 댓글 작성자 email: " + commentOwnerEmail);
    if (!loginEmail.equals(commentOwnerEmail)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("success", false, "message", "본인의 댓글만 삭제할 수 있습니다."));
    }

    boolean result = freeBoardService.removeFreeBoardComment(freeBoardCommentId);
    System.out.println("🗑 댓글 삭제 결과: " + result);

    return ResponseEntity.ok(Map.of("success", result));
  }

}
