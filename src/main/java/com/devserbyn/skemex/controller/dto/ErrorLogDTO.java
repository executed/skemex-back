package com.devserbyn.skemex.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ErrorLogDTO {

    private Long id;
    private String name;
    private String username;
    private LocalDateTime time;
    private String url;
    private int status;
    private String message;
}
