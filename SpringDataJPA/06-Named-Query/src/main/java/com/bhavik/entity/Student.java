package com.bhavik.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@NamedQuery(name = "Student.findAllPerAndGender", query = "select s from Student s where per>=:per and gender=:gender")
@NamedQueries(value={
        @NamedQuery(name = "Student.updateName", query = "update Student s set s.name=:name where s.rno=:rno"),
        @NamedQuery(name = "Student.findByGenderAndName", query = "select s.rno, s.name, s.gender from Student s where s.name=:name and s.gender=:gender"),
        @NamedQuery(name = "Student.findGenderCount", query = "select s.gender, count(s) from Student s group by s.gender")
})
@Table(name = "student")
public class Student {

    @Id
    @Column(name="rno")
    private Integer rno;

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
