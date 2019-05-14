package com.devserbyn.skemex.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.devserbyn.skemex.entity.RequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReservationDTO {

   public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";

   private Long id;
   private RequestStatus requestStatus;
   private String employeeNickname;
   private String approverNickname;
   private String requesterNickname;
   private Long workspaceId;
   private Integer workspaceNumber;
   private String roomTitle;
   private String description;
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
   private LocalDateTime requestedTime;

   private LocalDate startTime;

   private LocalDate endTime;
}
