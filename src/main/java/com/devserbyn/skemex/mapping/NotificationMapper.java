package com.devserbyn.skemex.mapping;

import com.devserbyn.skemex.controller.dto.NotificationDTO;
import com.devserbyn.skemex.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper extends BaseMapper<NotificationDTO, Notification> {

    @Override
    @Mapping(target = "type", expression = "java(entity.getType().toString())")
    NotificationDTO toDTO(Notification entity);
}
