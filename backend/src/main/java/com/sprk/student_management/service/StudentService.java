package com.sprk.student_management.service;

import com.sprk.student_management.dto.*;

import java.util.List;

public interface StudentService {

    // All the methods name in interface
    // Abstract public
    public StudentResponseDto saveStudent(StudentDto studentDto);

    PageResponse<StudentResponseDto> findAllStudents(StudentPageRequest pageRequest);

    StudentResponseDto findStudentResponseDtoByRollNo(String rollNo);

    List<StudentResponseDto> findAllByGender(String gender);

    void deleteStudent(String rollNo);

    StudentResponseDto updateStudent(String rollNo, StudentDto studentDto);

    EmailCheckResponse checkEmailAvailability(String email, Integer rollNo);


    List<StudentResponseDto> getAllByLocationAndAge(String location, String ageStr);
}
