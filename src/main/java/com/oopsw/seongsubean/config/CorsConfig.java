package com.oopsw.seongsubean.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); //json 데이터 자바 스크립트에서 처리
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*"); //
        config.addAllowedMethod("*");  //GET, POST, ...
        config.setExposedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Authorization"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
