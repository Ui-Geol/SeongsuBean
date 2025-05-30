package com.oopsw.seongsubean.cafe.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.oopsw.seongsubean.cafe.domain.MenuInfo;
import com.oopsw.seongsubean.cafe.repository.jparepository.MenuInfoRepository;
import com.oopsw.seongsubean.config.PropertyConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PropertyConfig.class)
@Transactional
public class MenuInfoRespositoryTest {

  @Autowired
  private MenuInfoRepository menuInfoRepository;

  @Test
  public void addMenuInfoTest() {
    MenuInfo newMenuInfo = MenuInfo.builder()
        .menuCategory("커피")
        .menuName("씨쏠트커피")
        .image("/images/cafe/Cafe4.png")
        .price(4000)
        .description("씨솔트 커피는 짜요")
        .cafeId(6)
        .build();
    MenuInfo savedMenuInfo = menuInfoRepository.save(newMenuInfo);

    assertThat(savedMenuInfo).isNotNull();
    assertThat(savedMenuInfo.getMenuName()).isEqualTo(newMenuInfo.getMenuName());
  }

  @Test
  public void getMenuInfoTest() {
    Pageable pageable = PageRequest.of(0, 2); // 첫 번째 페이지, 사이즈 2
    Page<MenuInfo> result = menuInfoRepository.findByCafeId(41, pageable);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getSize()).isEqualTo(2);
  }

  @Test
  public void getMenuInfoByMenuIdTest() {
    MenuInfo menuInfo = menuInfoRepository.findByMenuId(60);

    assertThat(menuInfo).isNotNull();
    assertThat(menuInfo.getMenuName()).isEqualTo("아샷추");

  }

  @Test
  public void setMenuInfoTest() {
    MenuInfo existingMenu = menuInfoRepository.findById(40)
        .orElseThrow(() -> new RuntimeException("메뉴 없음"));

    existingMenu.setMenuName("아이스 아메리카노");
    existingMenu.setPrice(30);

    MenuInfo updatedMenuInfo = menuInfoRepository.save(existingMenu);

    assertThat(updatedMenuInfo.getMenuName()).isEqualTo("아이스 아메리카노");
  }

  @Test
  public void deleteMenuInfoTest() {
    menuInfoRepository.deleteById(40);

    assertThat(menuInfoRepository.findById(40)).isNotPresent();
  }


}
