package com.oopsw.seongsubean.account.controller;

import com.oopsw.seongsubean.account.dto.UserDTO;
import com.oopsw.seongsubean.account.service.AccountService;
import com.oopsw.seongsubean.auth.AccountDetails;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountRestController {
  private final AccountService accountService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @GetMapping("/getEmail")
  public String getEmail(Authentication auth) {
    AccountDetails details = (AccountDetails) auth.getPrincipal();
    return details.getUsername();
  }

  @PostMapping("/join")
  public ResponseEntity<Map<String, String>> joinAction(@RequestBody UserDTO user) {
    accountService.addUser(user);
    return ResponseEntity.ok().body(Map.of("message", "회원가입에 성공 하셨습니다."));
  }

  @PostMapping("/checkPw")
  public ResponseEntity<Map<String, Boolean>> checkPwAction(Authentication auth ,@RequestBody UserDTO userDTO) {
    AccountDetails accountDetails = (AccountDetails) auth.getPrincipal();
    return ResponseEntity.ok(Map.of(
        "result", bCryptPasswordEncoder.matches(
            userDTO.getPassword(), accountDetails.getUser().getPassword())));
  }

  @GetMapping("/myPage")
  public UserDTO myPage(Authentication auth) {
    AccountDetails user = (AccountDetails) auth.getPrincipal();
    return user.getUser();
  }

  @GetMapping("/editProfile")
  public UserDTO editProfile(Authentication auth) {
    AccountDetails user = (AccountDetails) auth.getPrincipal(); //위 코드와 중복돼서 함수화 필요
    return user.getUser();
  }

  @PostMapping("/editProfile")
  public ResponseEntity<Map<String, String>> editProfileAction(@RequestBody UserDTO user,Authentication auth) {
    AccountDetails userDetails = (AccountDetails) auth.getPrincipal();
    user.setEmail(userDetails.getUsername());
    if (user.getNewPassword() != null && !user.getNewPassword().isBlank()) {
      user.setNewPassword(bCryptPasswordEncoder.encode(user.getNewPassword()));
    }
    if (user.getNewNickName() != null && !user.getNewNickName().isBlank()) {
      user.setNickName(user.getNewNickName());
    }
    accountService.setUserInfo(user);
    return ResponseEntity.ok(Map.of("message","정보를 수정하였습니다."));
  }

  @GetMapping("/myPost")
  public Map<String, Object> getMyPosts(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size,
          Authentication auth) {

    UserDetails user = (UserDetails) auth.getPrincipal();
    String email = user.getUsername();
    int offset = (page - 1) * size;

    // 페이징용 RowBounds 객체 사용
    RowBounds rowBounds = new RowBounds(offset, size);
    List<Map<String, Object>> posts = accountService.getMyBoards(email, rowBounds);

    // 총 게시글 수
    int totalCount = accountService.countMyBoards(email);
    int totalPages = (int) Math.ceil((double) totalCount / size);

    return Map.of("posts", posts, "currentPage", page,
        "totalPages", totalPages, "totalCount", totalCount);
  }

  @GetMapping("/myReview")
  public Map<String, Object> getMyReviews(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size,
      Authentication auth) {

    UserDetails user = (UserDetails) auth.getPrincipal();
    String email = user.getUsername();
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

    return Map.of("posts", reviews, "currentPage", page,
        "totalPages", totalPages, "totalCount", totalCount);
  }

  @GetMapping("/myCafe")
  public Map<String, Object> getMyCafes(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "4") int size,
      Authentication auth) {

    UserDetails user = (UserDetails) auth.getPrincipal();
    String email = user.getUsername();
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

    return Map.of("posts", cafes, "currentPage", page,
        "totalPages", totalPages, "totalCount", totalCount);
  }

  // 이메일 중복 체크
  @PostMapping("/checkEmail")
  public Map<String, Boolean> checkEmail(@RequestBody UserDTO user) {
    return Map.of("result", accountService.existsEmail(user.getEmail()));
  }

  // 닉네임 중복 체크
  @PostMapping("/checkNickname")
  public Map<String, Boolean> checkNickname(@RequestBody UserDTO user) {
    return Map.of("result", accountService.existsNickName(user.getNickName()));
  }

  @DeleteMapping("/deleteAccount")
  public Map<String, Boolean> deleteAccount(Authentication auth) {
    AccountDetails user = (AccountDetails) auth.getPrincipal();
    return Map.of("result",accountService.removeUser(user.getUsername()));
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
