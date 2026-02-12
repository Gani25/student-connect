package com.sprk.student_management.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExists extends StudentException {

    public EmailAlreadyExists(String message, HttpStatus status) {
        super(message, status);
    }
}
