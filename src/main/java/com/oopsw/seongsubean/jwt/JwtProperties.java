package com.oopsw.seongsubean.jwt;

public interface JwtProperties {
    String SECURET = "oopsw";
    int TIMEOUT = 60*60 * 1000; //60 * 60 * 24 * 1000
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
