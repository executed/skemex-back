package com.devserbyn.skemex.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LoginDTO {
    private String nickname;
    private List<String> roles;
}
