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
@ToString(exclude = {"passWord","phoneNumber"})
public class UserDTO {
  private String email;
  private String nickName;
  private String passWord;
  private String phoneNumber;
  private String image;
  private String role;
  private LocalDate birthDate;
  private LocalDate joinDate;
}
