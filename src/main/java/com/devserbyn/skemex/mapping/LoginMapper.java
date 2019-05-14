package com.devserbyn.skemex.mapping;

import com.devserbyn.skemex.controller.dto.LoginDTO;
import org.mapstruct.Mapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface LoginMapper{

    default LoginDTO toDTO(UserDetails entity) {
        if ( entity == null ) {
            return null;
        }

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setNickname(entity.getUsername());

        Collection<? extends GrantedAuthority> authorities = entity.getAuthorities();

        if(authorities != null) {
            loginDTO.setRoles(authorities.stream().map(a -> {
                if(a != null) {
                    return a.getAuthority();
                }
                return null;
            }).collect(Collectors.toList()));
        }

        return loginDTO;
    }
}
