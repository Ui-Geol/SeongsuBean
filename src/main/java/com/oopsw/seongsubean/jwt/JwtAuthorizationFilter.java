package com.oopsw.seongsubean.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oopsw.seongsubean.account.dto.UserDTO;
import com.oopsw.seongsubean.auth.AccountDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class JwtAuthorizationFilter
        extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @Override // /login
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        log.info("attemptAuthentication = 로그인 시도");
        //1.로그인할 정보 추출
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            UserDTO u=objectMapper.readValue(request.getInputStream(), UserDTO.class);
            log.info(u.getEmail()+" - "+u.getPassword());
            //2.로그인 시도 - form X
            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(u.getEmail(), u.getPassword());
            Authentication a=authenticationManager.authenticate(authRequest);
            AccountDetails details=(AccountDetails) a.getPrincipal();
            log.info(details.getUser().getEmail()+" - loginok");
            return a;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override //ok
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공");
        //3.JWT 작성
        AccountDetails details=(AccountDetails) authResult.getPrincipal();
        String jwtToken = JWT.create()
                .withSubject(details.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.TIMEOUT))
                .withClaim("id", details.getUser().getEmail())
                .withClaim("username", details.getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECURET))
                ;
        log.info(jwtToken);
        //4. 웹브라우저에 전달
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
        response.getWriter().println(Map.of("message", "loginOK"));
    }
}
