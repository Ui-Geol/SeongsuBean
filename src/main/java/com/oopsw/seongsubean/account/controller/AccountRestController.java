package com.oopsw.seongsubean.account.controller;

import com.oopsw.seongsubean.account.dto.UserDTO;
import com.oopsw.seongsubean.account.service.AccountService;
import com.oopsw.seongsubean.auth.AccountDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountRestController {
  private final AccountService accountService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  // 이메일 중복 체크
  @GetMapping("/checkEmail")
  public Map<String, Boolean> checkEmail(@RequestParam String email) {
    boolean exists = accountService.existsEmail(email);
    return Collections.singletonMap("exists", exists);
  }

  // 닉네임 중복 체크
  @GetMapping("/checkNickname")
  public Map<String, Boolean> checkNickname(@RequestParam String nickname) {
    boolean exists = accountService.existsNickName(nickname);
    return Collections.singletonMap("exists", exists);
  }

  // 비밀번호 체크
  @PostMapping("/checkPw")
  public ResponseEntity<?> checkPw(@RequestBody Map<String, String> body,@AuthenticationPrincipal AccountDetails accountDetails) {
    String inputPassword = body.get("password");
    String password = accountDetails.getUser().getPassword();
    return ResponseEntity.ok(Map.of("success", bCryptPasswordEncoder.matches(inputPassword, password)));
  }

  @DeleteMapping("/deleteAccount")
  public ResponseEntity<?> deleteAccount(
      @AuthenticationPrincipal AccountDetails accountDetails,
      HttpServletRequest request,
      HttpServletResponse response) {
    try {
      String email = accountDetails.getUser().getEmail();
      accountService.removeUser(email);

      // 인증 로그아웃 처리
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth != null) {
        new SecurityContextLogoutHandler().logout(request, response, auth);
      }

      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("삭제 실패: " + e.getMessage());
    }
  }

  @PutMapping("/uploadImage")
  public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
      Principal principal) throws IOException {
    if (file.isEmpty()) {
      return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
    }

    String email = principal.getName();
    UserDTO user = accountService.findByEmail(email);

    // 1. 절대 경로로 수정
    String uploadDir = new File("src/main/resources/static/images/account/").getAbsolutePath();

    String originalFilename = Paths.get(file.getOriginalFilename()).getFileName().toString();
    String safeFilename = originalFilename.replaceAll("\\s+", "_");

    String newFilename = UUID.randomUUID() + "_" + safeFilename;
    Path uploadPath = Paths.get(uploadDir);

    // 2. 저장 경로가 없다면 생성
    try {
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("디렉토리 생성 실패: " + e.getMessage());
    }

    // 3. 기존 이미지 삭제
    String oldImage = user.getImage();
    if (oldImage != null && !oldImage.equals("default.png")) {
      Path oldPath = uploadPath.resolve(oldImage);
      Files.deleteIfExists(oldPath);
    }

    // 4. 새 이미지 저장
    Path filePath = uploadPath.resolve(newFilename);
    file.transferTo(filePath.toFile());

    // 5. DB 업데이트
    user.setImage(newFilename);
    accountService.setImage(user);

    return ResponseEntity.ok("업로드 성공");
  }


}
