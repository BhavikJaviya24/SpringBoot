package com.bhavik.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Data

@Table(name = "student")
public class Student {

    // GenerationType.TABLE
//    @Id
//    @TableGenerator(name = "rno_gen",
//            table = "id_generator",
//            pkColumnName = "gen_name",
//            valueColumnName = "gen_value",
//            pkColumnValue = "student_id",
//            allocationSize = 1
//    )
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "rno_gen")

    // GenerationType.TABLE
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name="rno")
//    private Integer rno;
/*
    for AUTO, it uses SEQUENCE for PostgreSQL

Hibernate:
    create table student (
        rno integer not null,
        birth_date date,
        gender varchar(255) check ((gender in ('MALE','FEMALE','OTHER'))),
        name varchar(255),
        per float(53),
        primary key (rno)
    )

Hibernate:
    create sequence student_seq start with 1 increment by 50
 */

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="rno")
    private UUID rno;
    /*
    Hibernate:
    create table student (
        rno uuid not null,
        birth_date date,
        gender varchar(255) check ((gender in ('MALE','FEMALE','OTHER'))),
        name varchar(255),
        per float(53),
        primary key (rno)
    )

    notice rno column had datatype uuid.
    uuid rno = e63160ab-03e5-4833-a93d-989243dd33aa -> RNOs are generated like this (16-Byte binary data)
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
