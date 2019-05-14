package com.devserbyn.skemex.controller.dto;

import com.devserbyn.skemex.exception.RestException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

import static com.devserbyn.skemex.controller.dto.ResponseViewStatus.*;

@Getter
public class Response<T> {

    /**
     * Status of response (ERROR, VALIDATION, OK)
     */
    private ResponseViewStatus status;
    /**
     * Strings, which represent some messages with problem description
     */
    private List<Message> messageList;
    /**
     * Generic type field which should contain some data in case of status
     * field is OK.
     */
    private T data;

    private Response(ResponseViewStatus status, List<Message> messageList, T data) {
        this.status = status;
        this.messageList = messageList;
        this.data = data;
    }

    public static Response success() {
        return new Response<>(OK, null, null);
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(OK, null, data);
    }

    public static Response validation(List<Message> messages) {
        return new Response<>(VALIDATION, messages, null);
    }

    public static Response validation(Message msg) {
        return validation(Collections.singletonList(msg));
    }

    public static Response error(RestException ex) {
        return error(ex.getCode());
    }

    public static Response error(RestExceptionCode code) {
        Message msg = new Message(code.toString(), code.getResponseMessage());
        return error(Collections.singletonList(msg));
    }

    public static Response error(Message msg) {
        return new Response<>(ERROR, Collections.singletonList(msg), null);
    }

    public static Response error(List<Message> messages) {
        return new Response<>(ERROR, messages, null);
    }

    /**
     *  Inner class, that is used when response entity needs to contain some messages
     *  which are represented as instances of this class
     */
    @Getter
    @AllArgsConstructor
    @ToString
    public static class Message {

        private final String field;
        private final String text;

        public Message(String text) {
            this.field = null;
            this.text = text;
        }
    }

}
