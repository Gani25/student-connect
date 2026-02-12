package com.sprk.student_management.exception;

import org.springframework.http.HttpStatus;

public class AgeInvalid extends StudentException {

    public AgeInvalid(String message, HttpStatus status) {
        super(message, status);
    }
}
