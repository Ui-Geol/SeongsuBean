package com.oopsw.seongsubean.account.service;

import com.oopsw.seongsubean.account.dto.UserDTO;
import com.oopsw.seongsubean.account.repository.AccountRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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

  public boolean removeUser(UserDTO userDTO) {
    return accountRepository.removeUser(userDTO);
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
    if(userDTO.getNewPassword() != null){
      if(!userDTO.getNewPassword().equals(userDTO.getNewPasswordCheck())){
        throw new IllegalArgumentException("비밀번호 불일치.");
      }
      userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getNewPassword()));
    }
    if(userDTO.getNewNickName() != null){
      existsNickName(userDTO.getNewNickName());
      userDTO.setNickName(userDTO.getNewNickName());
    }
    return accountRepository.setUserInfo(userDTO);
  }

  public List<Map<String, Object>> getMyBoards(String email) {
    return accountRepository.getMyBoards(email);
  }

  public List<Map<String, Object>> getMyReviews(String email) {
    return accountRepository.getMyReviews(email);
  }

  public List<Map<String, Object>> getMyCafes(String email) {
    return accountRepository.getMyCafes(email);
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
