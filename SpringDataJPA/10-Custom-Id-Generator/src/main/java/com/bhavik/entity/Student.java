package com.bhavik.entity;

import com.bhavik.generator.RnoId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data

@Table(name = "student")
public class Student {

    @Id
    //new way
    @RnoId(name = "SBAI") // -> custom id generator annotation

    // old way
//    @GenericGenerator(name = "rnoGen", strategy = "com.bhavik.generator.StudentRnoGenerator") <- depricated after hibernate 5
//    @GeneratedValue(generator = "rnoGen")
    private String rno;

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
