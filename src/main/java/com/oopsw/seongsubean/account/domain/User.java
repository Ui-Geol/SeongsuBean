package com.oopsw.seongsubean.account.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"password"})
@Table(name = "user_info")
public class User {
  @Id
  private String email;
  @Column(unique = true, nullable = false)
  private String nickName;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private LocalDate birthDate;
  @Column(nullable = false)
  private String phoneNumber;
  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDate joinDate;
  @Column(nullable = true)
  private String image;
  @Column(nullable = true)
  private String role = "CUSTOMER";
}
