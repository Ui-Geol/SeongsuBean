package com.oopsw.seongsubean.cafe.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.oopsw.seongsubean.cafe.domain.MenuInfo;
import com.oopsw.seongsubean.cafe.dto.MenuDTO;
import com.oopsw.seongsubean.cafe.repository.jparepository.MenuInfoRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@Transactional
public class MenuServiceTest {

  @Autowired
  private MenuService menuService;

  @Autowired
  private MenuInfoRepository menuInfoRepository;

  //메뉴 생성
  @Test
  public void addMenuTest() {
    MenuDTO newMenuDTO = MenuDTO.builder()
        .menuCategory("커피")
        .menuName("씨쏠트커피")
        .image("/images/cafe/Cafe4.png")
        .price(4000)
        .description("씨솔트 커피는 짜요")
        .cafeId(6)
        .build();

    boolean result = menuService.addMenu(newMenuDTO);

    assertThat(result).isTrue();
  }

  //메뉴 조회
  @Test
  public void getMenuListTest() {
    Pageable pageable = PageRequest.of(0, 2); // 첫 번째 페이지, 사이즈 2

    List<MenuDTO> menuInfoList = menuService.getMenuList(35, pageable);

    assertThat(menuInfoList.size()).isEqualTo(2);
  }

  //메뉴 수정
  @Test
  public void setMenuTest() {
    MenuInfo newMenuInfo = MenuInfo.builder()
        .menuCategory("커피")
        .menuName("씨쏠트커피")
        .image("/images/cafe/Cafe4.png")
        .price(4000)
        .description("씨솔트 커피는 짜요")
        .cafeId(6)
        .build();

    assertThat(menuService.setMenu(3, newMenuInfo)).isTrue();
  }

  //메뉴 삭제
  @Test
  public void removeMenuTest() {
    menuService.removeMenu(3);

    Optional<MenuInfo> menuInfo = menuInfoRepository.findById(3);
    assertThat(menuInfo.isPresent()).isFalse();
  }


}
