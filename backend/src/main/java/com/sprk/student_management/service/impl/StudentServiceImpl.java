package com.sprk.student_management.service.impl;

import com.sprk.student_management.constant.StudentConstants;
import com.sprk.student_management.dto.*;
import com.sprk.student_management.entity.Student;
import com.sprk.student_management.exception.AgeInvalid;
import com.sprk.student_management.exception.EmailAlreadyExists;
import com.sprk.student_management.exception.StudentRollNoMismatch;
import com.sprk.student_management.exception.StudentRollNoNotFound;
import com.sprk.student_management.repository.StudentRepository;
import com.sprk.student_management.service.StudentService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    // Field Injection
    // @Autowired
    private final StudentRepository studentRepository;

    private final EntityManager entityManager;


    private String cleanValue(String value) {
        if (value != null && !value.isBlank()) {
            return value.trim();
        }
        return null;
    }

    @Transactional
    @Override
    public StudentResponseDto saveStudent(StudentDto studentDto) {

        if (studentRepository.existsByEmail(studentDto.getEmail())) {

            throw new EmailAlreadyExists(String.format(StudentConstants.EMAIL_EXISTS, studentDto.getEmail()), HttpStatus.valueOf(StudentConstants.EMAIL_CONFLICT));
        }

        Student student = Student
                .builder()
                .email(cleanValue(studentDto.getEmail()))
                .firstName(cleanValue(studentDto.getFirstName()))
                .lastName(cleanValue(studentDto.getLastName()))
                .age(studentDto.getAge())
                .address(cleanValue(studentDto.getAddress()))
                .percentage(studentDto.getPercentage())
                .rollNo(studentDto.getRollNo())
                .gender(cleanValue(studentDto.getGender()))
                .build();

        studentRepository.save(student);

        StudentResponseDto.StudentResponseDtoBuilder savedStudentResponseDtoBuilder = StudentResponseDto
                .builder()
                .rollNo(student.getRollNo())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .gender(student.getGender())
                .address(student.getAddress())
                .percentage(student.getPercentage())
                .age(student.getAge())
                .email(student.getEmail())
                .createdAt(student.getCreatedAt())
                .createdBy(student.getCreatedBy());

        if (student.getDepartment() == null) {
            savedStudentResponseDtoBuilder
                    .departmentId(null)
                    .departmentName(null)
                    .departmentLocation(null);
        } else {
            savedStudentResponseDtoBuilder
                    .departmentId(student.getDepartment().getDeptId())
                    .departmentName(student.getDepartment().getDeptName())
                    .departmentLocation(student.getDepartment().getLocation());
        }


        return savedStudentResponseDtoBuilder.build();
    }

    @Override
    public PageResponse<StudentResponseDto> findAllStudents(StudentPageRequest request) {
        int page = Math.max(request.getPageno() - 1, 0);
        int size = request.getPagesize() > 0 ? request.getPagesize() : 10;

        Sort sort = Sort.unsorted();
        if (request.getSortcolumn() != null && !request.getSortcolumn().isBlank()) {
            Sort.Direction direction = "desc".equalsIgnoreCase(request.getSortorder())
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            sort = Sort.by(direction, request.getSortcolumn());
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Filter filter = request.getFilter();
        String searchText = filter != null ? filter.getSearchText() : null;
        List<String> genders = filter != null ? filter.getGender() : null;

        Specification<Student> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchText != null && !searchText.isBlank()) {
                String likePattern = "%" + searchText.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("firstName")), likePattern),
                        cb.like(cb.lower(root.get("lastName")), likePattern),
                        cb.like(cb.lower(root.get("email")), likePattern),
                        cb.like(cb.lower(root.get("address")), likePattern)
                ));
            }

            if (genders != null && !genders.isEmpty()) {
                predicates.add(root.get("gender").in(genders));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Student> studentPage = studentRepository.findAll(spec, pageable);

        List<StudentResponseDto> content = studentPage.getContent()
                .stream()
                .map(student -> {
                            StudentResponseDto.StudentResponseDtoBuilder studentResponseDtoBuilder = StudentResponseDto.builder()
                                    .rollNo(student.getRollNo())
                                    .firstName(student.getFirstName())
                                    .lastName(student.getLastName())
                                    .gender(student.getGender())
                                    .address(student.getAddress())
                                    .percentage(student.getPercentage())
                                    .age(student.getAge())
                                    .email(student.getEmail())
                                    .createdAt(student.getCreatedAt())
                                    .createdBy(student.getCreatedBy())
                                    .updatedBy(student.getUpdatedBy())
                                    .updatedAt(student.getUpdatedAt());

                            if (student.getDepartment() == null) {
                                studentResponseDtoBuilder
                                        .departmentId(null)
                                        .departmentName(null)
                                        .departmentLocation(null);
                            } else {
                                studentResponseDtoBuilder
                                        .departmentId(student.getDepartment().getDeptId())
                                        .departmentName(student.getDepartment().getDeptName())
                                        .departmentLocation(student.getDepartment().getLocation());
                            }

                            return studentResponseDtoBuilder.build();
                        }
                )
                .toList();

        PageResponse<StudentResponseDto> pageResponse = new PageResponse<>();
        pageResponse.setContent(content);
        pageResponse.setPageNo(studentPage.getNumber() + 1);
        pageResponse.setPageSize(studentPage.getSize());
        pageResponse.setTotalElements(studentPage.getTotalElements());
        pageResponse.setTotalPages(studentPage.getTotalPages());
        pageResponse.setFirst(studentPage.isFirst());
        pageResponse.setLast(studentPage.isLast());

        return pageResponse;
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

    @Transactional
    @Override
    public StudentResponseDto findStudentResponseDtoByRollNo(String rollNo) {
        Student existingStudent = findStudentByRollNo(rollNo);

        StudentResponseDto.StudentResponseDtoBuilder studentResponseDtoBuilder = StudentResponseDto
                .builder()
                .rollNo(existingStudent.getRollNo())
                .firstName(existingStudent.getFirstName())
                .lastName(existingStudent.getLastName())
                .gender(existingStudent.getGender())
                .address(existingStudent.getAddress())
                .percentage(existingStudent.getPercentage())
                .age(existingStudent.getAge())
                .email(existingStudent.getEmail())
                .createdAt(existingStudent.getCreatedAt())
                .createdBy(existingStudent.getCreatedBy())
                .updatedBy(existingStudent.getUpdatedBy())
                .updatedAt(existingStudent.getUpdatedAt());

        if (existingStudent.getDepartment() == null) {
            studentResponseDtoBuilder
                    .departmentId(null)
                    .departmentName(null)
                    .departmentLocation(null);
        } else {
            studentResponseDtoBuilder
                    .departmentId(existingStudent.getDepartment().getDeptId())
                    .departmentName(existingStudent.getDepartment().getDeptName())
                    .departmentLocation(existingStudent.getDepartment().getLocation());
        }
        return studentResponseDtoBuilder.build();
    }

    @Transactional
    @Override
    public List<StudentResponseDto> findAllByGender(String gender) {
        List<Student> studentList = studentRepository.findByGender(gender);
        return studentList
                .stream()
                .map(student -> {
                            StudentResponseDto.StudentResponseDtoBuilder studentResponseDtoBuilder = StudentResponseDto
                                    .builder()
                                    .rollNo(student.getRollNo())
                                    .firstName(student.getFirstName())
                                    .lastName(student.getLastName())
                                    .gender(student.getGender())
                                    .address(student.getAddress())
                                    .percentage(student.getPercentage())
                                    .age(student.getAge())
                                    .email(student.getEmail())
                                    .departmentId(student.getDepartment().getDeptId())
                                    .departmentName(student.getDepartment().getDeptName())
                                    .departmentLocation(student.getDepartment().getLocation())
                                    .createdAt(student.getCreatedAt())
                                    .createdBy(student.getCreatedBy())
                                    .updatedBy(student.getUpdatedBy())
                                    .updatedAt(student.getUpdatedAt());

                            if (student.getDepartment() == null) {
                                studentResponseDtoBuilder
                                        .departmentId(null)
                                        .departmentName(null)
                                        .departmentLocation(null);
                            } else {
                                studentResponseDtoBuilder
                                        .departmentId(student.getDepartment().getDeptId())
                                        .departmentName(student.getDepartment().getDeptName())
                                        .departmentLocation(student.getDepartment().getLocation());
                            }

                            return studentResponseDtoBuilder.build();
                        }
                ).toList();
    }

    @Transactional
    @Override
    public void deleteStudent(String rollNo) {
        Student existingStudent = findStudentByRollNo(rollNo);

        studentRepository.delete(existingStudent);

    }

    @Transactional
    @Override
    public StudentResponseDto updateStudent(String rollNo, StudentDto studentDto) {

        Student existingStudent = findStudentByRollNo(rollNo);

        if (studentDto.getEmail() != null && !studentDto.getEmail().isBlank()) {
            String newEmail = cleanValue(studentDto.getEmail());

            if (!newEmail.equalsIgnoreCase(existingStudent.getEmail())) {
                if (studentRepository.existsByEmailAndRollNoNot(newEmail, existingStudent.getRollNo())) {
                    throw new EmailAlreadyExists(String.format(StudentConstants.EMAIL_EXISTS, studentDto.getEmail()), HttpStatus.valueOf(StudentConstants.EMAIL_CONFLICT));

                }
            }

            existingStudent.setEmail(newEmail);
        }


        if (studentDto.getFirstName() != null && !studentDto.getFirstName().isBlank()) {
            existingStudent.setFirstName(cleanValue(studentDto.getFirstName()));
        }
        if (studentDto.getLastName() != null && !studentDto.getLastName().isBlank()) {
            existingStudent.setLastName(cleanValue(studentDto.getLastName()));
        }
        if (studentDto.getGender() != null && !studentDto.getGender().isBlank()) {
            existingStudent.setGender(cleanValue(studentDto.getGender()));
        }
        if (studentDto.getAddress() != null && !studentDto.getAddress().isBlank()) {
            existingStudent.setAddress(cleanValue(studentDto.getAddress()));
        }
        if (studentDto.getPercentage() != null) {
            existingStudent.setPercentage(studentDto.getPercentage());
        }
        if (studentDto.getAge() != null) {
            existingStudent.setAge(studentDto.getAge());
        }


        studentRepository.save(existingStudent);

        entityManager.flush();
        entityManager.refresh(existingStudent);


        StudentResponseDto.StudentResponseDtoBuilder studentResponseDtoBuilder = StudentResponseDto
                .builder()
                .rollNo(existingStudent.getRollNo())
                .firstName(existingStudent.getFirstName())
                .lastName(existingStudent.getLastName())
                .gender(existingStudent.getGender())
                .address(existingStudent.getAddress())
                .percentage(existingStudent.getPercentage())
                .age(existingStudent.getAge())
                .email(existingStudent.getEmail())
                .createdAt(existingStudent.getCreatedAt())
                .createdBy(existingStudent.getCreatedBy())
                .updatedBy(existingStudent.getUpdatedBy())
                .updatedAt(existingStudent.getUpdatedAt());

        if (existingStudent.getDepartment() == null) {
            studentResponseDtoBuilder
                    .departmentId(null)
                    .departmentName(null)
                    .departmentLocation(null);
        } else {
            studentResponseDtoBuilder
                    .departmentId(existingStudent.getDepartment().getDeptId())
                    .departmentName(existingStudent.getDepartment().getDeptName())
                    .departmentLocation(existingStudent.getDepartment().getLocation());
        }
        return studentResponseDtoBuilder.build();

    }

    @Transactional
    @Override
    public EmailCheckResponse checkEmailAvailability(String email, Integer rollNo) {


        if (rollNo == null || rollNo == 0) {
            boolean exists = studentRepository.existsByEmailAndRollNoNot(email, 0);

            if (exists) {
                return new EmailCheckResponse(false, "Email already registered");
            } else {
                return new EmailCheckResponse(true, "Email available");
            }
        }


        boolean exists = studentRepository.existsByEmailAndRollNoNot(email, rollNo);

        if (exists) {
            return new EmailCheckResponse(false, "Email already registered to another student");
        }

        return new EmailCheckResponse(true, "Email available");
    }

    @Override
    public List<StudentResponseDto> getAllByLocationAndAge(String location, String ageStr) {
        Integer age = null;

        if (!Pattern.matches("^[\\d]+$", ageStr)) {

            throw new AgeInvalid("Enter Age In Integer Only", HttpStatus.BAD_REQUEST);
        }

        age = Integer.parseInt(ageStr);

        List<Student> students = studentRepository.findByAgeAndDepartmentLocation(age,location);

        return students.stream().map(student -> {
            StudentResponseDto.StudentResponseDtoBuilder studentResponseDtoBuilder = StudentResponseDto.builder()
                    .rollNo(student.getRollNo())
                    .firstName(student.getFirstName())
                    .lastName(student.getLastName())
                    .gender(student.getGender())
                    .address(student.getAddress())
                    .percentage(student.getPercentage())
                    .age(student.getAge())
                    .email(student.getEmail())
                    .createdAt(student.getCreatedAt())
                    .createdBy(student.getCreatedBy())
                    .updatedBy(student.getUpdatedBy())
                    .updatedAt(student.getUpdatedAt());

            if (student.getDepartment() == null) {
                studentResponseDtoBuilder
                        .departmentId(null)
                        .departmentName(null)
                        .departmentLocation(null);
            } else {
                studentResponseDtoBuilder
                        .departmentId(student.getDepartment().getDeptId())
                        .departmentName(student.getDepartment().getDeptName())
                        .departmentLocation(student.getDepartment().getLocation());
            }

            return studentResponseDtoBuilder.build();
        }).toList();


    }


}
