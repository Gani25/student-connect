package com.sprk.student_management.service.impl;

import com.sprk.student_management.constant.DepartmentConstant;
import com.sprk.student_management.dto.DepartmentDto;
import com.sprk.student_management.dto.DepartmentResponseDto;
import com.sprk.student_management.dto.StudentResponseDto;
import com.sprk.student_management.entity.Department;
import com.sprk.student_management.entity.Student;
import com.sprk.student_management.exception.StudentRollNoMismatch;
import com.sprk.student_management.exception.StudentRollNoNotFound;
import com.sprk.student_management.exception.department.DepartmentAlreadyExists;
import com.sprk.student_management.exception.department.DepartmentIdMismatch;
import com.sprk.student_management.exception.department.DepartmentNotFound;
import com.sprk.student_management.repository.DepartmentRepository;
import com.sprk.student_management.repository.StudentRepository;
import com.sprk.student_management.service.DepartmentService;
import com.sprk.student_management.service.StudentService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final StudentRepository studentRepository;


    // Since after update not getting updatedAt and By so with entity manager we will refresh to get updated object
    private final EntityManager entityManager;

    @Override
    @Transactional
    public DepartmentResponseDto saveDepartment(DepartmentDto departmentDto) {

        if (departmentRepository.existsByDeptNameAndLocation(departmentDto.getDeptName(), departmentDto.getLocation())) {
            throw new DepartmentAlreadyExists(
                    String.format(DepartmentConstant.DEPARTMENT_EXISTS, departmentDto.getDeptName(), departmentDto.getLocation()),
                    HttpStatus.valueOf(DepartmentConstant.DEPARTMENT_CONFLICT)
            );
        }

        Department department = Department.builder()
                .deptName(departmentDto.getDeptName())
                .location(departmentDto.getLocation())
                .build();

        Department savedDepartment = departmentRepository.save(department);

        DepartmentResponseDto departmentResponseDto = DepartmentResponseDto.builder()
                .deptName(savedDepartment.getDeptName())
                .location(savedDepartment.getLocation())
                .deptId(savedDepartment.getDeptId())
                .createdAt(department.getCreatedAt())
                .createdBy(savedDepartment.getCreatedBy())
                .build();


        return departmentResponseDto;
    }

    @Override
    public List<DepartmentResponseDto> getAllDepartments() {
        return departmentRepository.findAll().stream().map((department -> {
            return DepartmentResponseDto.builder()
                    .deptName(department.getDeptName())
                    .location(department.getLocation())
                    .deptId(department.getDeptId())
                    .createdAt(department.getCreatedAt())
                    .createdBy(department.getCreatedBy())
                    .updatedAt(department.getUpdatedAt())
                    .updatedBy(department.getUpdatedBy())
                    .build();
        })).toList();
    }

    private Department findDepartmentByDeptId(String deptIdStr) {

        if (!Pattern.matches("^[\\d]+$", deptIdStr)) {

            throw new DepartmentIdMismatch("Enter Department Id In Integer Only", HttpStatus.BAD_REQUEST);
        }
        Long deptId = Long.parseLong(deptIdStr);

        return departmentRepository
                .findById(deptId)
                .orElseThrow(() -> {
                    return new DepartmentNotFound(String.format("Department With id = %d Not Found", deptId), HttpStatus.NOT_FOUND);
                });
    }

    @Transactional
    @Override
    public DepartmentResponseDto updateDepartment(String deptIdStr, DepartmentDto departmentDto) {

        Department existingDepartment = findDepartmentByDeptId(deptIdStr);


        if (departmentDto.getDeptName() != null && !departmentDto.getDeptName().isBlank()) {
            existingDepartment.setDeptName(departmentDto.getDeptName().trim());
        }
        if (departmentDto.getLocation() != null && !departmentDto.getLocation().isBlank()) {
            existingDepartment.setLocation(departmentDto.getLocation().trim());
        }


        departmentRepository.save(existingDepartment);

        entityManager.flush();
        entityManager.refresh(existingDepartment);


        return DepartmentResponseDto.builder()
                .deptName(existingDepartment.getDeptName())
                .location(existingDepartment.getLocation())
                .deptId(existingDepartment.getDeptId())
                .createdAt(existingDepartment.getCreatedAt())
                .createdBy(existingDepartment.getCreatedBy())
                .updatedAt(existingDepartment.getUpdatedAt())
                .updatedBy(existingDepartment.getUpdatedBy())
                .build();
    }

    private Student findStudentByRollNo(String rollNo) {

        if (!Pattern.matches("^[\\d]+$", rollNo)) {

            throw new StudentRollNoMismatch("Enter Roll No In Integer Only", HttpStatus.BAD_REQUEST);
        }
        int rollNoInt = Integer.parseInt(rollNo);

        return studentRepository
                .findById(rollNoInt)
                .orElseThrow(() -> {
                    return new StudentRollNoNotFound(String.format("Student With Roll No = %d Not Found", rollNoInt), HttpStatus.NOT_FOUND);
                });
    }


    @Override
    public StudentResponseDto assignDepartment(String rollNoStr, String deptIdStr) {
        Student student = findStudentByRollNo(rollNoStr);

        Department department = findDepartmentByDeptId(deptIdStr);

        student.setDepartment(department);

        studentRepository.save(student);
        

        StudentResponseDto.StudentResponseDtoBuilder studentResponseDtoBuilder = StudentResponseDto.builder()
                .rollNo(student.getRollNo())
                .age(student.getAge())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .gender(student.getGender())
                .address(student.getAddress())
                .percentage(student.getPercentage())
                .departmentId(student.getDepartment().getDeptId())
                .departmentName(student.getDepartment().getDeptName())
                .departmentLocation(student.getDepartment().getLocation())
                .createdAt(student.getCreatedAt())
                .createdBy(student.getCreatedBy())
                .updatedAt(student.getUpdatedAt())
                .updatedBy(student.getUpdatedBy());

        return studentResponseDtoBuilder.build();
    }

    @Override
    public void deleteDepartment(String deptIdStr) {
        Department department = findDepartmentByDeptId(deptIdStr);

        System.out.println("Department");
        System.out.println(department.getStudents().get(0).getFirstName());
        List<Student> students = department.getStudents();

        students.forEach((student -> student.setDepartment(null)));

        departmentRepository.delete(department);

    }

    @Override
    public DepartmentResponseDto getDepartmentById(String deptIdStr) {
        Department existingDepartment = findDepartmentByDeptId(deptIdStr);

        return DepartmentResponseDto.builder()
                .deptName(existingDepartment.getDeptName())
                .location(existingDepartment.getLocation())
                .deptId(existingDepartment.getDeptId())
                .createdAt(existingDepartment.getCreatedAt())
                .createdBy(existingDepartment.getCreatedBy())
                .updatedAt(existingDepartment.getUpdatedAt())
                .updatedBy(existingDepartment.getUpdatedBy())
                .build();
    }
}
