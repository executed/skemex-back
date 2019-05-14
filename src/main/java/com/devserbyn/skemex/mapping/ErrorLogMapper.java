package com.devserbyn.skemex.mapping;

import com.devserbyn.skemex.controller.dto.ErrorLogDTO;
import com.devserbyn.skemex.entity.ErrorLog;
import org.mapstruct.Mapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Mapper (componentModel = "spring")
public interface ErrorLogMapper extends BaseMapper<ErrorLogDTO, ErrorLog> {

    @Override
    List<ErrorLogDTO> toDTO(List<ErrorLog> entity);

    default LocalDateTime map(Timestamp value){
        return value.toLocalDateTime();
    }

    default Timestamp map(LocalDateTime value){
        return Timestamp.valueOf(value);
    }
}
