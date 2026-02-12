package com.sprk.student_management.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ErrorResponseDto<E> {

    private String apiPath;

    private HttpStatus httpStatus;

    private E message;

    private LocalDateTime timestamp;
}
