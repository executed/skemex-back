package com.devserbyn.skemex.controller.search;

import com.devserbyn.skemex.entity.RequestStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationSearch {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";

    private RequestStatus requestStatus;
    private String employeeNickname;
    private String approverNickname;
    private String requesterNickname;
    private Long workspaceId;
    private Integer workspaceNumber;
    private String roomTitle;
    private LocalDate startTime;
    private LocalDate endTime;
}
