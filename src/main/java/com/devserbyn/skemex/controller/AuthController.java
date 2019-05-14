package com.devserbyn.skemex.controller;

import com.devserbyn.skemex.controller.dto.Response;
import com.devserbyn.skemex.controller.dto.RestExceptionCode;
import com.devserbyn.skemex.mapping.LoginMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
public class AuthController {

    private LoginMapper mapper;

    @Autowired
    public AuthController(LoginMapper mapper) {
        this.mapper = mapper;
    }

    @GetMapping
    @RequestMapping("/login")
    public String getLogin() {
        return "";
    }

    @GetMapping
    @RequestMapping("/accountInfo")
    @ResponseStatus(HttpStatus.OK)
    public Response getAccountInfo() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if(principal instanceof UserDetails) {
            return Response.success(mapper.toDTO(((UserDetails) principal)));
        }else {
            return Response.error(RestExceptionCode.NO_CONTENT);
        }
    }
}
