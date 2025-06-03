package com.oopsw.seongsubean.home.controller;

import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import com.oopsw.seongsubean.home.service.MainService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/main")
public class MainCardRestController {

  private final MainService mainService;

  public MainCardRestController(MainService mainService) {
    this.mainService = mainService;
  }

  /** GET  /api/main/cards → getMainCardView() 호출 */
  @GetMapping("/cards")
  public List<CafeDTO> getMainCardView() {
    return mainService.getMainCardView();
  }
}