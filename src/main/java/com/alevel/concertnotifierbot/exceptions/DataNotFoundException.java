package com.alevel.concertnotifierbot.exceptions;

public class DataNotFoundException extends BaseException{
    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
