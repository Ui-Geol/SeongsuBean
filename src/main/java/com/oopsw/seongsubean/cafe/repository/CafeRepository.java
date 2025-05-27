package com.oopsw.seongsubean.cafe.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CafeRepository {

  public String getCafeName(int cafeId);
}
