package com.oopsw.seongsubean.account.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "USER_INFO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {
    @Id
    @Column(name = "EMAIL", length = 320)
    private String email;
    @Column(name = "NICKNAME", length = 50)
    private String nickName;
    @Column(name = "PASSWORD", length = 100)
    private String password;
    @Column(name = "BIRTHDATE")
    private LocalDate birthDate;
    @Column(name = "PHONE_NUMBER", length = 20)
    private String phoneNumber;
    @Column(name = "JOIN_DATE")
    private LocalDate joinDate;
    @Column(name = "IMAGE", length = 500)
    private String image;
    @Column(name = "ROLE", nullable = false, length = 20)
    private String role;
    private boolean oauth;
}

