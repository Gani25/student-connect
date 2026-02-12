package com.sprk.student_management.controller;

import com.sprk.student_management.constant.DepartmentConstant;
import com.sprk.student_management.constant.StudentConstants;
import com.sprk.student_management.dto.*;
import com.sprk.student_management.entity.Department;
import com.sprk.student_management.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/department")
    public ResponseEntity<ResponseDto<DepartmentResponseDto>> addDepartment(@Valid @RequestBody DepartmentDto departmentDto){

        DepartmentResponseDto departmentResponseDto = departmentService.saveDepartment(departmentDto);

        ResponseDto<DepartmentResponseDto> responseDto = new ResponseDto<>();
        responseDto.setData(departmentResponseDto);
        responseDto.setMessage(
                String.format(DepartmentConstant.DEPARTMENT_CREATED_MSG, departmentResponseDto.getDeptName(), departmentResponseDto.getLocation())
        );

        responseDto.setStatusCode(DepartmentConstant.DEPARTMENT_CREATED);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getStatusCode())).body(responseDto);
    }

    @GetMapping("/department")
    public ResponseEntity<ResponseDto<List<DepartmentResponseDto>>>  getAllDepartments() {
        List<DepartmentResponseDto> allDepartments = departmentService.getAllDepartments();
        ResponseDto<List<DepartmentResponseDto>> responseDto = new ResponseDto<>();
        responseDto.setStatusCode(DepartmentConstant.SUCCESS);
        responseDto.setMessage(DepartmentConstant.GET_ALL_DEPARTMENTS);
        responseDto.setData(allDepartments);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getStatusCode())).body(responseDto);
    }

    @GetMapping("/department/find")
    public ResponseEntity<ResponseDto<DepartmentResponseDto>>  getDepartmentById(@RequestParam("deptId") String deptIdStr) {
        DepartmentResponseDto departmentResponseDto = departmentService.getDepartmentById(deptIdStr);
        ResponseDto<DepartmentResponseDto> responseDto = new ResponseDto<>();
        responseDto.setStatusCode(DepartmentConstant.SUCCESS);
        responseDto.setMessage(DepartmentConstant.GET_ALL_DEPARTMENTS);
        responseDto.setData(departmentResponseDto);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getStatusCode())).body(responseDto);
    }

    @PutMapping("/department")
    public ResponseEntity<ResponseDto<DepartmentResponseDto>> updateDepartment(@Valid @RequestBody DepartmentDto departmentDto, @RequestParam("deptId") String deptIdStr){

        DepartmentResponseDto departmentResponseDto = departmentService.updateDepartment(deptIdStr, departmentDto);

        ResponseDto<DepartmentResponseDto> responseDto = ResponseDto.<DepartmentResponseDto>builder()
                .message(String.format(DepartmentConstant.UPDATE_DEPARTMENT_MSG, deptIdStr))
                .statusCode(DepartmentConstant.SUCCESS)
                .data(departmentResponseDto)
                .build();
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getStatusCode())).body(responseDto);
    }

    @DeleteMapping("/department")
    public ResponseEntity<ResponseDto<?>> deleteDepartment( @RequestParam("deptId") String deptIdStr) {

        departmentService.deleteDepartment(deptIdStr);

        ResponseDto<?> responseDto = ResponseDto.builder()
                .message(String.format(DepartmentConstant.DELETE_DEPARTMENT_MSG,deptIdStr))
                .statusCode(DepartmentConstant.SUCCESS)
                .build();

        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getStatusCode())).body(responseDto);
    }





    @PutMapping("/students/{rollNo}/department/{deptId}")
    public ResponseEntity<ResponseDto<StudentResponseDto>> assignDepartment(@PathVariable("rollNo") String rollNoStr, @PathVariable("deptId") String deptIdStr){

        StudentResponseDto studentResponseDto = departmentService.assignDepartment(rollNoStr,deptIdStr);

        ResponseDto<StudentResponseDto> responseDto = ResponseDto.<StudentResponseDto>builder()
                .statusCode(DepartmentConstant.SUCCESS)
                .message(DepartmentConstant.ASSIGN_DEPARTMENT_TO_STUDENT)
                .data(studentResponseDto)
                .build();
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getStatusCode())).body(responseDto);
    }
}
