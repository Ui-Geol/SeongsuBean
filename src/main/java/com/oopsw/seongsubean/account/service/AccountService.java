package com.oopsw.seongsubean.account.service;

import com.oopsw.seongsubean.account.dto.UserDTO;
import com.oopsw.seongsubean.account.repository.AccountRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
  private final AccountRepository accountRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public boolean addUser(UserDTO userDTO) {
    if(existsEmail(userDTO.getEmail()) ||
        existsNickName(userDTO.getNickName())){
      throw new IllegalArgumentException("정보 중복");
    }
    userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

    return accountRepository.addUser(userDTO);
  }

  public boolean removeUser(String email) {
    return accountRepository.removeUser(email);
  }
  public UserDTO getUserInfo(UserDTO userDTO) {
    return accountRepository.getUserInfo(userDTO);
  }

  public UserDTO getUserByEmailAndPassword(UserDTO userDTO) {
    return accountRepository.getUserByEmailAndPassword(userDTO);
  }

  public boolean setImage(UserDTO userDTO) {
    return accountRepository.setImage(userDTO);
  }

  public boolean setUserInfo(UserDTO userDTO) {
    return accountRepository.setUserInfo(userDTO);
  }

  public List<Map<String, Object>> getMyBoards(String email, RowBounds rowBounds) {
    return accountRepository.getMyBoards(email, rowBounds);
  }

  public int countMyBoards(String email) {
    return accountRepository.countMyBoards(email);
  }

  public List<Map<String, Object>> getMyReviews(String email, RowBounds rowBounds) {
    return accountRepository.getMyReviews(email, rowBounds);
  }

  public int countMyReviews(String email) {
    return accountRepository.countMyReviews(email);
  }

  public List<Map<String, Object>> getMyCafes(String email, RowBounds rowBounds) {
    return accountRepository.getMyCafes(email, rowBounds);
  }

  public int countMyCafes(String email) {
    return accountRepository.countMyCafes(email);
  }

  public UserDTO findByEmail(String email) {
    return accountRepository.findByEmail(email);
  }


  public boolean existsNickName(String nickName) {
    if(accountRepository.existsNickName(nickName)){
      return true;
    }
    return false;
  }
  public boolean existsEmail(String email) {
    if(accountRepository.existsEmail(email)){
      return true;
    }
    return false;
  }


}
