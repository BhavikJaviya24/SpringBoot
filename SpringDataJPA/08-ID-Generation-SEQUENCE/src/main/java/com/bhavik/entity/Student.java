package com.bhavik.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data

@Table(name = "student")
public class Student {
/*
    // to save single record
    @Id
    @SequenceGenerator(name = "rno_gen",    // app-side generator name
            sequenceName = "student_seq",   // db-side sequence name
            allocationSize = 1  )       // must match or less than 'increment by' in sequence at db
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rno_gen")
    @Column(name="rno")
    private Integer rno;

 */

    // to save multiple records at a time
    @Id
    @SequenceGenerator(name = "rno_gen",    // app-side generator name
        sequenceName = "student_seq2",   // db-side sequence name
        allocationSize = 5 )       // hibernate will generate 5 values, at db side 'increment by' must be 5 or more
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rno_gen")
    @Column(name="rno")
    private Integer rno;

    /*
     batch    |  INCREMENT BY = 5  | allocationSize = 5
   -----------------------------------------------------
    1st batch |        101         |         97
              |                    |         98
              |                    |         99
              |                    |         100
              |                    |         101
   -----------------------------------------------------
    2nd batch |        106         |         102
              |                    |         103
              |                    |         104
              |                    |         105
              |                    |         106
   -----------------------------------------------------
     */

    @Column(name="name")
    private String name;

    @Column(name="per")
    private Double per;

    @Enumerated(EnumType.STRING)
    @Column(name="gender")
    private Gender gender;

    @Column(name="birth_date")
    private LocalDate birthDate;

}
