package com.oopsw.seongsubean.jwt.controller;

import com.oopsw.seongsubean.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/jwt")
public class JwtRestController {

  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping("/validate-token")
  public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token) {

    if (jwtTokenProvider.isTokenExpired(token)) {
      return ResponseEntity.ok("만료");
    } else {
      return ResponseEntity.ok("유효");
    }
  }


}
