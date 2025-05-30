package com.oopsw.seongsubean.cafe.repository.mybatisrepository;

import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import com.oopsw.seongsubean.cafe.dto.CafeHeaderDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CafeRepository {

  public CafeDTO getAllByCafeId(int cafeId);

  public String getCafeNameByCafeId(int cafeId);

  public Boolean addCafe(CafeDTO cafe);

  public Integer getCafeIdByCafeNameAndAddress(CafeDTO cafe);

  public CafeHeaderDTO getCafeHeaderByCafeId(int cafeId);

  public Boolean setCafe(CafeDTO cafe);

  public Boolean removeCafe(int cafeId);
}
