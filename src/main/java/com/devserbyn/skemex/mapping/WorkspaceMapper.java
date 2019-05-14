package com.devserbyn.skemex.mapping;

import com.devserbyn.skemex.controller.dto.WorkspaceDTO;
import com.devserbyn.skemex.entity.Workspace;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkspaceMapper extends BaseMapper<WorkspaceDTO, Workspace> {
    @Override
    @Mapping(target= "nickname", source = "entity.employee.nickname")
    @Mapping(target= "firstName", source = "entity.employee.firstName")
    @Mapping(target= "lastName", source = "entity.employee.lastName")
    @Mapping(target = "organizationName", source = "entity.employee.organization.name")
    @Mapping(target = "roomId", source = "entity.room.id")
    WorkspaceDTO toDTO(Workspace entity);

}
