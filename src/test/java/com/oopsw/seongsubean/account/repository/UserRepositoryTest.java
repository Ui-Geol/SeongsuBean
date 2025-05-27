package com.oopsw.seongsubean.account.repository;


import com.oopsw.seongsubean.account.dto.UserDTO;
import java.time.LocalDate;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {
  @Autowired
  private AccountRepository userMapper;

  @Test
  public void getUserInfoTest() {
    UserDTO user = userMapper.getUserInfo(UserDTO.builder().passWord("2345").email("zzzz@gmail.com").build());
    System.out.println(user);
  }

  @Test
  public void addUserTest(){
    userMapper.addUser(UserDTO.builder()
        .nickName("아커만리바이")
        .passWord("1234")
        .phoneNumber("010-2323-4787")
        .email("xxxxxx@gmail.com")
        .birthDate(LocalDate.of(1999,2,20))
        .build());
  }

  @Test
  public void getPassWordTest(){
    UserDTO user = userMapper.getPassWord(UserDTO.builder().passWord("1234").email("xxxxxx@gmail.com").build());
    System.out.println(user);
  }

  @Test
  public void setUserInfoTest(){
    userMapper.setUserInfo(UserDTO.builder().email("xxxxxx@gmail.com").passWord("5678").build());
  }

  @Test
  public void setImageTest(){
    userMapper.setImage(UserDTO.builder().image("asd.png").email("xxxxxx@gmail.com").build());
  }

  @Test
  public void getMyBoardsTest(){
    System.out.println(userMapper.getMyBoards("jennie0116@gmail.com").toString());
  }

  @Test
  public void getMyReviewsTest(){
    System.out.println(userMapper.getMyReviews("jennie0116@gmail.com").toString());
  }

  @Test
  public void getMyCafesTest(){
    System.out.println(userMapper.getMyCafes("yuqi0923@gmail.com").toString());
  }
}
