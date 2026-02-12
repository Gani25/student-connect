package com.sprk.student_management.controller;

import com.sprk.student_management.constant.StudentConstants;
import com.sprk.student_management.dto.*;
import com.sprk.student_management.entity.Student;
import com.sprk.student_management.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "CRUD APIs for Student of SPRK Technologies",
        description = "REST apis of Student in SPRK Technologies like GET, POST, PUT, DELETE"
)
@RequestMapping("/api/v1")
public class StudentController {

    // Injecting Service
    private final StudentService studentService;

    // Insert student
    @Operation(
            summary = "Create Student Rest API",
            description = "API to register new student."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Http Status Created",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email Already Register",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
    })
    @PostMapping("/student")
    public ResponseEntity<ResponseDto<StudentResponseDto>> addStudent(@Valid @RequestBody StudentDto studentDto) {
        // Service Call
        System.out.println(studentDto);
        StudentResponseDto savedStudentDto = studentService.saveStudent(studentDto);
        ResponseDto<StudentResponseDto> responseDto = new ResponseDto<>();
        responseDto.setStatusCode(StudentConstants.STUDENT_CREATED);
        responseDto.setMessage(String.format(StudentConstants.STUDENT_CREATED_MSG,savedStudentDto.getRollNo()));
        responseDto.setData(savedStudentDto);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getStatusCode())).body(responseDto);
    }

    // Find All Students
    @Operation(
            summary = "Get All Students Rest API",
            description = "API to get all student available in Database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Http Status Success",
                    content = @Content(
                            schema = @Schema(implementation = PageResponse.class)
                    )
            ),
    })
    @PostMapping("/students")
    public ResponseEntity<ResponseDto<PageResponse<StudentResponseDto>>>  getAllStudents(@RequestBody StudentPageRequest studentPageRequest) {
        PageResponse<StudentResponseDto> studentResponseDtos = studentService.findAllStudents(studentPageRequest);
        ResponseDto<PageResponse<StudentResponseDto>> responseDto = new ResponseDto<>();
        responseDto.setStatusCode(StudentConstants.SUCCESS);
        responseDto.setMessage(StudentConstants.GET_ALL_STUDENT);
        responseDto.setData(studentResponseDtos);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getStatusCode())).body(responseDto);
    }

    @GetMapping("/student/{rollNo}")
    public ResponseEntity<ResponseDto<StudentResponseDto>>  getStudentByRollNo(@PathVariable String rollNo) {
        StudentResponseDto existingStudentResponseDto = studentService.findStudentResponseDtoByRollNo(rollNo);

        ResponseDto<StudentResponseDto> responseDto = new ResponseDto<>();
        responseDto.setStatusCode(StudentConstants.SUCCESS);
        responseDto.setData(existingStudentResponseDto);
        responseDto.setMessage(String.format(StudentConstants.GET_STUDENT_BY_ROLL_NO,rollNo));



        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getStatusCode())).body(responseDto);
    }

    @GetMapping("/student/email-available/{email}/{rollNo}")
    public ResponseEntity<EmailCheckResponse> checkEmail(@PathVariable String email, @PathVariable String rollNo) {

        Integer rollNoInteger = 0;
        if(Pattern.matches("\\d+",rollNo)){

            rollNoInteger = Integer.parseInt(rollNo);

        }

        EmailCheckResponse resp =
                studentService.checkEmailAvailability(email, rollNoInteger);

        return ResponseEntity.ok(resp);
    }


    @GetMapping("/student/gender")
    public ResponseEntity<ResponseDto<List<StudentResponseDto>>> getAllByGender(@RequestParam String gender) {

        List<StudentResponseDto> studentResponseDtos = studentService.findAllByGender(gender);
        ResponseDto<List<StudentResponseDto>> responseDto = new ResponseDto<>();
        responseDto.setStatusCode(StudentConstants.SUCCESS);
        responseDto.setData(studentResponseDtos);
        responseDto.setMessage(String.format(StudentConstants.GET_STUDENT_BY_GENDER, gender));


        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getStatusCode())).body(responseDto);
    }


    @DeleteMapping("/student")
    public ResponseEntity<ResponseDto<String>> deleteStudent(@RequestParam String rollNo) {
        // Service
        studentService.deleteStudent(rollNo);


            String msg = String.format(StudentConstants.DELETE_STUDENT_MSG, rollNo);
            ResponseDto<String> responseDto = new ResponseDto<>();
            responseDto.setStatusCode(StudentConstants.SUCCESS);
            responseDto.setMessage(msg);
            responseDto.setData(null);
            return ResponseEntity.status(HttpStatus.valueOf(responseDto.getStatusCode())).body(responseDto);


    }

    @PutMapping("/student")
    public ResponseEntity<ResponseDto<StudentResponseDto>> updateStudent(@RequestParam String rollNo, @RequestBody StudentDto studentDto) {

        StudentResponseDto updatedStudentResponseDto = studentService.updateStudent(rollNo, studentDto);
        ResponseDto<StudentResponseDto> responseDto = new ResponseDto<>();
        responseDto.setStatusCode(StudentConstants.SUCCESS);
        responseDto.setMessage(String.format(StudentConstants.UPDATE_STUDENT_MSG, rollNo));
        responseDto.setData(updatedStudentResponseDto);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getStatusCode())).body(responseDto);
    }
    @GetMapping("/student/location/age")
    public ResponseEntity<ResponseDto<List<StudentResponseDto>>> getAllByLocationAndAge(@RequestParam String location, @RequestParam("age") String ageStr) {

        List<StudentResponseDto> studentResponseDtos = studentService.getAllByLocationAndAge(location,ageStr);
        ResponseDto<List<StudentResponseDto>> responseDto = new ResponseDto<>();
        responseDto.setStatusCode(StudentConstants.SUCCESS);
        responseDto.setData(studentResponseDtos);
        responseDto.setMessage(String.format("Fetching All Students Based on Department Location %s and Age greater than %s",location,ageStr));
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getStatusCode())).body(responseDto);
    }

}

















