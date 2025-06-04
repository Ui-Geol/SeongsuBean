package com.oopsw.seongsubean.home;

import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import com.oopsw.seongsubean.home.repository.MainRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
public class MainRepositoryTest {

  @Autowired
  private MainRepository mainRepository;

  @Test
  public void getSearchCafeNameTest() {
    List<String> cafe = mainRepository.getSearchCafeName("대림창고");
    System.out.println(cafe);
  }

  @Test
  public void getSearchCafeMenuTest() {
    List<String> cafe = mainRepository.getSearchCafeMenu("커피");
    System.out.println(cafe);
  }

  @Test
  public void getEachMenuTest() {
    List<String> cafe = mainRepository.getEachMenu("바게트");
    System.out.println(cafe);
  }

//  @Test
//  public void getMainCardViewTest(){
//    List<CafeInfo> cafe = mainRepository.getMainCardView();
//    System.out.println(cafe);
//  }
//<//DTO받아서 테스트코드 다시 돌려보기
//  @Test
//  public void getRankingTest(){
//    List<RangkingDTO> cafe = mainRepository.getRanking();
//    System.out.println(cafe);
//}
}
