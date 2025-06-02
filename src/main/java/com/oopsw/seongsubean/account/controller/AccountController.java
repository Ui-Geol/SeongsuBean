package com.oopsw.seongsubean.account.controller;

import com.oopsw.seongsubean.account.dto.UserDTO;
import com.oopsw.seongsubean.account.service.AccountService;
import com.oopsw.seongsubean.auth.AccountDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
  private final AccountService accountService;

  @GetMapping("/login")
  public String login() {
    return "account/login-view";
  }

//  @PostMapping("/login")
//  public String getUser(@AuthenticationPrincipal AccountDetails user) {
//    UserDTO userdto = user.getUser();
//    session
//    return "account/login-view";
//  }

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
    UserDTO user = accountDetails.getUser();
    model.addAttribute("user", user);
    return "account/my-page";
  }

  @GetMapping("/editProfile")
  public String editProfile(@AuthenticationPrincipal AccountDetails accountDetails, Model model) {
    UserDTO user = accountDetails.getUser();
    model.addAttribute("user", user);
    return "account/edit-profile";
  }

  @GetMapping("/checkPw")
  public String checkPw(@AuthenticationPrincipal AccountDetails accountDetails, Model model) {
    UserDTO user = accountDetails.getUser();
    model.addAttribute("user", user);
    return "account/check-pw";
  }

  @GetMapping("/myPost")
  public String myPost() {
    return "account/my-post";
  }

  @GetMapping("/myReview")
  public String myReview() {
    return "account/my-review";
  }

  @GetMapping("/myCafe")
  public String myCafe() {
    return "account/my-cafe";
  }
}
