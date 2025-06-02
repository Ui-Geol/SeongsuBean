package com.oopsw.seongsubean.cafe.repository.mybatisrepository;

import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import com.oopsw.seongsubean.cafe.dto.CafeHeaderDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CafeRepository {

  CafeDTO getAllByCafeId(int cafeId);

  String getCafeNameByCafeId(int cafeId);

  Boolean addCafe(CafeDTO cafe);

  Integer getCafeIdByCafeNameAndAddress(CafeDTO cafe);

  CafeHeaderDTO getCafeHeaderByCafeId(int cafeId);

  Boolean setCafe(CafeDTO updatedCafe);

  Boolean removeCafe(int cafeId);

  Boolean setCafeStatus(@Param("cafeId") Integer cafeId, @Param("status") String status);
}
