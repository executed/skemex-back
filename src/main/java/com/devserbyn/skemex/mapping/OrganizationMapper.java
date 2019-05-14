package com.devserbyn.skemex.mapping;

import com.devserbyn.skemex.controller.dto.OrganizationDTO;
import com.devserbyn.skemex.entity.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrganizationMapper extends BaseMapper<OrganizationDTO, Organization> {

    @Override
    @Mapping(target = "ownerNickname", source = "entity.owner.nickname")
    @Mapping(target = "parentName", source = "entity.parent.name")
    OrganizationDTO toDTO(Organization entity);

}
