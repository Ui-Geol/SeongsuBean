package com.oopsw.seongsubean.auth;

import com.oopsw.seongsubean.account.dto.UserDTO;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class AccountDetails implements UserDetails, OAuth2User {
  private final UserDTO user;
  private Map<String, Object> attributes;

  public AccountDetails(UserDTO user) {this.user = user;}

  public AccountDetails(UserDTO user, Map<String, Object> attributes) {
    this.user = user;
    this.attributes = attributes;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes != null ? attributes : Map.of();
  }

  @Override
  public String getName() {
    return user.getEmail();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    String role = user.getRole(); // 예: "CUSTOMER", "OWNER"
    if (role == null || role.isBlank()) {
      throw new IllegalStateException("Role is not set for account: " + user.getEmail());
    }
    return List.of(new SimpleGrantedAuthority("ROLE_" + role)); // ✅ ROLE_ 접두사 붙이기
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public UserDTO getUser() {
    return this.user;
  }
}
