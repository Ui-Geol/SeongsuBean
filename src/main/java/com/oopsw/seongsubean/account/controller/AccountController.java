package com.oopsw.seongsubean.account.controller;

import com.oopsw.seongsubean.account.dto.UserDTO;
import com.oopsw.seongsubean.account.service.AccountService;
import com.oopsw.seongsubean.auth.AccountDetails;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
  private final AccountService accountService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @GetMapping("/login")
  public String login() {
    return "account/login-view";
  }

  @GetMapping("/join")
  public String join() {
    return "account/join-view";
  }

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

  @GetMapping("/editProfile")
  public String editProfile(@AuthenticationPrincipal AccountDetails accountDetails, Model model) {
    UserDTO user = accountDetails.getUser();
    //OAuth2로 로그인 한 유저인지 확인
    boolean isOAuth2 = user.isOauth();
    model.addAttribute("isOAuth2User", isOAuth2);
    model.addAttribute("user", user);
    return "account/edit-profile";
  }

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

  @GetMapping("/checkPw")
  public String checkPw(@AuthenticationPrincipal AccountDetails accountDetails, Model model) {
    UserDTO user = accountDetails.getUser();
    //OAuth2로 로그인 한 유저인지 확인
    boolean isOAuth2 = user.isOauth();
    model.addAttribute("isOAuth2User", isOAuth2);
    model.addAttribute("user", user);
    if(isOAuth2) {
      return "redirect:/account/editProfile";
    }
    return "account/check-pw";
  }

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
}
