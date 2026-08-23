package com.bhavik;

import com.bhavik.entity.Gender;
import com.bhavik.entity.Student;

public class Main {
    public static void main(String[] args) {

        //Student student = new Student(101, "AAA", 89.56, Gender.MALE);
        //System.out.println(student);

        for (Gender g : Gender.values()){
            System.out.println(g);
            System.out.println(g.ordinal());
            System.out.println(g.getValue());
            System.out.println(g.getCode());
        }

    }
}