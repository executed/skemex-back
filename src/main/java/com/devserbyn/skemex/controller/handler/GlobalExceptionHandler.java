package com.devserbyn.skemex.controller.handler;

import com.devserbyn.skemex.controller.dto.Response;
import com.devserbyn.skemex.controller.dto.Response.Message;
import com.devserbyn.skemex.controller.dto.RestExceptionCode;
import com.devserbyn.skemex.exception.BadRequestException;
import com.devserbyn.skemex.exception.NotFoundException;
import com.devserbyn.skemex.exception.RestException;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.JDBCException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     *  This method handles SQLException.
     *  SQLException reports problems while working with database.
     */
    @ExceptionHandler({ SQLException.class })
    public ResponseEntity<Response> handleSQL(SQLException ex) {
        log.error("SQLException was thrown", ex);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                             .body(Response.error(RestExceptionCode.REPOSITORY));
    }

    /**
     *  This method handles JDBCException.
     *  JDBCException reports problems while working with database
     *  through Hibernate.
     */
    @ExceptionHandler({ JDBCException.class })
    public ResponseEntity<Response> handlePersistence(JDBCException ex) {
        // Class: Integrity Constraint Violation (23); Code: unique_violation (23505)
        if (ex.getSQLState().equals("23505")){
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                    .body(Response.success());
        }
        log.error("PersistenceException was thrown", ex);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(Response.error(RestExceptionCode.REPOSITORY));
    }

    /**
     *  This method handles BadRequestException which is inherited from RestException and throws in
     *  case of some request from client doesn't fit in logic of what server side waits on
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response> handleBadRequestException(BadRequestException ex){
        log.debug(String.format("BadRequestException was thrown %s",
                                ex.getDescription()), ex);
        Message msg = new Message(RestExceptionCode.BAD_REQUEST.toString(), ex.getDescription());
        return ResponseEntity.badRequest()
                             .body(Response.validation(msg));
    }

    /**
     *  This method handles NotFoundException which is inherited from RestException and throws in
     *  case of some data requested from client is not found
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response> handleNotFoundException(NotFoundException ex){
        log.debug("NotFoundException was thrown", ex);
        Message msg = new Message(RestExceptionCode.NOT_FOUND.toString(), ex.getDescription());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(Response.error(msg));
    }

    /**
     *  This method handles RestException which is itself top of the project custom exceptions
     *  hierarchy. So this method handles exceptions in case some unknown exception inherited
     *  from RestException was thrown or not handled by other exception handler in this class.
     */
    @ExceptionHandler(RestException.class)
    public ResponseEntity<Response> handleRestException(RestException ex){
        log.debug("RestException was thrown", ex);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(Response.error(ex));
    }

    /**
     *  This method handles all types of exceptions.
     *  This ExceptionHandler is used when no handler for common
     *  types of exceptions was found.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleAll(Exception ex){
        log.debug("Exception was thrown", ex);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(Response.error(RestExceptionCode.DEFAULT));
    }
}
