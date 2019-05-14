package com.devserbyn.skemex.exception;

import com.devserbyn.skemex.controller.dto.RestExceptionCode;
import lombok.Getter;

@Getter
public class NotFoundException extends RestException {

    private String description;

    public NotFoundException() {
        super(RestExceptionCode.NOT_FOUND);
    }

    public NotFoundException(String description) {
        super(RestExceptionCode.NOT_FOUND);
        this.description = description;
    }

}

