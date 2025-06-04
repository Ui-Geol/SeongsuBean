package com.oopsw.seongsubean.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class MainController {
  /*                             홈화면 테스트 */
  @GetMapping("/")
  public String map() {
    return "home/map";
  }

  @GetMapping("/free-list")
  public String free() {
    return "board/free-list";
  }
  @GetMapping("/report-list")
  public String report() {
    return "board/report-list";
  }
  @GetMapping("/free-detail")
  public String freeDetail() {
    return "board/free-detail";
  }
  @GetMapping("/report-detail")
  public String reportDetail() {
    return "board/report-detail";
  }
  @GetMapping("/free-post")
  public String freePost() {
    return "board/free-post";
  }
  @GetMapping("/report-post")
  public String reportPost() {
    return "board/report-post";
  }
  /*                                  카페 테스트 */
  @GetMapping("/cafe-detail")
  public String cafeDetail() {
    return "cafe/cafe-detail";
  }
  @GetMapping("/review-form")
  public String reviewForm() {
    return "cafe/review-form";
  }
  @GetMapping("/add-menu")
  public String addMenu() {
    return "cafe/add-menu";
  }
  @GetMapping("/cafe-registration")
  public String registerCafe() {
    return "cafe/cafe-registration";
  }
  /*                                   마이페이지 테스트 */
  @GetMapping("loginView")
  public String loginView() {
    return "account/login-view";
  }
  @GetMapping("/join-view")
  public String joinView() {
    return "account/join-view";
  }
  @GetMapping("/my-page")
  public String myPage() {
    return "account/my-page";
  }
  @GetMapping("/edit-profile")
  public String editProfile() {
    return "account/edit-profile";
  }
  @GetMapping("/check-pw")
  public String checkPw() {
    return "account/check-pw";
  }
  @GetMapping("/my-cafe")
  public String myCafe() {
    return "account/my-cafe";
  }
  @GetMapping("/my-post")
  public String myPost() {
    return "account/my-post";
  }
  @GetMapping("/my-review")
  public String myReview() {
    return "account/my-review";
  }
}

