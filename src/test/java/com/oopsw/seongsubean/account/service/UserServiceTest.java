package com.oopsw.seongsubean.account.service;

import com.oopsw.seongsubean.account.dto.UserDTO;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
  @Autowired
  private AccountService accountService;

  @Test
  public void successAddUserTest() {
    accountService.addUser(UserDTO.builder()
        .email("zzzz@gmail.com")
        .password("1234")
        .nickName("리바이아커만")
        .birthDate(LocalDate.of(1999,2,20))
        .phoneNumber("010-5467-3465")
        .build());
  }
  @Test
  public void failAddUserTest() {
    accountService.addUser(UserDTO.builder()
        .email("kukukusd@gmail.com")
        .password("1234")
        .nickName("쿠쿠쿠쿠쿠")
        .birthDate(LocalDate.of(1999,2,20))
        .phoneNumber("010-5467-3465")
        .build());
  }

//  @Test
//  public void successRemoveUserTest() {
//    System.out.println(accountService.removeUser(UserDTO.builder()
//        .email("zzzz@gmail.com")
//        .password("$2a$10$V3ZvnQbwR9AKgZiFG.pTtu4PaflaeiSJ4X5LHRk4MLA3urPQcw9gC")
//        .build()));
//  }
//
//  @Test
//  public void failRemoveUserTest() {
//    System.out.println(accountService.removeUser(UserDTO.builder()
//        .email("zzzz@gmail.com")
//        .password("$2a$10$V3ZvnQbwR9AKgZiFG.pTtu4PaflaeiSJ4X5LHRk4MLA3urPQcw9gC")
//        .build()));
//  }

  @Test
  public void successGetUserInfoTest(){
    System.out.println(accountService.getUserInfo(UserDTO.builder().email("karina0411@gmail.com").password("0411").build()));
  }
  @Test
  public void failGetUserInfoTest(){
    System.out.println(accountService.getUserInfo(UserDTO.builder()
        .email("karina0411@gmail.com")
        .password("0423").build())); //null
  }

  @Test
  public void successGetUserByEmailAndPasswordTest(){
    System.out.println(accountService.getUserByEmailAndPassword(UserDTO.builder()
        .email("karina0411@gmail.com")
        .password("0411").build()));
  }
  @Test
  public void failGetUserByEmailAndPasswordTest(){
    System.out.println(accountService.getUserByEmailAndPassword(UserDTO.builder()
        .email("karina0411@gmail.com")
        .password("04121").build())); //null
  }

  @Test
  public void successSetImageTest(){
    System.out.println(accountService.setImage(UserDTO.builder()
        .email("zzzz@gmail.com")
        .image("zz.png")
        .build()));
  }

  @Test
  public void failSetImageTest(){
    System.out.println(accountService.setImage(UserDTO.builder()
        .email("zzasdasdzz@gmail.com")
        .image("zz.png")
        .build()));
  }

  @Test
  public void successSetUserInfoTest(){
    System.out.println(accountService.setUserInfo(UserDTO.builder()
        .email("zzzz@gmail.com")
        .newNickName("리바이아커만2")
        .newPassword("12345")
        .newPasswordCheck("12345")
        .build()));
  }
  @Test
  public void failSetUserInfoTest(){
    System.out.println(accountService.setUserInfo(UserDTO.builder()
        .email("zzzz@gmail.com")
        .newNickName("테일러수입푸드")
        .newPassword("12345")
        .newPasswordCheck("12345")
        .build()));
  }

  @Test
  public void successSetNickNameTest(){
    System.out.println(accountService.setUserInfo(UserDTO.builder()
        .email("zzzz@gmail.com")
        .newNickName("리바이아커만3")
        .build()));
  }
  @Test
  public void failSetNickNameTest(){
    System.out.println(accountService.setUserInfo(UserDTO.builder()
        .email("zzzz@gmail.com")
        .newNickName("리바이아커만3")
        .build()));
  }

  @Test
  public void successSetPasswordTest(){
    System.out.println(accountService.setUserInfo(UserDTO.builder()
        .email("zzzz@gmail.com")
        .newPassword("1111")
        .newPasswordCheck("1111")
        .build()));
  }
  @Test
  public void failSetPasswordTest(){
    System.out.println(accountService.setUserInfo(UserDTO.builder()
        .email("zzzz@gmail.com")
        .newPassword("1111")
        .newPasswordCheck("11211")
        .build()));
  }

//  @Test
//  public void successGetMyBoards(){
//    System.out.println(accountService.getMyBoards("dlwodnjs2669@gmail.com"));
//  }
//  @Test
//  public void failGetMyBoards(){
//    System.out.println(accountService.getMyBoards("dlwosddnjs2669@gmail.com"));
//    //null
//  }

//  @Test
//  public void successGetMyReviews(){
//    System.out.println(accountService.getMyReviews("yuqi0923@gmail.com"));
//  }
//  @Test
//  public void failGetMyReviews(){
//    System.out.println(accountService.getMyReviews("dlwosddnjs2669@gmail.com"));
//    //null
//  }

//  @Test
//  public void successGetMyCafes(){
//    System.out.println(accountService.getMyCafes("yuqi0923@gmail.com"));
//  }
//  @Test
//  public void failGetMyCafes(){
//    System.out.println(accountService.getMyCafes("dlwosddnjs2669@gmail.com"));
//    //null
//  }
}
