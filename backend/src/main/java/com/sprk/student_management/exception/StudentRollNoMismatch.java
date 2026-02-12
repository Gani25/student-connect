package com.sprk.student_management.exception;

import org.springframework.http.HttpStatus;

public class StudentRollNoMismatch extends StudentException {

    public StudentRollNoMismatch(String message, HttpStatus status) {
        super(message, status);
    }
}
