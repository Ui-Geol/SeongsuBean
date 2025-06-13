package com.oopsw.seongsubean.cafe.service;

import com.oopsw.seongsubean.cafe.domain.MenuInfo;
import com.oopsw.seongsubean.cafe.dto.MenuDTO;
import com.oopsw.seongsubean.cafe.repository.jparepository.MenuInfoRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

  private final MenuInfoRepository menuInfoRepository;

  //메뉴 생성
  public boolean addMenu(MenuDTO menuDTO) {
    MenuInfo newMenuInfo = menuInfoRepository.save(convertFromDTO(menuDTO));

    return newMenuInfo.getMenuId() != null;
  }

  //전체 메뉴 조회
  public List<MenuDTO> getMenuList(Integer cafeId, Pageable pageable) {
    List<MenuInfo> menuInfoList = menuInfoRepository.findByCafeId(cafeId, pageable).getContent();
    return menuInfoList.stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  //메뉴 조회
  public MenuDTO getMenu(Integer menuId) {
    MenuInfo menuInfo = menuInfoRepository.findById(menuId).get();
    return convertToDTO(menuInfo);
  }


  // MenuInfo를 MenuDTO로 변환하는 메서드
  private MenuDTO convertToDTO(MenuInfo menuInfo) {
    return MenuDTO.builder()
        .menuId(menuInfo.getMenuId())
        .menuCategory(menuInfo.getMenuCategory())
        .menuName(menuInfo.getMenuName())
        .price(menuInfo.getPrice())
        .description(menuInfo.getDescription())
        .image(menuInfo.getImage())
        .cafeId(menuInfo.getCafeId())
        .build();
  }

  private MenuInfo convertFromDTO(MenuDTO menuDTO) {
    return MenuInfo.builder()
        .menuCategory(menuDTO.getMenuCategory())
        .menuName(menuDTO.getMenuName())
        .price(menuDTO.getPrice())
        .description(menuDTO.getDescription())
        .image(menuDTO.getImage())
        .cafeId(menuDTO.getCafeId())
        .build();
  }

  //메뉴 수정
  @Transactional
  public boolean setMenu(MenuDTO menuDto) {
    // 입력값 검증
    if (menuDto == null) {
      throw new IllegalArgumentException("메뉴 ID와 메뉴 정보는 필수입니다.");
    }

    // 메뉴 존재 여부 확인 및 수정
    Optional<MenuInfo> optionalMenu = menuInfoRepository.findById(menuDto.getMenuId());

    if (optionalMenu.isEmpty()) {
      return false; // 또는 throw new EntityNotFoundException
    }

    MenuInfo existingMenu = optionalMenu.get();

    // 필드 업데이트
    existingMenu.setMenuName(menuDto.getMenuName());
    existingMenu.setMenuCategory(menuDto.getMenuCategory());
    existingMenu.setPrice(menuDto.getPrice());
    existingMenu.setImage(formatImagePath(menuDto.getImage()));
    existingMenu.setDescription(menuDto.getDescription());

    return true;
  }

  private String formatImagePath(String imagePath) {
    if (imagePath == null || imagePath.trim().isEmpty()) {
      return null;
    }

    String cleanPath = imagePath.trim();

    // 이미 경로가 있으면 그대로, 없으면 추가
    if (cleanPath.startsWith("/images/menu/")) {
      return cleanPath;
    } else {
      return "/images/menu/" + cleanPath;
    }
  }

  //메뉴 삭제
  public boolean removeMenu(Integer menuId) {
    menuInfoRepository.deleteById(menuId);
    Optional<MenuInfo> menuInfo = menuInfoRepository.findById(menuId);
    return menuInfo.isPresent();
  }

}
