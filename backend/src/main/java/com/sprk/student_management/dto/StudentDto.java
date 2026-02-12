package com.sprk.student_management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Student",
        description = "Schema to hold information of Student"
)
@ToString
@Builder
public class StudentDto {

    @Schema(
            description = "Hold roll number of student",
            example = "10"
    )
    private Integer rollNo;

    @NotBlank(message = "First Name Cannot Have Empty Space or Blank")
    @Schema(
            description = "Hold first name of student",
            example = "Abdul Gani"
    )
    private String firstName;

    @NotBlank(message = "Last Name Cannot Have Empty Space or Blank")
    @Schema(
            description = "Hold last name of student",
            example = "Memon"
    )
    private String lastName;

    @Email(message = "Please enter correct email")
    @NotBlank(message = "Email Cannot Be Empty")
    @Schema(
            description = "Hold email of student",
            example = "demo12@gmail.com"
    )
    private String email;

    @Min(value = 5, message = "Ensure age must be greater than 5")
    @Max(value = 200, message = "Ensure age must be below 200")
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


    @NotBlank(message = "Address Cannot Be Empty")
    @Schema(
            description = "Hold address of student",
            example = "Kharghar"
    )
    private String address;

    @Min(value = 0, message = "Percentage should be between 0 to 100")
    @Max(value = 100, message = "Percentage should be between 0 to 100")
    @Schema(
            description = "Hold percentage of student",
            example = "85.55"
    )
    private Double percentage;
}
