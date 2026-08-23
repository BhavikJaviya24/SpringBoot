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

        System.out.println("\n\n======================================================================================\n\n");
        Map<String, Object> student = studentRepository.findById(103);
        System.out.println(student);

        System.out.println("\n\n======================================================================================\n\n");
        List<Map<String, Object>> studentList = studentRepository.findAll();
        System.out.println(studentList);

        System.out.println("\n\n======================================================================================\n\n");
        List<Map<String, Object>> cityStudents = studentRepository.findByCity("Chennai");
        for(Map<String, Object> student2 : cityStudents){
            System.out.println(student2);
        }

        /*
        //  -- Insert --
        Student student = new Student(104, "DDD", 56.34 ,"Mumbai");
        studentRepository.save(student);

        // -- Delete --
        int rowsAffected = studentRepository.delete(101);
        System.out.println(rowsAffected + " rows deleted");

        // -- Update --
        Student updateStudent = new Student();
        updateStudent.setRno(101);
        updateStudent.setName("BBB");
        updateStudent.setPer(84.33);
        updateStudent.setCity("Pune");
        int affectedRows = studentRepository.update(updateStudent);
        System.out.println(affectedRows + " rows updated");
        */
    }

}
