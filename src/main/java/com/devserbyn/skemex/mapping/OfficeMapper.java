package com.devserbyn.skemex.mapping;

import com.devserbyn.skemex.controller.dto.OfficeDTO;
import com.devserbyn.skemex.entity.Office;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OfficeMapper extends BaseMapper<OfficeDTO, Office> {
}
