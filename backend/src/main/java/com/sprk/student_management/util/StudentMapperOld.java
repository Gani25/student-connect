package com.sprk.student_management.util;

import com.sprk.student_management.dto.StudentDto;
import com.sprk.student_management.entity.Student;

public class StudentMapperOld {

    public static Student mappedStudentDtoToStudent(StudentDto studentDto) {
        Student student = new Student();
        student.setRollNo(studentDto.getRollNo());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setGender(studentDto.getGender());
        student.setAge(studentDto.getAge());
        student.setAddress(studentDto.getAddress());
        student.setPercentage(studentDto.getPercentage());
        student.setEmail(studentDto.getEmail());
        return student;
    }

    public static StudentDto mappedStudentToStudentDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setRollNo(student.getRollNo());
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());
        studentDto.setGender(student.getGender());
        studentDto.setAge(student.getAge());
        studentDto.setAddress(student.getAddress());
        studentDto.setPercentage(student.getPercentage());
        studentDto.setEmail(student.getEmail());
        return studentDto;

    }
}
