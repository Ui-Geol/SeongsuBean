package com.oopsw.seongsubean.account.repository.jparepository;

import com.oopsw.seongsubean.account.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
    // 이메일(PK)로 사용자 조회
    Optional<UserInfo> findByEmail(String email);

    // 닉네임 중복 체크용
    boolean existsByNickName(String nickName);

}
