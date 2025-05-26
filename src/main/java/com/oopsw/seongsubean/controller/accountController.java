package com.oopsw.seongsubean.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class accountController {
  @GetMapping("/login")
  public String login() {
    return "account/login-view";
  }
  @GetMapping("/join")
  public String join() {
    return "account/join-view";
  }
  @GetMapping("/myPage")
  public String myPage() {
    return "account/my-page";
  }
  @GetMapping("/editProfile")
  public String editProfile() {
    return "account/edit-profile";
  }
  @GetMapping("/checkPw")
  public String checkPw() {
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
