package com.devserbyn.skemex.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FloorDTO {
    private Long id;
    private Integer number;
    private Long officeId;
}
