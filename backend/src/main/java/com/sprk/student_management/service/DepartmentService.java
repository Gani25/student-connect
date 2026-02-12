package com.sprk.student_management.service;

import com.sprk.student_management.dto.DepartmentDto;
import com.sprk.student_management.dto.DepartmentResponseDto;
import com.sprk.student_management.dto.StudentResponseDto;

import java.util.List;

public interface DepartmentService {
    DepartmentResponseDto saveDepartment(DepartmentDto departmentDto);

    List<DepartmentResponseDto> getAllDepartments();

    DepartmentResponseDto updateDepartment(String deptIdStr, DepartmentDto departmentDto);

    StudentResponseDto assignDepartment(String rollNoStr, String deptIdStr);

    void deleteDepartment(String deptIdStr);

    DepartmentResponseDto getDepartmentById(String deptIdStr);
}
