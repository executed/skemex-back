package com.devserbyn.skemex.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MigrationConflictDTO {
    private Long id;
    private String message;
    private Boolean resolved;

    private Boolean roomConflict;
    private Integer floorNumber;
}
