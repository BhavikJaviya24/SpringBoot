package com.bhavik.model;

public class Student {
    private int rno;
    private String name;
    private Double per;
    private String city;

    public Student() {
    }

    public Student(int rno, String name, Double per, String city) {
        this.rno = rno;
        this.name = name;
        this.per = per;
        this.city = city;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
