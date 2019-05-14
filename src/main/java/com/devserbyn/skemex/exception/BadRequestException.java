package com.devserbyn.skemex.exception;

import com.devserbyn.skemex.controller.dto.RestExceptionCode;

public class BadRequestException extends RestException {

    private String description;

    public BadRequestException() {
        super(RestExceptionCode.BAD_REQUEST);
    }

    public BadRequestException(String description) {
        super(RestExceptionCode.BAD_REQUEST);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
