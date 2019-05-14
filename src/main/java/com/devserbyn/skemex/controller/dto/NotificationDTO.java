package com.devserbyn.skemex.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDTO {
    private Long id;
    private String message;
    private String type;
    private Long migrationId;
    private Boolean viewed;
}
