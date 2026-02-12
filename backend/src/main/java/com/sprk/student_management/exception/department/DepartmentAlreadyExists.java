package com.sprk.student_management.exception.department;

import com.sprk.student_management.exception.StudentException;
import org.springframework.http.HttpStatus;

public class DepartmentAlreadyExists extends StudentException {

    public DepartmentAlreadyExists(String message, HttpStatus status) {
        super(message, status);
    }
}
