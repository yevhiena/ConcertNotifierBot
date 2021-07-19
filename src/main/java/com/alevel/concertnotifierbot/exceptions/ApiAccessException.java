package com.alevel.concertnotifierbot.exceptions;

public class ApiAccessException extends BaseException{
    public ApiAccessException(String message) {
        super(message);
    }

    public ApiAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
