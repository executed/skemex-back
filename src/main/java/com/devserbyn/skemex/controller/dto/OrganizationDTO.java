package com.devserbyn.skemex.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrganizationDTO {
    private Long id;
    private String name;
    private String ownerNickname;
    private String parentName;
}
