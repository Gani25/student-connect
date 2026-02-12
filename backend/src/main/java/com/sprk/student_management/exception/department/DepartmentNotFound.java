package com.sprk.student_management.exception.department;

import com.sprk.student_management.exception.StudentException;
import org.springframework.http.HttpStatus;

public class DepartmentNotFound extends StudentException {

    public DepartmentNotFound(String message, HttpStatus status) {
        super(message, status);
    }
}
