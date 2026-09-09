package com.bhavik.runners;

import com.bhavik.entity.Gender;
import com.bhavik.entity.Student;
import com.bhavik.service.StudentService;
import com.bhavik.util.StudentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


@Component
public class Runner implements ApplicationRunner {
    @Autowired
    private StudentService studentService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /* saving one student
        Student student = new Student();
        student.setName("BBB");
        student.setPer(78.45);
        student.setGender(Gender.MALE);
        student.setBirthDate(LocalDate.now());

        Student savedStudent = studentService.save(student);
        StudentUtil.printStudents(List.of(savedStudent));
        */

        Student s1 = new Student();
        s1.setName("FFF");
        s1.setPer(34.67);
        s1.setGender(Gender.MALE);
        s1.setBirthDate(LocalDate.now());

        Student s2 = new Student();
        s2.setName("GGG");
        s2.setPer(89.32);
        s2.setGender(Gender.MALE);
        s2.setBirthDate(LocalDate.now());

        Student s3 = new Student();
        s3.setName("HHH");
        s3.setPer(90.34);
        s3.setGender(Gender.FEMALE);
        s3.setBirthDate(LocalDate.now());

        Student s4 = new Student();
        s4.setName("III");
        s4.setPer(73.45);
        s4.setGender(Gender.FEMALE);
        s4.setBirthDate(LocalDate.now());

        Student s5 = new Student();
        s5.setName("JJJ");
        s5.setPer(78.11);
        s5.setGender(Gender.MALE);
        s5.setBirthDate(LocalDate.now());

        List<Student> savedStudents = studentService.saveAllStudents(List.of(s1, s2, s3, s4, s5));
        StudentUtil.printStudents(savedStudents);
    }
}
