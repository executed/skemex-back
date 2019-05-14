package com.devserbyn.skemex.controller.search;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomFilter {
    private String countOp;
    private Integer numberOfCount;
    private Integer firstElement;
    private Integer maxElements;
    private boolean withReserved;
    private LocalDate startDate;
    private LocalDate endDate;
}
