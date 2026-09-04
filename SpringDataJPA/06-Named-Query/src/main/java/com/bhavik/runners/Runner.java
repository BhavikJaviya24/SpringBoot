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
        /*
        // find by percentage and gender
        List<Student> students = studentService.fetchAllPerAndGender(70.0, Gender.MALE);
        StudentUtil.printStudents(students);

        // update name
        studentService.modifyName("aaa", 101);

        // find by name and gender
        List<Object[]> students = studentService.fetchByGenderAndName("BBB",  Gender.MALE);
        for (Object[] student : students) {
            System.out.println("Roll no : " + student[0]);
            System.out.println("Name : " + student[1]);
            System.out.println("Gender : " + student[2]);
        }
         */
        // find gender count
        List<Object[]> counts = studentService.fetchGenderCount();
        for (Object[] object : counts) {
            System.out.println("Gender :: " +  object[0]);
            System.out.println("Count :: " +  object[1]);
            System.out.println("------".repeat(6));
        }
    }
}
