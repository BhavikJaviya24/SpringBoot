package com.bhavik.runners;

import com.bhavik.entity.Gender;
import com.bhavik.entity.Student;
import com.bhavik.service.StudentService;
import com.bhavik.util.StudentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.tokens.BlockEndToken;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Runner implements ApplicationRunner {
    @Autowired
    private StudentService studentService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /*
        List<Student> byGenderMale = studentService.fetchByGender(Gender.MALE);
        System.out.println("------".repeat(6) + "MALE STUDENTS" + "------".repeat(6));
        StudentUtil.printStudents(byGenderMale);

        List<Student> byGenderFemale = studentService.fetchByGender(Gender.FEMALE);
        System.out.println("------".repeat(6) + "FEMALE STUDENTS" + "------".repeat(6));
        StudentUtil.printStudents(byGenderFemale);

        List<Student> byBirthDate = studentService.fetchByBirthDate(LocalDate.of(2025, 8, 21));
        System.out.println("------".repeat(6) + "BirthDate STUDENTS" + "------".repeat(6));
        StudentUtil.printStudents(byBirthDate);

        List<Student> byDistinction = studentService.fetchByDistinction();
        System.out.println("------".repeat(6) + "DISTINCTION STUDENTS" + "------".repeat(6));
        StudentUtil.printStudents(byDistinction);

        List<Student> betweenPer = studentService.fetchByPerBetween(58.54, 67.34);
        System.out.println("------".repeat(6) + "STUDENTS BETWEEN PER 50 - 60" + "------".repeat(6));
        StudentUtil.printStudents(betweenPer);

        List<Student> dateAfter = studentService.fetchByDateAfter(LocalDate.of(2025, 7, 30));
        System.out.println("------".repeat(6) + "STUDENTS AFTER DATE 2025-07-30" + "------".repeat(6));
        StudentUtil.printStudents(dateAfter);

        List<Student> dateBefore = studentService.fetchByDateBefore(LocalDate.of(2025, 7, 30));
        System.out.println("------".repeat(6) + "STUDENTS BEFORE DATE 2025-07-30" + "------".repeat(6));
        StudentUtil.printStudents(dateBefore);

        List<Student> perAfter = studentService.fetchByPerAfter(80.00);
        System.out.println("------".repeat(6) + "STUDENTS AFTER PER 80%" + "------".repeat(6));
        StudentUtil.printStudents(perAfter);

        List<Student> perBefore = studentService.fetchByPerBefore(60.00);
        System.out.println("------".repeat(6) + "STUDENTS BEFORE PER 60%" + "------".repeat(6));
        StudentUtil.printStudents(perBefore);

        List<Student> nameStartWith = studentService.fetchByNameStartingWith("a");
        System.out.println("------".repeat(6) + "STUDENTS NAME START WITH a (CASE INSENSITIVE)" + "------".repeat(6));
        StudentUtil.printStudents(nameStartWith);

         */

        List<Student> toppers = studentService.fetchTop3ByPer();
        System.out.println("------".repeat(6) + "TOPPERS" + "------".repeat(6));
        StudentUtil.printStudents(toppers);


    }
}
