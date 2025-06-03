package com.oopsw.seongsubean.home.repository;

import com.oopsw.seongsubean.home.dto.CafeInfoDTO;
import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import com.oopsw.seongsubean.cafe.dto.RankingDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MainRepository {

  List<CafeDTO> getMainCardView();

  List<RankingDTO> getRanking();

  List<String> getSearchCafeName(String cafeName);

  List<String> getSearchCafeMenu(String menuCategory);

  List<String> getEachMenu(String menuName);
}


