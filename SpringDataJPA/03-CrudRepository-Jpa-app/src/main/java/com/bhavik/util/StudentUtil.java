package com.bhavik.util;

import com.bhavik.entity.Gender;
import com.bhavik.entity.Student;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class StudentUtil {
    private static Scanner sc = new Scanner(System.in);

    public static void printStudents(List<Student> students){
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

    public static Student readStudentDetails() {
        Student student = new Student();

        System.out.print("Enter Roll No: ");
        student.setRno(Integer.parseInt(sc.nextLine().trim()));

        System.out.print("Enter Name: ");
        student.setName(sc.nextLine().trim());

        System.out.print("Enter Percentage: ");
        student.setPer(Double.parseDouble(sc.nextLine().trim()));

        System.out.print("Enter Gender (" + Arrays.toString(Gender.values()) + "): ");
        student.setGender(Gender.valueOf(sc.nextLine().trim().toUpperCase()));

        System.out.print("Enter Birth Date (YYY-MM-DD): ");
        student.setBirthDate(LocalDate.parse(sc.nextLine().trim()));

        return student;
    }

    public static List<Integer> readIdList() {
        System.out.print("Enter number of ids: ");
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter id " + (i + 1) + ": ");
            ids.add(Integer.parseInt(sc.nextLine().trim()));
        }
        return ids;
    }
}
