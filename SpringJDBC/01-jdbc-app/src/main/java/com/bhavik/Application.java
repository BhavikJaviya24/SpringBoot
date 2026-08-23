package com.bhavik;

import com.bhavik.model.Student;
import com.bhavik.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

    /*
    @Autowired
    static StudentRepository studentRepository;

    static members can't be autowired.
     */
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        StudentRepository studentRepository = (StudentRepository) context.getBean("studentRepository");

        /*  -- Insert --
        Student student = new Student(101, "AAA", 84.33, "Pune");
        studentRepository.save(student);
        */

        /*  -- Delete --
        int rowsAffected = studentRepository.delete(101);
        System.out.println(rowsAffected + " rows deleted");
        */

        /* -- Update -- */
        Student updateStudent = new Student();
        updateStudent.setRno(101);
        updateStudent.setName("BBB");
        updateStudent.setPer(84.33);
        updateStudent.setCity("Pune");
        int affectedRows = studentRepository.update(updateStudent);
        System.out.println(affectedRows + " rows updated");
    }

}
