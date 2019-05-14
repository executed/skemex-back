package com.devserbyn.skemex.mapping;

import com.devserbyn.skemex.controller.dto.EmployeeDTO;
import com.devserbyn.skemex.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper extends BaseMapper<EmployeeDTO, Employee> {

    @Override
    @Mapping(target = "organizationName", source = "entity.organization.name")
    EmployeeDTO toDTO(Employee entity);

    @Override
    @Mapping(target = "nickname", ignore = true)
    Employee toEntity(EmployeeDTO dto);
}
