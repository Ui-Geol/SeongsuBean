package com.oopsw.seongsubean.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.oopsw.seongsubean.account.dto.UserDTO;
import com.oopsw.seongsubean.account.repository.mybatisrepository.AccountRepository;
import com.oopsw.seongsubean.auth.AccountDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
@Log4j2
public class JwtBasicAuthenticationFilter extends BasicAuthenticationFilter {
    private final AccountRepository accountRepository;

    public JwtBasicAuthenticationFilter(AuthenticationManager authenticationManager,
        AccountRepository accountRepository) {
        super(authenticationManager);
        this.accountRepository = accountRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("doFilterInternal 요청됨: " + request.getRequestURI());
        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);
        if (jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        // 토큰 추출
        String token = jwtHeader.replace(JwtProperties.TOKEN_PREFIX, "");

        try {
            // JWT 디코딩 및 subject(email) 추출
            String email = JWT.require(Algorithm.HMAC512(JwtProperties.SECURET))
                    .build()
                    .verify(token)
                    .getSubject();
            log.info("검증된 이메일: " + email);
            if (email != null) {
                // 사용자 찾기
                UserDTO user = accountRepository.findByEmail(email);
                if (user != null) {
                    AccountDetails userDetails = new AccountDetails(user);
                    Authentication authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    // SecurityContext에 인증 정보 설정
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            log.warn(" JWT 인증 실패: " + e.getMessage());
        }
        chain.doFilter(request, response);
    }
}
