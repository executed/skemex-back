package com.devserbyn.skemex.controller.dto;

import com.devserbyn.skemex.validation.dto.DTOValidationCheck.UpdateCheck;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;


@Getter
@Setter
@NoArgsConstructor
public class RoomDTO {
    @Null
    @NotNull (groups = {UpdateCheck.class})
    private Long id;
    @NotEmpty
    private String title;
    @NotNull
    private Long floorId;

    private Long officeId;
    @Min(0)
    private long spaceSize;
    @Min(0)
    private long spaceLeft;
}
