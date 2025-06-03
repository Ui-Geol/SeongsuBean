package com.oopsw.seongsubean.home.controller;

import com.oopsw.seongsubean.home.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchRestController {

  private final MainService mainService;
  @GetMapping("/api/search")
  public List<String> searchByKeyword(@RequestParam String keyword) {
    List<String> results = new ArrayList<>();

    // 1) 카페명으로 정확히 일치하는 주소 조회
    List<String> cafeAddr = mainService.getSearchCafeName(keyword);
    if (cafeAddr != null && !cafeAddr.isEmpty()) {
      results.addAll(cafeAddr);
    }

    // 2) 메뉴명(키워드)으로 특정메뉴 판매 카페들의 주소 조회
    List<String> menuAddrs = mainService.getEachMenu(keyword);
    if (menuAddrs != null && !menuAddrs.isEmpty()) {
      results.addAll(menuAddrs);
    }

    return results;
  }
}