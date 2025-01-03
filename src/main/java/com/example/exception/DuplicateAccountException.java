package com.example.exception;

public class DuplicateAccountException extends RuntimeException{

    private String message;

    public DuplicateAccountException() {}

    public DuplicateAccountException(String msg) {
        super(msg);
        this.message = msg;
    }

}
