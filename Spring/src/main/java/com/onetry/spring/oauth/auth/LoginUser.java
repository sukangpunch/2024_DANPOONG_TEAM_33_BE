package com.onetry.spring.oauth.auth;

import com.onetry.spring.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
// UserDetails 와 OAuth2User 를 모두 구현하여 일반 로그인과, OAuth2 두가지 방식 모두 사용
public class LoginUser implements UserDetails {

    // User 엔티티 인스턴스를 가지고 있다.
    private final User user;
    public LoginUser(User user){
        this.user = user;
    }

    // 권한을 가져오고 authorities 컬렉션에 추가하여 사용자 권한 정보로 사용.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(user::getRole);
        return authorities;
    }

    public Long getUserId(){

        return user.getId();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
