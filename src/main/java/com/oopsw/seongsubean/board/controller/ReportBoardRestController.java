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
@RequestMapping("/api/report")
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
    if(user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
    }
    String email = user.getEmail();
    ReportBoardDTO dto = ReportBoardDTO.builder()
            .title(title)
            .content(content)
            .email(email)
            .build();
    List<String> fileNames = new ArrayList<>();
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
          try {
            file.transferTo(filePath.toFile());
            fileNames.add("/images/upload/report/" + email + "/" + newFilename);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
    boolean success = reportBoardService.addReportBoard(dto, fileNames);
    return ResponseEntity.ok(Map.of("success", success, "id", dto.getReportBoardId()));
  }
  @GetMapping("/list")
  public ResponseEntity<Map<String, Object>> getReportBoardList(
          @RequestParam(defaultValue = "1", required = false) int page,
          @RequestParam(defaultValue = "7", required = false) int size) {
    int offset=(page-1)*size;
    List<ReportBoardDTO> list = reportBoardService.getReportBoardList(offset, size);
    int totalCount = reportBoardService.getTotalReportBoardCount();
    int totalPages = (int)Math.ceil((double)totalCount/size);
    Map<String, Object> result = Map.of(
            "content", list,
            "currentPages", page,
            "totalPages", totalPages);
    return ResponseEntity.ok(result);
  }
  @GetMapping("/{id}")
  public ResponseEntity<?> getReportBoardDetail(@PathVariable("id") Integer id) {
    ReportBoardDTO dto = reportBoardService.getReportBoardDetail(id);
    if (dto == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(Map.of("error", "해당 게시글이 존재하지 않습니다."));
    }
    return ResponseEntity.ok(dto);
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
  @PutMapping("/post/{id}")
  public ResponseEntity<Map<String, Object>> setReportBoard(@AuthenticationPrincipal AccountDetails accountDetails,
                                            @PathVariable("id") Integer id,
                                            @RequestBody ReportBoardDTO dto) {
    if(accountDetails.getUser() == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false,"message","로그인이 필요합니다"));
    }
    String loginEmail = accountDetails.getUser().getEmail();
    String reportBoardOwnerEmail = reportBoardService.getReportBoardOwnerEmail(id);
    if(!loginEmail.equals(reportBoardOwnerEmail)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
              .body(Map.of("updated",false,"message","본인의 게시글만 수정할 수 있습니다"));
    }
    dto.setReportBoardId(id);
    boolean result = reportBoardService.setReportBoard(dto, List.of());
    return ResponseEntity.ok(Map.of("success", result,"id", dto.getReportBoardId()));
  }
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteReportBoard(@AuthenticationPrincipal AccountDetails accountDetails,
                                             @PathVariable("id") Integer id) {
    if(accountDetails.getUser() == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false,"message","로그인이 필요합니다"));
    }
    String loginEmail = accountDetails.getUser().getEmail();
    String reportBoardOwnerEmail = reportBoardService.getReportBoardOwnerEmail(id);
    if (!loginEmail.equals(reportBoardOwnerEmail)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
              .body(Map.of("deleted", false, "message", "본인의 게시글만 삭제할 수 있습니다."));
    }
    boolean result = reportBoardService.removeReportBoard(id);
    return ResponseEntity.ok(Map.of("deleted", result));
  }
}
