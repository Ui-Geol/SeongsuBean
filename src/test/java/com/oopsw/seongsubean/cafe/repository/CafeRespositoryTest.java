package com.oopsw.seongsubean.cafe.repository;

import com.oopsw.seongsubean.cafe.repository.mybatisrepository.CafeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CafeRespositoryTest {

  @Autowired
  CafeRepository cafeRepository;

  @Test
  public void testCafeRepository() {
    System.out.println(cafeRepository.getCafeName(3));
  }
}
