    package com.sprk.student_management.entity;

    import jakarta.persistence.*;
    import lombok.*;

    // For class we follow PascalCase
    // -> Example StudentInfo
    // -> Table will be created with snake case -> student_info
    @NoArgsConstructor
    @Data
    @Entity
    @AllArgsConstructor
    //@Table(name = "student_sprk_info") now table will be created with this name
    @Builder
    @ToString
    public class Student extends BaseEntity {

        // Since for columns we use camelCase -> rollNo -> column name will be snake_case -> roll_no
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer rollNo;

    //    @Column(name = "user_first_name")
        @Column(nullable = false)
        private String firstName;

        @Column(nullable = false)
        private String lastName;

        @Column(nullable = false, unique = true)
        private String email;

        @Column(nullable = false)
        private Integer age;

        private String gender;

        @Column(nullable = false)
        private String address;

        @Column(nullable = false)
        private Double percentage;

        @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
        @JoinColumn(name = "dept_id")
        private Department department;
    }
