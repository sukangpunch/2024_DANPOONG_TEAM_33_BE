package com.example.onetry.oauth.auth;

import com.example.onetry.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
// UserDetails 와 OAuth2User 를 모두 구현하여 일반 로그인과, OAuth2 두가지 방식 모두 사용
public class LoginUser implements UserDetails, OAuth2User {

    // User 엔티티 인스턴스를 가지고 있다.
    private final User user;
    // 소셜 로그인 시 사용자 속성을 저장하기 위한 필드
    private Map<String, Object> attributes;
    public LoginUser(User user){
        this.user = user;
    }
    public LoginUser(User user, Map<String, Object> attributes){
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return user.getName();
    }

    // 소셜 로그인시 사용자 프로필 정보 등을 접근할 수 있도록 한다.
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // 권한을 가져오고 authorities 컬렉션에 추가하여 사용자 권한 정보로 사용.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(user::getRole);
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    public Long getUserId(){
        return user.getId();
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
