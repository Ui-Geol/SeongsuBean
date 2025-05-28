package com.oopsw.seongsubean.cafe.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MenuInfoRespositoryTest {

  @Autowired
  private MenuInfoRepository menuInfoRepository;

  @Test
  public void findById() {
    System.out.println(menuInfoRepository.findById(1));

  }
}
