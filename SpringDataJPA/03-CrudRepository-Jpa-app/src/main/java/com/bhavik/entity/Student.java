package com.bhavik.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "student")
public class Student {

    @Id
    @Column(name="rno")
    private Integer rno;

    @Column(name="name")
    private String name;

    @Column(name="per")
    private Double per;

    @Column(name="gender")
    private Gender gender;
}
