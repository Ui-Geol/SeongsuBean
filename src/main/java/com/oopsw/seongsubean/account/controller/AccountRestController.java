package com.oopsw.seongsubean.account.controller;

import com.oopsw.seongsubean.account.dto.UserDTO;
import com.oopsw.seongsubean.account.service.AccountService;
import com.oopsw.seongsubean.auth.AccountDetails;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountRestController {

  private final AccountService accountService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @GetMapping("/email")
  public String getEmail(Authentication auth) {
    AccountDetails details = (AccountDetails) auth.getPrincipal();
    return details.getUsername();
  }

  @PostMapping
  public ResponseEntity<Map<String, String>> joinAccount(@RequestBody UserDTO user) {
    accountService.addUser(user);
    return ResponseEntity.ok().body(Map.of("message", "회원가입에 성공 하셨습니다."));
  }

  @DeleteMapping
  public Map<String, Boolean> deleteAccount(Authentication auth) {
    AccountDetails user = (AccountDetails) auth.getPrincipal();
    return Map.of("result", accountService.removeUser(user.getUsername()));
  }

  @PostMapping("/exist/email")
  public Map<String, Boolean> checkEmail(@RequestBody UserDTO user) {
    return Map.of("result", accountService.existsEmail(user.getEmail()));
  }

  @PostMapping("/exist/nickname")
  public Map<String, Boolean> checkNickname(@RequestBody UserDTO user) {
    return Map.of("result", accountService.existsNickName(user.getNickName()));
  }

  @GetMapping("/profile")
  public UserDTO myPage(Authentication auth) {
    AccountDetails user = (AccountDetails) auth.getPrincipal();
    return user.getUser();
  }

  @PutMapping("/profile")
  public ResponseEntity<Map<String, String>> editProfile(@RequestBody UserDTO user,
      Authentication auth) {
    AccountDetails userDetails = (AccountDetails) auth.getPrincipal();
    user.setEmail(userDetails.getUsername());
    if (user.getNewPassword() != null && !user.getNewPassword().isBlank()) {
      user.setNewPassword(bCryptPasswordEncoder.encode(user.getNewPassword()));
    }
    if (user.getNewNickName() != null && !user.getNewNickName().isBlank()) {
      user.setNickName(user.getNewNickName());
    }
    accountService.setUserInfo(user);
    return ResponseEntity.ok(Map.of("message", "정보를 수정하였습니다."));
  }

  @PostMapping("/profile")
  public ResponseEntity<Map<String, Boolean>> checkPw(Authentication auth,
      @RequestBody UserDTO userDTO) {
    AccountDetails accountDetails = (AccountDetails) auth.getPrincipal();
    return ResponseEntity.ok(Map.of(
        "result", bCryptPasswordEncoder.matches(
            userDTO.getPassword(), accountDetails.getUser().getPassword())));
  }

  @PutMapping("/profile/image")
  public ResponseEntity<Map<String, String>> setImage(@RequestBody Map<String, String> image, Authentication auth) {
    AccountDetails accountDetails = (AccountDetails) auth.getPrincipal();
    UserDTO userDTO = accountDetails.getUser();

    String fileName = image.get("image");
    if (fileName == null || fileName.trim().isEmpty()) {
      return ResponseEntity.badRequest().body(Map.of("message", "파일명이 없습니다."));
    }

    // 파일명 저장
    userDTO.setImage(fileName);
    accountService.setImage(userDTO);

    return ResponseEntity.ok(Map.of("message", "업로드 성공"));
  }


  @GetMapping("/profile/posts")
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

  @GetMapping("/profile/reviews")
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

  @GetMapping("/profile/cafes")
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

    return Map.of("cafes", cafes, "currentPage", page,
        "totalPages", totalPages, "totalCount", totalCount);
  }
}
