package com.devserbyn.skemex.controller.handler;

import com.devserbyn.skemex.controller.dto.Response;
import com.devserbyn.skemex.controller.dto.Response.Message;
import com.devserbyn.skemex.controller.dto.RestExceptionCode;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     *  This method handles 2 types of exception thrown: BindException &
     *  MethodArgumentNotValidException.
     *  BindException is thrown when fatal binding errors occur.
     *  MethodArgumentNotValidException is thrown when argument annotated
     *  with @Valid failed validation
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
                                     MethodArgumentNotValidException ex,
                                     HttpHeaders headers, HttpStatus status,
                                     WebRequest request) {
        List<Message> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String errorMsg = String.format("%s : %s", error.getField(),
                                                       error.getDefaultMessage());
            errors.add(new Message("field", errorMsg));
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
           String errorMsg =  String.format("%s : %s", error.getObjectName(),
                                                       error.getDefaultMessage());
           errors.add(new Message("object", errorMsg));
        }
        log.debug(String.format("MethodArgumentNotValidException was thrown with message %s",
                                errors), ex);
        return ResponseEntity.badRequest()
                             .body(Response.validation(errors));
    }

    /**
     *  This method handles 2 types of exception thrown: MissingServletRequestPartException &
     *  MissingServletRequestParameterException.
     *  MissingServletRequestPartException is thrown when the part of a multipart request
     *  not found.
     *  MissingServletRequestParameterException is thrown when request missing parameter.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
                                     MissingServletRequestParameterException ex,
                                     HttpHeaders headers,
                                     HttpStatus status,
                                     WebRequest request) {
        String errorMessage = ex.getParameterName() + " parameter is missing";
        log.debug(String.format("MissingServletRequestParameterException was thrown %s",
                                errorMessage), ex);
        Message msg = new Message(RestExceptionCode.MISSING_SERVLET_REQ_PARAM.toString(), ex.getParameterName());
        return ResponseEntity.badRequest()
                             .body(Response.validation(msg));
    }

    /**
     *  This method handles NoHandlerFoundException.
     *  NoHandlerFoundException is thrown when no handlers were found for request.
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request) {
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        log.debug(String.format("NoHandlerFoundException was thrown %s", error), ex);
        //same return data as it is in GlobalExceptionHandler.handleNotFoundException method
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(Response.error(RestExceptionCode.NOT_FOUND));
    }

    /**
     *  This method handles HttpRequestMethodNotSupportedException.
     *  NoHandlerFoundException is thrown in case user sent request with an unsupported
     *  HTTP method.
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
                                     HttpRequestMethodNotSupportedException ex,
                                     HttpHeaders headers,
                                     HttpStatus status,
                                     WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append("Supported methods: ");
        Objects.requireNonNull(ex.getSupportedHttpMethods()).forEach(t -> builder.append(t)
                                                                                 .append(" "));
        log.debug(String.format("HttpRequestMethodNotSupportedException was thrown %s",
                                builder.toString()), ex);
        Message msg = new Message(RestExceptionCode.METHOD_NOT_SUPPORTED.toString(), builder.toString());
        return ResponseEntity.badRequest()
                             .body(Response.error(msg));
    }

    /**
     *  This method handles HttpMediaTypeNotSupportedException.
     *  HttpMediaTypeNotSupportedException is thrown when the client send a request with
     *  unsupported media type.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
                                     HttpMediaTypeNotSupportedException ex,
                                     HttpHeaders headers,
                                     HttpStatus status,
                                     WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append("Supported media types: ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        log.debug(String.format("HttpMediaTypeNotSupportedException was thrown %s",
                                builder.toString()), ex);
        Message msg = new Message(RestExceptionCode.MEDIA_TYPE_NOT_SUPPORTED.toString(), builder.toString());
        return ResponseEntity.status(BAD_REQUEST)
                             .body(Response.error(msg));
    }

    /**
     *  This method handles AsyncRequestTimeoutException.
     *  AsyncRequestTimeoutException is thrown when some error occurred while processing
     *  asynchronous request .
     */
    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex,
                                                                        HttpHeaders headers,
                                                                        HttpStatus status,
                                                                        WebRequest webRequest){
        log.debug(String.format("AsyncRequestTimeoutException was thrown %s",
                                ex.getLocalizedMessage()), ex);
        return ResponseEntity.status(BAD_REQUEST)
                             .body(Response.error(RestExceptionCode.ASYNC));
    }

    /**
     *  This method handles HttpMessageConversionException which happens in some type of data
     *  binding errors in servlet layer.
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<Object> handleMessageConversion(HttpMessageConversionException ex){
        log.debug("HttpMessageConversionException exception was thrown", ex);
        return ResponseEntity.status(BAD_REQUEST)
                             .body(Response.error(RestExceptionCode.MSG_CONVERSION));
    }

    /**
     *  This method handles all unknown types of exceptions on responseEntity exception layer.
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
                                                                       HttpHeaders headers,
                                                                       HttpStatus status,
                                                                       WebRequest request) {
        log.debug("handleExceptionInternal exception was thrown", ex);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                             .body(Response.error(RestExceptionCode.DEFAULT));
    }
}
