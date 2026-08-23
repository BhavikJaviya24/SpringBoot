package com.bhavik;

import com.bhavik.model.Student;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("Hello !!");

        Student student = new Student();
        Student student2 = new Student(101, "AAA", "Pune");
        Student student3 = new Student();
        student3.setRno(102);
        student3.setName("BBB");
        student3.setCity("Mumbai");

        System.out.println("Student 1: " + student);
        System.out.println("Student 2: " + student2);
        System.out.println("Student 3: " + student3.getRno() + " " + student3.getName() + " " + student3.getCity());

        Student student4 = new Student(101, "AAA", "Pune");
        System.out.println("Studenr2 is equal to Student4 : " + student2.equals(student4));
    }

}
