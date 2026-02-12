package com.sprk.student_management.exception;


import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class StudentException extends RuntimeException {

    // Exception -> urlPath, msg, statusCode
    private HttpStatus status;

    public StudentException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
