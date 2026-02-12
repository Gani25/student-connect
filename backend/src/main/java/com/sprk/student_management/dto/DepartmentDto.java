package com.sprk.student_management.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {


    private Long deptId;

    @NotBlank(message = "Department Name Cannot Be Empty")
    private String deptName;

    @NotBlank(message = "Department Location Cannot Be Empty")
    private String location;

}
