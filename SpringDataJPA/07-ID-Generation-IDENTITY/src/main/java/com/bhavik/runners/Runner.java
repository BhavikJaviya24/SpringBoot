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
        /*
        Student student = new Student();
        student.setName("CCC");
        student.setPer(56.33);
        student.setGender(Gender.FEMALE);
        student.setBirthDate(LocalDate.now());

        Student savedStudent = studentService.save(student);
        StudentUtil.printStudents(List.of(savedStudent));
         */
        StudentUtil.printStudents(studentService.fetchAll());
    }
}
