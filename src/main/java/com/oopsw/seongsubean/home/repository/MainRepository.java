package com.oopsw.seongsubean.home.repository;

import com.oopsw.seongsubean.home.dto.CafeInfo;
import com.oopsw.seongsubean.home.dto.RankingDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainRepository {

  List<CafeInfo> getMainCardView();

  List<RankingDto> getRanking();

  String getSearchCafeName(String cafeName);

  List<String> getSearchCafeMenu(String menuName);

  List<String> getEachMenu(String menuName);

}


