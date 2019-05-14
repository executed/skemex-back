package com.devserbyn.skemex.mapping;

import com.devserbyn.skemex.controller.dto.ReservationDTO;
import com.devserbyn.skemex.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ReservationMapper extends BaseMapper<ReservationDTO, Reservation> {

    @Override
    @Mapping(target = "employeeNickname", source = "entity.employee.nickname")
    @Mapping(target = "approverNickname", source = "entity.approver.nickname")
    @Mapping(target = "requesterNickname", source = "entity.requester.nickname")
    @Mapping(target = "workspaceNumber", source = "entity.workspace.number")
    @Mapping(target = "roomTitle", source = "entity.workspace.room.title")
    @Mapping(target = "workspaceId", source = "entity.workspace.id")
    ReservationDTO toDTO(Reservation entity);

}
