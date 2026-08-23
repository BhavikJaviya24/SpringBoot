package com.bhavik;

import com.bhavik.model.Student;
import com.bhavik.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        StudentRepository studentRepository = (StudentRepository) context.getBean("studentRepository");

        Object ob = studentRepository.findGrade(104);
        System.out.println("Grade : " + ob);

        Object avg = studentRepository.findAverage("Chennai");
        System.out.println("Average : " + avg);
    }

}
