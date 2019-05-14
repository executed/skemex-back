package com.devserbyn.skemex.controller.dto;

import com.devserbyn.skemex.entity.WorkspaceStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WorkspaceDTO {
    private Long id;
    private Integer number;
    private WorkspaceStatus status;
    private Float x;
    private Float y;
    private String nickname;
    private String firstName;
    private String lastName;
    private String organizationName;
    private Long roomId;
    private boolean checkDelete;
}
