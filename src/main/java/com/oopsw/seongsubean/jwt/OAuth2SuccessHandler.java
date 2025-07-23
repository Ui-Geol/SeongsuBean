package com.oopsw.seongsubean.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.oopsw.seongsubean.auth.AccountDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    AccountDetails details = (AccountDetails) authentication.getPrincipal();

    String jwtToken = JWT.create()
        .withSubject(details.getUsername())
        .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.TIMEOUT))
        .withClaim("id", details.getUser().getEmail())
        .withClaim("username", details.getUsername())
        .sign(Algorithm.HMAC512(JwtProperties.SECURET))
        ;
    log.info(jwtToken);
    //4. 웹브라우저에 전달
    response.sendRedirect("http://localhost:8880/?token=" + jwtToken);

  }
}