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
import java.util.*;

@Component
public class Runner implements ApplicationRunner {
    @Autowired
    private StudentService studentService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //List<Student> students = studentService.fetchAllStudent();
        //StudentUtil.printStudents(students);

        //List<Student> students = studentService.fetchPercentageRange(20.00, 60.00);
        //StudentUtil.printStudents(students);

        List<Object[]> students = studentService.fetchGenderNamePer(60.0, Gender.MALE);
        for (Object[] student : students) {
            System.out.println("Name : " + student[1].toString());
            System.out.println("Percentage : " + student[2]);
            System.out.println("Gender : " + student[0].toString());
            System.out.println("----".repeat(10));
        }
    }
}
