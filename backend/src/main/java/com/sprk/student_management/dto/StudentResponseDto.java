package com.sprk.student_management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "StudentResponse",
        description = "Schema to hold information of Student"
)
@ToString
@Builder
public class StudentResponseDto {

    @Schema(
            description = "Hold roll number of student",
            example = "10"
    )
    private Integer rollNo;

    @Schema(
            description = "Hold first name of student",
            example = "Abdul Gani"
    )
    private String firstName;

    @Schema(
            description = "Hold last name of student",
            example = "Memon"
    )
    private String lastName;


    @Schema(
            description = "Hold email of student",
            example = "demo12@gmail.com"
    )
    private String email;


    @Schema(
            description = "Hold age of student",
            example = "26"
    )
    private Integer age;

    @Schema(
            description = "Hold gender of student",
            example = "Male"
    )
    private String gender;


    @Schema(
            description = "Hold address of student",
            example = "Kharghar"
    )
    private String address;


    @Schema(
            description = "Hold percentage of student",
            example = "85.55"
    )
    private Double percentage;

    @Schema(
            description = "Hold id of department",
            example = "1"
    )
    private Long departmentId;

    @Schema(
            description = "Hold department name",
            example = "Computers"
    )
    private String departmentName;

    @Schema(
            description = "Hold department location",
            example = "Dadar, Mumbai"
    )
    private String departmentLocation;




    private LocalDateTime createdAt;


    private String createdBy;


    private LocalDateTime updatedAt;


    private String updatedBy;
}
