package com.oopsw.seongsubean.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  public boolean isTokenExpired(String token) {
    if (token == null || !token.startsWith(JwtProperties.TOKEN_PREFIX)) {
      return false;
    }
    // 토큰 추출
    String pureToken = token.replace(JwtProperties.TOKEN_PREFIX, "");

    try {
      Date expiredDate = JWT.require(Algorithm.HMAC512(JwtProperties.SECURET))
          .build()
          .verify(pureToken).getExpiresAt();

      return expiredDate.before(new Date());
    } catch (ExpiredJwtException e) {
      return true;
    } catch (Exception e) {
      return true;
    }
  }

}
