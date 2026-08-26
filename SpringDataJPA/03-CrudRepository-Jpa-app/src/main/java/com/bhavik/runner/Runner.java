package com.bhavik.runner;

import com.bhavik.entity.Student;
import com.bhavik.service.StudentService;
import com.bhavik.util.StudentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


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
        int choice;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("\n\n=============== STUDENT MENU ===============");
            System.out.println("1. Save/Update Student");
            System.out.println("2. Save All Students");
            System.out.println("3. Delete Student");
            System.out.println("4. Delete All Students");
            System.out.println("5. Delete All Students (by list)");
            System.out.println("6. Delete Student By Id");
            System.out.println("7. Delete All Students By Id List");
            System.out.println("8. Fetch By Id");
            System.out.println("9. Fetch All Students");
            System.out.println("10. Fetch All Students By Id List");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = Integer.parseInt(sc.nextLine().trim());

            switch (choice) {
                case 1:
                    Student student1 = StudentUtil.readStudentDetails();
                    studentService.saveOrUpdateStudent(student1);
                    if(studentService.studentExistById(student1.getRno())){
                        System.out.println("Record UPDATED Successfully.");
                    }
                    else {
                        System.out.println("Record SAVED Successfully.");
                    }
                    break;
                case 2:
                    System.out.print("How many students? ");
                    int n = Integer.parseInt(sc.nextLine().trim());
                    List<Student> students1 = new ArrayList<>();
                    for (int i = 0; i < n; i++) {
                        System.out.println("Student " + (i + 1) + ":");
                        students1.add(StudentUtil.readStudentDetails());
                    }
                    System.out.println("-----".repeat(4) + " Saved Students " + "-----".repeat(4));
                    StudentUtil.printStudents(studentService.saveAllStudent(students1));
                    break;
                case 3:
                    Student student2 = StudentUtil.readStudentDetails();
                    if(studentService.deleteStudent(student2)){
                        System.out.println("Student DELETED Successfully.");
                    }
                    else{
                        System.out.println("Student NOT DELETED !!!!!");
                    }
                    break;
                case 4:
                    if(studentService.deleteAllStudent()){
                        System.out.println("ALL Students DELETED Successfully.");
                    }
                    else{
                        System.out.println("ALl Students NOT DELETED !!!!!");
                    }
                    break;
                case 5:
                    System.out.print("How many students to delete? ");
                    int m = Integer.parseInt(sc.nextLine().trim());
                    List<Student> students2 = new ArrayList<>();
                    for (int i = 0; i < m; i++) {
                        System.out.println("Student " + (i + 1) + ":");
                        students2.add(StudentUtil.readStudentDetails());
                    }
                    if(studentService.deleteAllStudent(students2)){
                        System.out.println("ALL given Students DELETED Successfully.");
                    }
                    else{
                        System.out.println("ALl given Students NOT DELETED !!!!!");
                    }
                    break;
                case 6:
                    System.out.print("Enter Roll No to delete: ");
                    int rno = Integer.parseInt(sc.nextLine().trim());
                    if(studentService.deleteStudentById(rno)){
                        System.out.println("Student with Roll No. " + rno + " DELETED Successfully.");
                    }
                    else{
                        System.out.println("Student with Roll No. " + rno + " NOT found to be deleted!!");
                    }
                    break;
                case 7:
                    List<Integer> ids = StudentUtil.readIdList();
                    if(studentService.deleteAllStudentById(ids)){
                        System.out.println("ALL Students with given IDs DELETED Successfully.");
                    }
                    else{
                        System.out.println("Students with given IDs NOT DELETED !!!!!");
                    }
                    break;
                case 8:
                    System.out.print("Enter Roll No to fetch: ");
                    int rno2 = Integer.parseInt(sc.nextLine().trim());
                    Optional<Student> student3 = studentService.fetchById(rno2);
                    if (student3.isPresent()) {
                        StudentUtil.printStudents(List.of(student3.get()));
                    } else {
                        System.out.println("Student with Roll No. "+ rno2 + " not found");
                    }
                    break;
                case 9:
                    StudentUtil.printStudents(studentService.fetchAllStudent());
                    break;
                case 10:
                    List<Integer> ids2 = StudentUtil.readIdList();
                    StudentUtil.printStudents(studentService.fetchAllStudentById(ids2));
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        } while (choice != 0);
    }
}
