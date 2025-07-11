package com.oopsw.seongsubean.home.controller;

import com.oopsw.seongsubean.home.dto.CafeAddressDTO;
import com.oopsw.seongsubean.home.service.MainService;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CafeApiRestController {

  private final MainService mainService;

  public CafeApiRestController(MainService mainService) {
    this.mainService = mainService;
  }

  @GetMapping("/search-by-menu")
  public List<CafeAddressDTO> getEachMenu(@RequestParam String menuName) {
    return mainService.getSearchCafeMenu(menuName);
  }

  /** 평균별점 상위 5개 카페 주소와 ID 반환 */
  @GetMapping("/search/top5")
  public List<CafeAddressDTO> top5CafesByRating() {
    return mainService.getRanking().stream()
            .limit(5)
            .flatMap(r -> {
              List<CafeAddressDTO> cafeAddresses = mainService.getSearchCafeName(r.getCafeName());
              return cafeAddresses != null
                      ? cafeAddresses.stream()
                      : Stream.empty();
            })
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());
  }
}