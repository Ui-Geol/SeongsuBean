package com.oopsw.seongsubean.home.controller;

import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import com.oopsw.seongsubean.home.service.MainService;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CafeApiController {

  private final MainService mainService;

  public CafeApiController(MainService mainService) {
    this.mainService = mainService;
  }

  @GetMapping("/search-by-menu")
  public List<String> getEachMenu(@RequestParam String menuName) {
    return mainService.getSearchCafeMenu(menuName);
  }
  /** 평균별점 상위 5개 카페 주소 반환 */
  @GetMapping("/search/top5")
  public List<String> top5CafesByRating() {
    return mainService.getRanking().stream()
        .limit(5)
        .flatMap(r -> {
          List<String> names = mainService.getSearchCafeName(r.getCafeName());
          return names != null
              ? names.stream()
              : Stream.empty();
        })
        .filter(Objects::nonNull)
        .distinct()
        .collect(Collectors.toList());
  }
}
