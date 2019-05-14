package com.devserbyn.skemex.controller.dto;

/**
 *  Enumeration class which contains error codes of custom exceptions, thrown on server side
 *  that will be used on UI side to process type of reaction on errors happening on server.
 */
public enum RestExceptionCode {

    /** Exception handler wasn't found */
    DEFAULT("Unexpected error occurred"),
    /** BadRequestException was thrown */
    BAD_REQUEST("Bad request"),
    /** No mapping for context was found in controller layer */
    NOT_FOUND("No mapping for context"),
    /** Request Http method not supported for current context */
    METHOD_NOT_SUPPORTED("Request http method not supported"),
    /** Media type, that was sent not supported for specified context */
    MEDIA_TYPE_NOT_SUPPORTED("Request media type not supported"),
    /** Asynchronous request is not supported for specified context */
    ASYNC("Asynchronous request not supported"),
    /**
     * Https message is not readable/writable
     * (perhaps some problems with Jackson serialization)
     */
    MSG_CONVERSION("Something wrong with http message conversion"),
    /** Some of the required parameters in handling method is missing */
    MISSING_SERVLET_REQ_PARAM("Request parameter is missing"),
    /** Some exception was thrown at repository level */
    REPOSITORY("Something wrong with database"),
    /** Getting/calculating data from data source returned empty object */
    NO_CONTENT("No data found");

    private final String responseMessage;

    RestExceptionCode(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }}
