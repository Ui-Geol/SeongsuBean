package com.oopsw.seongsubean.cafe.service;

import com.oopsw.seongsubean.cafe.domain.MenuInfo;
import com.oopsw.seongsubean.cafe.repository.jparepository.MenuInfoRepository;
import java.awt.Menu;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {
  private final MenuInfoRepository menuInfoRepository;
  //메뉴 생성
  public boolean addMenu(MenuInfo menuInfo) {
    MenuInfo newMenuInfo = menuInfoRepository.save(menuInfo);

    return newMenuInfo == menuInfo;
  }
  //메뉴 조회
  public Page<MenuInfo> getMenuList(Integer cafeId, Pageable pageable) {
     return menuInfoRepository.findByCafeId(cafeId, pageable);
  }
  //메뉴 수정
  public boolean updateMenu(Integer menuId, MenuInfo newMenuInfo) {
    Optional<MenuInfo> menuInfo = menuInfoRepository.findById(menuId);
    menuInfo.get().setMenuName(newMenuInfo.getMenuName());
    menuInfo.get().setMenuCategory(newMenuInfo.getMenuCategory());
    menuInfo.get().setPrice(newMenuInfo.getPrice());
    menuInfo.get().setImage(newMenuInfo.getImage());
    menuInfo.get().setDescription(newMenuInfo.getDescription());
    menuInfo.ifPresent(menuInfoRepository::save);

    Optional<MenuInfo> updatedMenuInfo = menuInfoRepository.findById(menuId);

    return updatedMenuInfo.get().equals(newMenuInfo);
  }
  //메뉴 삭제
  public boolean deleteMenu(Integer menuId) {
    menuInfoRepository.deleteById(menuId);
    Optional<MenuInfo> menuInfo = menuInfoRepository.findById(menuId);
    return menuInfo.isPresent();
  }

}
