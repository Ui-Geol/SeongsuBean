package com.oopsw.seongsubean.account.repository.jparepository;

import com.oopsw.seongsubean.account.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
  User findByEmail(String email);
  User findByNickName(String nickname);
}
