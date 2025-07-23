package com.oopsw.seongsubean.cafe.controller;

import com.oopsw.seongsubean.cafe.dto.MenuDTO;
import com.oopsw.seongsubean.cafe.service.MenuService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafe/{cafeId}/menu")
public class CafeMenuRestController {

  private final MenuService menuService;

  @PostMapping
  public ResponseEntity<String> addMenu(@PathVariable Integer cafeId,
      @RequestBody MenuDTO menuDTO) {

    menuDTO.setImage("/images/menu/" + menuDTO.getImage());
    menuDTO.setCafeId(cafeId);

    try {
      menuService.addMenu(menuDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body("메뉴 생성이 성공하였습니다");
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메뉴 생성이 실패하였습니다");
    }
  }

  @GetMapping
  public ResponseEntity<Map<String, Object>> getMenus(@PathVariable("cafeId") Integer cafeId) {
    Pageable pageable = PageRequest.of(0, 100);
    List<MenuDTO> menuDTOList = menuService.getMenuList(cafeId, pageable);

    return ResponseEntity.ok(Map.of("menuDTOList", menuDTOList));
  }

  @GetMapping("/{menuId}")
  public ResponseEntity<Map<String, Object>> getMenu(@PathVariable("menuId") Integer menuId) {
    MenuDTO menuDto = menuService.getMenu(menuId);

    return ResponseEntity.ok(Map.of("menuDto", menuDto));
  }

  @PutMapping
  public ResponseEntity<String> setMenu(@Valid @RequestBody MenuDTO menuDto) {

    try {
      menuService.setMenu(menuDto);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카페 업데이트를 실패하였습니다");
    }
  }

  @DeleteMapping("/{menuId}")
  public ResponseEntity<String> removeCafe(
      @PathVariable Integer menuId) {
    try {
      menuService.removeMenu(menuId);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메뉴 삭제를 실패하였습니다");
    }
  }

}
