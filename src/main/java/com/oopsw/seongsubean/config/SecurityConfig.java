package com.oopsw.seongsubean.config;

import com.oopsw.seongsubean.auth.AccountOauth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
      AccountOauth2UserService accountOauth2UserService) throws Exception {
    http.csrf(csrf -> csrf.disable());

    http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

    http.formLogin(form -> form
        .loginPage("/account/login")
        .loginProcessingUrl("/account/login")
        .failureUrl("/account/login?error")
        .defaultSuccessUrl("/", true)
        .usernameParameter("email")
        .permitAll()
    );
    http.logout(logout -> logout
        .logoutUrl("/account/logout")               // 로그아웃 요청 경로 (POST)
        .logoutSuccessUrl("/")         // 로그아웃 성공 시 리다이렉트, 홈 화면으로 바꾸기
        .invalidateHttpSession(true)                // 세션 무효화
        .clearAuthentication(true)                  // 인증 정보 제거
        .deleteCookies("JSESSIONID")                // 쿠키 제거
    );
    http.oauth2Login(oauth2 -> oauth2
        .loginPage("/account/login") // 사용자 정의 로그인 페이지
        .defaultSuccessUrl("/", true)
        .userInfoEndpoint(userInfo -> userInfo
            .userService(accountOauth2UserService) // 여기에서 Kakao, Google 모두 처리
        )
    );
    return http.build();
  }
}
