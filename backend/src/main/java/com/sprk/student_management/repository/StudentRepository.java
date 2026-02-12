package com.sprk.student_management.repository;

import com.sprk.student_management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer>, JpaSpecificationExecutor<Student> {
    // No need to write any logic for CRUD

    // Custom
    // JPQM custom query methods
    List<Student> findByGender(String gender);

    boolean existsByEmail(String email);

    boolean existsByEmailAndRollNoNot(String email, Integer rollNo);


    @Query("""
            select s from Student s 
            
            where s.department.location = :location 
            and s.age >= :age 
            """)
    List<Student> findByAgeAndDepartmentLocation(@Param("age") Integer age, @Param("location") String location);
}
