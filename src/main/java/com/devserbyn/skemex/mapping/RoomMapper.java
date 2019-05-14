package com.devserbyn.skemex.mapping;

import com.devserbyn.skemex.controller.dto.RoomDTO;
import com.devserbyn.skemex.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface RoomMapper extends BaseMapper<RoomDTO, Room> {

    @Override
    @Mapping(target = "officeId", source = "entity.floor.office.id")
    @Mapping(target = "spaceSize", expression = "java((entity.getWorkspaces() == null) ? 0 :" +
            "entity.getWorkspaces().size())")
    @Mapping(target = "spaceLeft", expression = "java(entity.countSpaceLeft())")
    @Mapping(target = "floorId", source = "entity.floor.id")
    RoomDTO toDTO(Room entity);
}
