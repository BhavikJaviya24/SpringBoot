package com.bhavik.runner;

import com.bhavik.entity.Gender;
import com.bhavik.entity.Student;
import com.bhavik.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class Runner implements ApplicationRunner {
    @Autowired
    private StudentService studentService;

    private static void printStudents(List<Student> students){
        if(!students.isEmpty()){
            for(Student student : students){
                System.out.println("Roll no.: " + student.getRno());
                System.out.println("Name : " + student.getName());
                System.out.println("Percentage : " + student.getPer());
                System.out.println("Gender : " + student.getGender());
                System.out.println("Birth Date : " + student.getBirthDate());
                System.out.println("----".repeat(10));
            }
        }
        else {
            System.out.println("No Data!!");
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /*
        Student s1 = new Student(101, "AAA", 25.67, Gender.FEMALE, LocalDate.of(2025, 8, 21));
        Student s2 = new Student(106, "FFF", 27.89, Gender.MALE, LocalDate.of(2025, 8, 26));
        Student s3 = new Student(107, "GGG", 19.34, Gender.FEMALE, LocalDate.of(2025, 7, 23));

        List<Student> students = studentService.saveAllStudent(List.of(s1,s2,s3));
        Runner.printStudents(students);

         */

        List<Student> students = studentService.saveAllStudent(List.of(
                new Student(101, "AAA", 25.67, Gender.FEMALE, LocalDate.of(2025, 8, 21)),
                new Student(106, "FFF", 27.89, Gender.MALE, LocalDate.of(2025, 8, 26)),
                new Student(107, "GGG", 19.34, Gender.FEMALE, LocalDate.of(2025, 7, 23))));
        Runner.printStudents(students);

        /*
        Runner.printStudents(studentService.saveAllStudent(List.of(
                new Student(101, "AAA", 25.67, Gender.FEMALE, LocalDate.of(2025, 8, 21)),
                new Student(106, "FFF", 27.89, Gender.MALE, LocalDate.of(2025, 8, 26)),
                new Student(107, "GGG", 19.34, Gender.FEMALE, LocalDate.of(2025, 7, 23)))));

         */
        /*

        // delete by id
        if(studentService.deleteStudentById(1011)){
            System.out.println("Student with rno " + 1011 + " deleted successfully");
        }
        else{
            System.out.println("Unsuccessfull to delete Student with rno " + 1011);
        }

        // fetch all students
        Runner.printStudents(studentService.fetchAllStudent());

        // find by id
        Optional<Student> student = studentService.fetchById(101);
        if(student.isPresent()){
            Student s = student.get();
            System.out.println("Student Details:::");
            System.out.println("Roll no.: " + s.getRno());
            System.out.println("Name : " + s.getName());
            System.out.println("Percentage : " + s.getPer());
            System.out.println("Gender : " + s.getGender());
            System.out.println("Birth Date : " + s.getBirthDate());
        }
        else {
            System.out.println("NO student found for rno::" + 101);
        }

        // saveOrUpdateStudent
        Student student1 = new Student(105, "EEE", 66.66, Gender.MALE, LocalDate.of(2025, 07, 30));
        studentService.saveOrUpdateStudent(student1);

        Student student2 = new Student(101, "AAA", 89.45, Gender.FEMALE, LocalDate.of(2025, 05, 21));
        student2.setPer(45.56);
        student2.setName("AAAA");
        studentService.saveOrUpdateStudent(student2);

        // update
        Student student = new Student(104, "DDD", 67.90, Gender.MALE, LocalDate.now());
        student.setPer(80.34);
        System.out.println("Updated : " + studentService.updateStudent(student));

        // Insert
        Student student = studentService.saveStudent(new Student(103, "CCC", 96.78, Gender.MALE, LocalDate.of(2025, 8, 21)));
        System.out.println("Student saved successfully!!\nData : " + student + "\n");

         */
    }
}
