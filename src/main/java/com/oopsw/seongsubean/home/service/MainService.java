package com.oopsw.seongsubean.home.service;

import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import com.oopsw.seongsubean.cafe.dto.RankingDTO;
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
  public List<CafeDTO> getMainCardView() {
    return mainRepository.getMainCardView();
  }
  /**

   * 카페 별점 랭킹 리스트 조회
   */
  public List<RankingDTO> getRanking() {
    return mainRepository.getRanking();
  }

  /**
   * 카페명으로 정확히 일치하는 카페 이름 조회
   */
  public List<String> getSearchCafeName(String cafeName) {
    return mainRepository.getSearchCafeName(cafeName);
  }

  /**
   * 메뉴 카테고리에 해당하는 카페들의 주소 목록 조회
   *
   * @param menuCategory 메뉴 카테고리 (예: "빵")
   * @return [CAFE_ID, ADDRESS, DETAIL_ADDRESS] 를 합친 문자열 리스트
   */
  public List<String> getSearchCafeMenu(String menuCategory) {
    return mainRepository.getSearchCafeMenu(menuCategory);
  }

  /**
   * 메뉴명을 카테고리처럼 받아, 그 메뉴를 판매하는 카페 주소들 조회
   */
  public List<String> getEachMenu(String menuName) {
    return mainRepository.getEachMenu(menuName);
  }




}