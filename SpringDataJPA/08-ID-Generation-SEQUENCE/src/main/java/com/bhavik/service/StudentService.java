package com.bhavik.service;

import com.bhavik.entity.Student;

import java.util.List;

public interface StudentService {
    public Student save(Student student);
    public List<Student> saveAllStudents(List<Student> students);
    public List<Student> fetchAll();
}
