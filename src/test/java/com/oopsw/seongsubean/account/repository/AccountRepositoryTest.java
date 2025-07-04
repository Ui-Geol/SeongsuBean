package com.oopsw.seongsubean.account.repository;


import com.oopsw.seongsubean.account.dto.UserDTO;
import com.oopsw.seongsubean.account.repository.mybatisrepository.AccountRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class AccountRepositoryTest {
  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Test
  public void successGetUserInfoTest() {
    UserDTO user = accountRepository.getUserInfo(UserDTO.builder().password("0524").email("hayoon@gmail.com").build());
    System.out.println(user);
  }
  @Test
  public void failGetUserInfoTest() {
    UserDTO user = accountRepository.getUserInfo(UserDTO.builder().password("2345").build());
    System.out.println(user); //null
  }

  @Test
  public void successAddUserTest(){
    accountRepository.addUser(UserDTO.builder()
        .nickName("아커만리바이222")
        .password("1234")
        .phoneNumber("010-2323-4787")
        .email("xxxxxx2@gmail.com")
        .birthDate(LocalDate.of(1999,2,20))
        .build());
  }
  @Test
  public void failAddUserTest(){
    assertThrows(Exception.class, ()->{
      accountRepository.addUser(UserDTO.builder()
          .nickName("아커만리바이")
          .password("1234")
          .phoneNumber("010-2323-4787")
          .email("xxxxxx@gmail.com")
          .birthDate(LocalDate.of(1999,2,20))
          .build());
    });
  }

  @Test
  public void successGetPassWordTest(){
    UserDTO user = accountRepository.getUserByEmailAndPassword(UserDTO.builder().password("1234").email("xxxxxx@gmail.com").build());
    System.out.println(user);
  }
  @Test
  public void failGetPassWordTest(){
    UserDTO user = accountRepository.getUserByEmailAndPassword(UserDTO.builder().password("1232334").email("xxxxxx@gmail.com").build());
    System.out.println(user); //null
  }

  @Test
  public void successSetUserInfoTest(){
    accountRepository.setUserInfo(UserDTO.builder().email("xxxxxx@gmail.com").password("5678").build());
  }
  @Test
  public void failSetUserInfoTest(){
    accountRepository.setUserInfo(UserDTO.builder().email("xxxxsdxx@gmail.com").password("5678").build());
    // 없는 이메일이여도 update문은 0행을 업데이트 함
  }

  @Test
  public void successSetImageTest(){
    accountRepository.setImage(UserDTO.builder().image("asd.png").email("xxxxxx@gmail.com").build());
  }
  @Test
  public void failSetImageTest(){
    accountRepository.setImage(UserDTO.builder().image("asd.png").email("xxxx32xx@gmail.com").build());
    //마찬가지
  }

//  @Test
//  public void successGetMyBoardsTest(){
//    System.out.println(userMapper.getMyBoards("jennie0116@gmail.com").toString());
//  }
//  @Test
//  public void failGetMyBoardsTest(){
//    System.out.println(userMapper.getMyBoards("jen23nie0116@gmail.com").toString());
//    //null 반환
//  }

//  @Test
//  public void successGetMyReviewsTest(){
//    System.out.println(userMapper.getMyReviews("jennie0116@gmail.com").toString());
//  }
//  @Test
//  public void failGetMyReviewsTest(){
//    System.out.println(userMapper.getMyReviews("jennie0116@gmail.com").toString());
//    //null 반환
//  }

//  @Test
//  public void successGetMyCafesTest(){
//    System.out.println(userMapper.getMyCafes("yuqi0923@gmail.com").toString());
//  }
//  @Test
//  public void failGetMyCafesTest(){
//    System.out.println(userMapper.getMyCafes("yuqi0923@gmail.com").toString());
//    //null 반환
//  }

  @Test
  public void successExcistsEmailTest(){
    System.out.println(accountRepository.existsEmail("hayoondsadas@gmail.com"));
  }

  @Test
  public void successExcistsNickNameTest(){
    System.out.println(accountRepository.existsNickName("아커만vf리바이"));
  }

  @Test
  public void successDeleteUser(){
    System.out.println(accountRepository.removeUser("z2131zzz@gmail.com"));
  }
//  @Test
//  public void failDeleteUser(){
//    System.out.println(accountRepository.removeUser(UserDTO.builder()
//        .email("zzsazz@gmail.com")
//        .password("$2a$10$vWgCDeAzCaJlgEPyBJcbUuznaorCNKUEH77uKxwCbQdvPZe7UkLWi")
//        .build()));
//  }

  @Test
  public void successUpdatePw(){
    System.out.println(accountRepository.updatePw(UserDTO.builder().password(bCryptPasswordEncoder.encode("1234")).build()));
  }
}
