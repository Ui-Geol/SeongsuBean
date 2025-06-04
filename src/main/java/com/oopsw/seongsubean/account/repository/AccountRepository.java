package com.oopsw.seongsubean.account.repository;

import com.oopsw.seongsubean.account.dto.UserDTO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountRepository {
  public boolean addUser(UserDTO userDTO);
  public boolean removeUser(String email);
  public UserDTO getUserInfo(UserDTO userDTO);
  public UserDTO getUserByEmailAndPassword(UserDTO userDTO);
  public boolean setImage(UserDTO userDTO);
  public boolean setUserInfo(UserDTO userDTO);
  public List<Map<String, Object>> getMyBoards(String email);
  public List<Map<String, Object>> getMyReviews(String email);
  public List<Map<String, Object>> getMyCafes(String email);
  public boolean existsEmail(String email);
  public boolean existsNickName(String nickName);
  public UserDTO findByEmail(String email);
}
