package com.oopsw.seongsubean.cafe.controller;

import com.oopsw.seongsubean.cafe.dto.MenuDTO;
import com.oopsw.seongsubean.cafe.service.MenuService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafe/{cafeId}/menu")
public class CafeMenuRestController {

  private final MenuService menuService;

  @GetMapping
  public ResponseEntity<Map<String, Object>> getMenu(@PathVariable("cafeId") Integer cafeId) {
    Pageable pageable = PageRequest.of(0, 100);
    List<MenuDTO> menuDTOList = menuService.getMenuList(cafeId, pageable);

    return ResponseEntity.ok(Map.of("menuDTOList", menuDTOList));
  }

}
