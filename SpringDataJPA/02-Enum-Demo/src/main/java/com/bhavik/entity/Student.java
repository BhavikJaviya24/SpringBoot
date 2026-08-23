package com.bhavik.entity;

public class Student {
    private int rno;
    private String name;
    private Double per;

    private Gender gender;

    public Student() {
    }

    public Student(int rno, String name, Double per, Gender gender) {
        this.rno = rno;
        this.name = name;
        this.per = per;
        this.gender = gender;
    }

    public int getRno() {
        return rno;
    }

    public void setRno(int rno) {
        this.rno = rno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPer() {
        return per;
    }

    public void setPer(Double per) {
        this.per = per;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Student{" +
                "rno=" + rno +
                ", name='" + name + '\'' +
                ", per=" + per +
                ", gender=" + gender +
                '}';
    }
}
