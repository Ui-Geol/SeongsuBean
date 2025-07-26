package com.oopsw.seongsubean.home.controller;

import com.oopsw.seongsubean.home.dto.CafeAddressDTO;
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
  public List<CafeAddressDTO> searchByKeyword(@RequestParam String keyword) {
    List<CafeAddressDTO> results = new ArrayList<>();

    // 1) 카페명으로 일치하는 카페들의 주소와 ID 조회
    List<CafeAddressDTO> cafeList = mainService.getSearchCafeName(keyword);
    if (cafeList != null && !cafeList.isEmpty()) {
      results.addAll(cafeList);
    }

    // 2) 메뉴명(키워드)으로 특정메뉴 판매 카페들의 주소와 ID 조회
    List<CafeAddressDTO> menuCafeList = mainService.getEachMenu(keyword);
    if (menuCafeList != null && !menuCafeList.isEmpty()) {
      results.addAll(menuCafeList);
    }

    return results;
  }
}