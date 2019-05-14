package com.devserbyn.skemex.controller.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomSearchDTO {
    private Long id;
    private String title;
    private long spaceSize;
    private long spaceLeft;
    private List<Long> spaces;
}
