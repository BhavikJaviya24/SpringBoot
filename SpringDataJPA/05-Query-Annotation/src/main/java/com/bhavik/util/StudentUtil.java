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
}
