package com.devserbyn.skemex.mapping;


import com.devserbyn.skemex.controller.dto.MigrationConflictDTO;
import com.devserbyn.skemex.entity.MigrationConflict;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MigrationConflictMapper extends BaseMapper<MigrationConflictDTO, MigrationConflict> {

    @Override
    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "message", source = "entity.message")
    @Mapping(target = "resolved", source = "entity.resolved")
    @Mapping(target = "roomConflict", expression = "java(entity.getRoomAttributes() != null)")
    MigrationConflictDTO toDTO(MigrationConflict entity);
}
