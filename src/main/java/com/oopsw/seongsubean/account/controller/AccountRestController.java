package com.oopsw.seongsubean.account.controller;

import com.oopsw.seongsubean.account.service.AccountService;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountRestController {
  private final AccountService accountService;

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
}
