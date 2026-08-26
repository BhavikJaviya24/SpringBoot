package com.bhavik.service;

import com.bhavik.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    public Student saveStudent(Student student);
    public List<Student> saveAllStudent(List<Student> students);
    public Student updateStudent(Student student);
    public Student saveOrUpdateStudent(Student student);
    public Optional<Student> fetchById(Integer rno);
    public List<Student> fetchAllStudent();
    public boolean deleteStudentById(Integer rno);
}
