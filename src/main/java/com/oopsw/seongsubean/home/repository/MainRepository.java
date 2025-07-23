package com.oopsw.seongsubean.home.repository;

import com.oopsw.seongsubean.home.dto.CafeAddressDTO;
import com.oopsw.seongsubean.home.dto.CafeInfoDTO;
import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import com.oopsw.seongsubean.cafe.dto.RankingDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MainRepository {

  List<CafeDTO> getMainCardView(@Param("offset") int offset);

  List<RankingDTO> getRanking();

  // String -> CafeAddressDTO로 변경
  List<CafeAddressDTO> getSearchCafeName(String cafeName);

  // String -> CafeAddressDTO로 변경
  List<CafeAddressDTO> getSearchCafeMenu(String menuCategory);

  // String -> CafeAddressDTO로 변경
  List<CafeAddressDTO> getEachMenu(String menuName);
}