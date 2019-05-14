package com.devserbyn.skemex.mapping;

import org.mapstruct.Mapping;

import java.util.List;

public interface BaseMapper<DTO, E> {
    DTO toDTO(E entity);

    List<DTO> toDTO(List<E> enitties);

    @Mapping(target = "id", ignore = true)
    E toEntity(DTO dto);

    List<E> toEntity(List<DTO> dtos);
}
