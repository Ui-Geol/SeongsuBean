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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountRestController {
  private final AccountService accountService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @PostMapping("/join")
  public String joinAction(@ModelAttribute UserDTO form, Model model) {
    accountService.addUser(form);
    model.addAttribute("joinSuccess", true); // 메시지 전달
    return "account/login-view";
  }

  @GetMapping("/myPage")
  public String myPage(@AuthenticationPrincipal AccountDetails accountDetails, Model model) {
    String email = accountDetails.getUsername();
    UserDTO user = accountService.findByEmail(email);
    model.addAttribute("user", user);

    return "account/my-page";
  }

//  @GetMapping("/editProfile")
//  public String editProfile(@AuthenticationPrincipal AccountDetails accountDetails, Model model) {
//    UserDTO user = accountDetails.getUser();
//    //OAuth2로 로그인 한 유저인지 확인
//    boolean isOAuth2 = user.isOauth();
//    model.addAttribute("isOAuth2User", isOAuth2);
//    model.addAttribute("user", user);
//    return "account/edit-profile";
//  }

  @PostMapping("/editProfile")
  public String editProfileAction(@ModelAttribute UserDTO form, Model model) {
    if (form.getNewPassword() != null && !form.getNewPassword().isEmpty()) {
      // 비밀번호 암호화
      String encoded = bCryptPasswordEncoder.encode(form.getNewPassword());
      form.setNewPassword(encoded);
    }
    accountService.setUserInfo(form);
    UserDTO updatedUser = accountService.findByEmail(form.getEmail());
    model.addAttribute("user", updatedUser);
    model.addAttribute("editSuccess", true);
    return "account/my-page";
  }

//  @GetMapping("/checkPw")
//  public String checkPw(@AuthenticationPrincipal AccountDetails accountDetails, Model model) {
//    UserDTO user = accountDetails.getUser();
//    //OAuth2로 로그인 한 유저인지 확인
//    boolean isOAuth2 = user.isOauth();
//    model.addAttribute("isOAuth2User", isOAuth2);
//    model.addAttribute("user", user);
//    if(isOAuth2) {
//      return "redirect:/account/editProfile";
//    }
//    return "account/check-pw";
//  }

  @GetMapping("/myPost")
  public String getMyPosts(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size,
          Principal principal,
          Model model) {

    String email = principal.getName();
    int offset = (page - 1) * size;

    // 페이징용 RowBounds 객체 사용
    RowBounds rowBounds = new RowBounds(offset, size);
    List<Map<String, Object>> posts = accountService.getMyBoards(email, rowBounds);

    // 총 게시글 수
    int totalCount = accountService.countMyBoards(email);
    int totalPages = (int) Math.ceil((double) totalCount / size);

    model.addAttribute("posts", posts);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("totalCount", totalCount);
    return "account/my-post";
  }

  @GetMapping("/myReview")
  public String myReview(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size,
          Principal principal,
          Model model) {

    String email = principal.getName();
    int offset = (page - 1) * size;

    // 페이징용 RowBounds 객체 사용
    RowBounds rowBounds = new RowBounds(offset, size);
    List<Map<String, Object>> reviews = accountService.getMyReviews(email, rowBounds);

    // 총 리뷰 수
    int totalCount = accountService.countMyReviews(email);
    int totalPages = (int) Math.ceil((double) totalCount / size);
    if (totalPages < 1) {
      totalPages = 1;
    }

    model.addAttribute("reviews", reviews);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("totalCount", totalCount);
    return "account/my-review";
  }

  @GetMapping("/myCafe")
  public String myCafe(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "4") int size,
          Principal principal,
          Model model) {

    String email = principal.getName();
    int offset = (page - 1) * size;

    // 페이징용 RowBounds 객체 사용
    RowBounds rowBounds = new RowBounds(offset, size);
    List<Map<String, Object>> cafes = accountService.getMyCafes(email, rowBounds);

    // 총 리뷰 수
    int totalCount = accountService.countMyCafes(email);
    int totalPages = (int) Math.ceil((double) totalCount / size);
    if (totalPages < 1) {
      totalPages = 1;
    }

    model.addAttribute("cafes", cafes);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("totalCount", totalCount);
    return "account/my-cafe";
  }

  // 이메일 중복 체크
  @GetMapping("/api/checkEmail")
  public Map<String, Boolean> checkEmail(@RequestParam String email) {
    boolean exists = accountService.existsEmail(email);
    return Collections.singletonMap("exists", exists);
  }

  // 닉네임 중복 체크
  @GetMapping("/api/checkNickname")
  public Map<String, Boolean> checkNickname(@RequestParam String nickname) {
    boolean exists = accountService.existsNickName(nickname);
    return Collections.singletonMap("exists", exists);
  }

  // 비밀번호 체크
  @PostMapping("/api/checkPw")
  public ResponseEntity<?> checkPw(@RequestBody Map<String, String> body,@AuthenticationPrincipal AccountDetails accountDetails) {
    String inputPassword = body.get("password");
    String password = accountDetails.getUser().getPassword();
    return ResponseEntity.ok(Map.of("success", bCryptPasswordEncoder.matches(inputPassword, password)));
  }

  @DeleteMapping("/api/deleteAccount")
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

  @PutMapping("/api/uploadImage")
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
