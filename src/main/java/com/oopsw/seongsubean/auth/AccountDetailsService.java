package com.oopsw.seongsubean.auth;

//import com.oopsw.seongsubean.account.dto.UserDTO;
import com.oopsw.seongsubean.account.domain.UserInfo;
import com.oopsw.seongsubean.account.repository.jparepository.UserInfoRepository;
//import com.oopsw.seongsubean.account.repository.mybatisrepository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDetailsService implements UserDetailsService {
  private final UserInfoRepository userInfoRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserInfo user = userInfoRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));
    return new AccountDetails(user);
  }
}
