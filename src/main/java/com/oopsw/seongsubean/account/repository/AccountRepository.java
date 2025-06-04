package com.oopsw.seongsubean.account.repository;

import com.oopsw.seongsubean.account.dto.UserDTO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

@Mapper
public interface AccountRepository {
  boolean addUser(UserDTO userDTO);
  boolean removeUser(String email);
  UserDTO getUserInfo(UserDTO userDTO);
  UserDTO getUserByEmailAndPassword(UserDTO userDTO);
  boolean setImage(UserDTO userDTO);
  boolean setUserInfo(UserDTO userDTO);
  List<Map<String, Object>> getMyBoards(String email, RowBounds rowBounds);
  int countMyBoards(String email);
  List<Map<String, Object>> getMyReviews(String email, RowBounds rowBounds);
  int countMyReviews(String email);
  List<Map<String, Object>> getMyCafes(String email, RowBounds rowBounds);
  int countMyCafes(String email);
  boolean existsEmail(String email);
  boolean existsNickName(String nickName);
  UserDTO findByEmail(String email);
}
