package com.devserbyn.skemex.service.schedule.filter;

import com.devserbyn.skemex.entity.RequestStatus;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationUpdateFilter {
    private RequestStatus oldStatus;
    private RequestStatus newStatus;
    private LocalDate startTime;
    private LocalDate endTime;
}
