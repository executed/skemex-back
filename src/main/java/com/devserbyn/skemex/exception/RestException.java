package com.devserbyn.skemex.exception;

import com.devserbyn.skemex.controller.dto.RestExceptionCode;
import lombok.Getter;

@Getter
public class RestException extends RuntimeException {

    protected final RestExceptionCode code;

    RestException(RestExceptionCode code) {
        this.code = code;
    }

}
