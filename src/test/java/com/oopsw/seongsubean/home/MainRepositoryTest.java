package com.oopsw.seongsubean.home;

import com.oopsw.seongsubean.home.dto.CafeInfo;
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
  public void getSearchCafeName() {
    String cafe = mainRepository.getSearchCafeName("대림창고");
    System.out.println(cafe);
  }

  @Test
  public void getSearchCafeMenu(){
    List<String> cafe = mainRepository.getSearchCafeMenu("바게트");
    System.out.println(cafe);
  }

  @Test
  public void getEachMenu(){
    List<String> cafe = mainRepository.getEachMenu("바게트");
    System.out.println(cafe);
  }

  @Test
  public void getMainCardView(){
    List<CafeInfo> cafe = mainRepository.getMainCardView();
    System.out.println(cafe);
  }

}
