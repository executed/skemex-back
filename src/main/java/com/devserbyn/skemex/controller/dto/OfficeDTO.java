package com.devserbyn.skemex.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OfficeDTO {
    private Long id;
    private String name;
    private String city;
    private String timeZone;
}
