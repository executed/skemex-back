package com.devserbyn.skemex.mapping;

import com.devserbyn.skemex.controller.dto.FloorDTO;
import com.devserbyn.skemex.entity.Floor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FloorMapper extends BaseMapper<FloorDTO, Floor> {
    @Override
    @Mapping(target = "officeId", source = "entity.office.id")
    FloorDTO toDTO(Floor entity);
}

