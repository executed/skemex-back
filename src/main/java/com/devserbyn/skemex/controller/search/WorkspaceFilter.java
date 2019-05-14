package com.devserbyn.skemex.controller.search;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceFilter {
    private String project;
    private String employee;
    private Long workspaceId;
    private Integer firstElement;
    private Integer maxElements;
    private Long roomId;
    private boolean withReserved;
    private LocalDate startDate;
    private LocalDate endDate;
}
