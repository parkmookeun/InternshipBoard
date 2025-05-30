package com.example.demo.global.exception;

public class InvalidFileException extends RuntimeException {

    private String message;

    public InvalidFileException(String message) {
        super(message);
    }
}
