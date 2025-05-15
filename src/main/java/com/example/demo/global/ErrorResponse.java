package com.example.demo.global;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {
    private HttpStatus httpStatus;
    private String errorMessage;

    public ErrorResponse(HttpStatus httpStatus, String errorMessage){
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
