package com.devserbyn.skemex.configuration.security;

import com.devserbyn.skemex.entity.Role;
import com.devserbyn.skemex.entity.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class UserDetailsImpl implements UserDetails {
    private String nickname;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;


    public static UserDetails create(User user) {
        Collection<? extends GrantedAuthority> authorities =
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList());

        return UserDetailsImpl.builder()
                .nickname(user.getNickname())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return nickname;
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
}
