package com.oopsw.seongsubean.board.controller;

import com.oopsw.seongsubean.account.dto.UserDTO;
import com.oopsw.seongsubean.auth.AccountDetails;
import com.oopsw.seongsubean.board.dto.ReportBoardDTO;
import com.oopsw.seongsubean.board.service.ReportBoardService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.UUID;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/reportboards")
public class ReportBoardRestController {

  private final ReportBoardService reportBoardService;

  public ReportBoardRestController(ReportBoardService reportBoardService) {
    this.reportBoardService = reportBoardService;
  }

  @PostMapping
  public ResponseEntity<?> addReportBoard(
          @AuthenticationPrincipal AccountDetails accountDetails,
          @RequestParam String title,
          @RequestParam String content,
          @RequestParam(required = false) List<MultipartFile> images) throws IOException {
    UserDTO user = accountDetails.getUser();
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
    }
    String email = user.getEmail();
    ReportBoardDTO dto = ReportBoardDTO.builder()
            .title(title)
            .content(content)
            .email(email)
            .build();

    List<String> imagePaths = new ArrayList<>();
    if (images != null) {
      String uploadDir = new File("src/main/resources/static/images/upload/report/" + email).getAbsolutePath();
      Path uploadPath = Paths.get(uploadDir);
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }
      for (MultipartFile file : images) {
        if (!file.isEmpty()) {
          String originalFilename = file.getOriginalFilename();
          String newFilename = UUID.randomUUID() + "_" + originalFilename;
          Path filePath = uploadPath.resolve(newFilename);
          file.transferTo(filePath.toFile());
          imagePaths.add("images/upload/report/" + email + "/" + newFilename);
        }
      }
    }
    boolean success = reportBoardService.addReportBoard(dto, imagePaths);
    return ResponseEntity.ok(Map.of("success", success, "id", dto.getReportBoardId()));
  }

  @GetMapping("/list")
  public ResponseEntity<Map<String, Object>> getReportBoardList(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "7") int size) {
    int offset = (page - 1) * size;
    List<ReportBoardDTO> list = reportBoardService.getReportBoardList(offset, size);
    int totalCount = reportBoardService.getTotalReportBoardCount();
    int totalPages = (int) Math.ceil((double) totalCount / size);
    return ResponseEntity.ok(Map.of(
            "content", list,
            "currentPage", page,
            "totalPages", totalPages
    ));
  }

  @GetMapping("/detail")
  public ResponseEntity<?> getReportBoardDetail(@RequestParam("id") Integer id) {
    ReportBoardDTO dto = reportBoardService.getReportBoardDetail(id);
    if (dto == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(Map.of("error", "해당 게시글이 존재하지 않습니다."));
    }
    return ResponseEntity.ok(dto);
  }

  @PutMapping("/post")
  public ResponseEntity<Map<String, Object>> setReportBoard(
          @AuthenticationPrincipal AccountDetails accountDetails,
          @RequestParam("id") Integer id,
          @RequestBody ReportBoardDTO dto) {
    if (accountDetails == null || accountDetails.getUser() == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body(Map.of("updated", false, "message", "로그인이 필요합니다."));
    }

    String loginEmail = accountDetails.getUser().getEmail();
    String postOwnerEmail = reportBoardService.getReportBoardOwnerEmail(id);
    if (!loginEmail.equals(postOwnerEmail)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
              .body(Map.of("updated", false, "message", "본인의 게시글만 수정할 수 있습니다."));
    }

    dto.setReportBoardId(id);
    boolean result = reportBoardService.setReportBoard(dto, List.of());
    return ResponseEntity.ok(Map.of("updated", result));
  }

  @DeleteMapping
  public ResponseEntity<?> deleteReportBoard(
          @AuthenticationPrincipal AccountDetails accountDetails,
          @RequestParam("id") Integer id) {
    if (accountDetails == null || accountDetails.getUser() == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body(Map.of("deleted", false, "message", "로그인이 필요합니다."));
    }

    String loginEmail = accountDetails.getUser().getEmail();
    String ownerEmail = reportBoardService.getReportBoardOwnerEmail(id);
    if (!loginEmail.equals(ownerEmail)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
              .body(Map.of("deleted", false, "message", "본인의 게시글만 삭제할 수 있습니다."));
    }

    boolean result = reportBoardService.removeReportBoard(id);
    return ResponseEntity.ok(Map.of("deleted", result));
  }

  @GetMapping("/auth/email")
  public ResponseEntity<?> getCurrentUserEmail(@AuthenticationPrincipal AccountDetails accountDetails) {
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
}

