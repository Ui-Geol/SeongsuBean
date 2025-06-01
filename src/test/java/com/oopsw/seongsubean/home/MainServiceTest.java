package com.oopsw.seongsubean.home;
import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import com.oopsw.seongsubean.cafe.dto.RankingDTO;
import com.oopsw.seongsubean.home.dto.CafeInfoDTO;
import com.oopsw.seongsubean.home.service.MainService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MainServiceTest {
  @Autowired
  MainService mainService;
  CafeDTO cafeDTO;

  @Test
  public void getMainCardViewtest() {
    List<CafeDTO> mainCardView = mainService.getMainCardView();
    System.out.println(mainCardView);
  }

  @Test
  public void getRankingtest() {
    List<RankingDTO> getRanking = mainService.getRanking();
    System.out.println(getRanking);
  }

  @Test
  public void getSearchCafeNametest() {
    List<String> getSearchCafeName = mainService.getSearchCafeName("성수");
    System.out.println(getSearchCafeName);
  }

  @Test
  public void getSearchCafeMenutest() {
    List<String> getSearchCafeMenu = mainService.getSearchCafeMenu("커피");
    System.out.println(getSearchCafeMenu);
  }

  @Test
  public void getEachMenutest() {
    List<String> getEachMenu = mainService.getEachMenu("바게트");
    System.out.println(getEachMenu);
  }
}
