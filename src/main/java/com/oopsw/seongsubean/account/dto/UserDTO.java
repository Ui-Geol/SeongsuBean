package com.oopsw.seongsubean.account.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"password","newPassword","newPasswordCheck","phoneNumber"})
public class UserDTO {
  private String email;
  private String nickName;
  private String newNickName;
  private String password;
  private String passwordCheck;
  private String newPassword;
  private String newPasswordCheck;
  private String phoneNumber;
  private String image;
  private String role;
  private boolean oauth;
  private LocalDate birthDate;
  private LocalDate joinDate;
}
