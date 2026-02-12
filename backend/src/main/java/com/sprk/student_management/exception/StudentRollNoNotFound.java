package com.sprk.student_management.exception;

import org.springframework.http.HttpStatus;

public class StudentRollNoNotFound extends StudentException{

    public StudentRollNoNotFound(String message, HttpStatus status) {
        super(message, status);
    }
}
