package com.oopsw.seongsubean.home.service;

import com.oopsw.seongsubean.home.repository.MainRepository;
import com.oopsw.seongsubean.home.dto.CafeInfo;
import com.oopsw.seongsubean.home.dto.RankingDto;
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
  public List<CafeInfo> getMainCardView() {
    return mainRepository.getMainCardView();
  }

  /**
   * 카페 별점 랭킹 리스트 조회
   */
  public List<RankingDto> getRanking() {
    return mainRepository.getRanking();
  }

  /**
   * 카페명으로 정확히 일치하는 카페 이름 조회
   */
  public String getSearchCafeName(String cafeName) {
    return mainRepository.getSearchCafeName(cafeName);
  }

  /**
   * 메뉴명으로 해당 메뉴를 팔고 있는 카페 이름들 조회
   */
  public List<String> getSearchCafeMenu(String menuName) {
    return mainRepository.getSearchCafeMenu(menuName);
  }

  /**
   * 메뉴명을 카테고리처럼 받아, 그 메뉴를 판매하는 카페 주소들 조회
   */
  public List<String> getEachMenu(String menuName) {
    return mainRepository.getEachMenu(menuName);
  }
}