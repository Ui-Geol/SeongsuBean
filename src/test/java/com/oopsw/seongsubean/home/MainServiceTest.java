//package com.oopsw.seongsubean.home;
//import com.oopsw.seongsubean.home.repository.MainRepository;
//import com.oopsw.seongsubean.home.service.MainService;
//import java.util.List;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class MainServiceTest {
//  @Autowired
//  MainService mainService;
//
//
//  @Test
//  public void getMainCardViewtest() {
//    List<CafeInfo> mainCardView = mainService.getMainCardView();
//    System.out.println(mainCardView);
//  }
//
//  @Test
//  public void getRankingtest() {
//    List<RankingDto> getRanking = mainService.getRanking();
//    System.out.println(getRanking);
//  }
//
//  @Test
//  public void getSearchCafeNametest() {
//    String getSearchCafeName = mainService.getSearchCafeName("대림창고");
//    System.out.println(getSearchCafeName);
//  }
//
//  @Test
//  public void getSearchCafeMenutest() {
//    List<String> getSearchCafeMenu = mainService.getSearchCafeMenu("바게트");
//    System.out.println(getSearchCafeMenu);
//  }
//
//  @Test
//  public void getEachMenutest() {
//    List<String> getEachMenu = mainService.getEachMenu("바게트");
//    System.out.println(getEachMenu);
//  }
//}
