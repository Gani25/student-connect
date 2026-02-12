package com.sprk.student_management.exception.department;

import com.sprk.student_management.exception.StudentException;
import org.springframework.http.HttpStatus;

public class DepartmentIdMismatch extends StudentException {

    public DepartmentIdMismatch(String message, HttpStatus status) {
        super(message, status);
    }
}
