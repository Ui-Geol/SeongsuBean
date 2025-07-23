package com.oopsw.seongsubean.home.service;

import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import com.oopsw.seongsubean.cafe.dto.RankingDTO;
import com.oopsw.seongsubean.home.dto.CafeAddressDTO;
import com.oopsw.seongsubean.home.dto.CafeInfoDTO;
import com.oopsw.seongsubean.home.repository.MainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {
  private final MainRepository mainRepository;

  /**
   * 메인 페이지 카드 뷰에 필요한 카페 리스트 조회
   */
  public List<CafeDTO> getMainCardView(int page) {
    int offset = Math.max((page - 1) * 4, 0);
    return mainRepository.getMainCardView(offset);
  }

  /**
   * 카페 별점 랭킹 리스트 조회
   */
  public List<RankingDTO> getRanking() {
    return mainRepository.getRanking();
  }

  /**
   * 카페명으로 정확히 일치하는 카페 정보 조회 (수정)
   */
  public List<CafeAddressDTO> getSearchCafeName(String cafeName) {
    return mainRepository.getSearchCafeName(cafeName);
  }

  /**
   * 메뉴 카테고리에 해당하는 카페들의 정보 조회 (수정)
   *
   * @param menuCategory 메뉴 카테고리 (예: "빵")
   * @return cafeId와 address를 포함한 CafeAddressDTO 리스트
   */
  public List<CafeAddressDTO> getSearchCafeMenu(String menuCategory) {
    return mainRepository.getSearchCafeMenu(menuCategory);
  }

  /**
   * 메뉴명을 카테고리처럼 받아, 그 메뉴를 판매하는 카페 정보들 조회 (수정)
   */
  public List<CafeAddressDTO> getEachMenu(String menuName) {
    return mainRepository.getEachMenu(menuName);
  }
}