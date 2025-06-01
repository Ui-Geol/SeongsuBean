package com.oopsw.seongsubean.board.controller;

import com.oopsw.seongsubean.board.dto.ReportBoardDTO;
import com.oopsw.seongsubean.board.service.ReportBoardService;

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
          @RequestParam String title,
          @RequestParam String content,
          @RequestParam String email,
          @RequestParam(required = false) List<MultipartFile> images) {

    ReportBoardDTO dto = ReportBoardDTO.builder()
            .title(title)
            .content(content)
            .email(email)
            .build();
    List<String> fileNames = new ArrayList<>();
    if (images != null) {
      for (MultipartFile file : images) {
        if (!file.isEmpty()) {
          String originalFilename = file.getOriginalFilename();
          String uploadDir = "/path/to/static/images/upload/report/" + email; // 임시 경로 (ID 아직 없음)
          File dir = new File(uploadDir);
          if (!dir.exists()) dir.mkdirs();

          Path filePath = Paths.get(uploadDir, originalFilename);
          try {
            Files.copy(file.getInputStream(), filePath);
            fileNames.add(originalFilename);
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
  public ResponseEntity<List<ReportBoardDTO>> getReportBoardList() {
    HttpHeaders headers = new HttpHeaders();
    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
    List<ReportBoardDTO> list = reportBoardService.getReportBoardList();
    return ResponseEntity.ok(list);
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
  @PutMapping("/post/{id}")
  public Map<String, Object> setReportBoard(@PathVariable("id") Integer id,
      @RequestBody ReportBoardDTO dto) {
    dto.setReportBoardId(id);
    boolean result = reportBoardService.setReportBoard(dto, List.of());
    return Map.of("updated", result);
  }
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteReportBoard(@PathVariable("id") Integer id) {
    boolean result = reportBoardService.removeReportBoard(id);
    return ResponseEntity.ok(Map.of("deleted", result));
  }
}
