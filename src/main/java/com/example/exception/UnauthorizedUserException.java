package com.example.exception;

public class UnauthorizedUserException extends RuntimeException{
    private String message;

    public UnauthorizedUserException(){}

    public UnauthorizedUserException(String msg) {
        super(msg);
        this.message = msg;
    }
}
