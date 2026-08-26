package com.bhavik.service;

import com.bhavik.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    public boolean studentExistById(Integer id);

    public Student saveOrUpdateStudent(Student student);
    public List<Student> saveAllStudent(List<Student> students);

    public boolean deleteStudent(Student student);
    public boolean deleteAllStudent();
    public boolean deleteAllStudent(List<Student> students);
    public boolean deleteStudentById(Integer rno);
    public boolean deleteAllStudentById(List<Integer> ids);

    public Optional<Student> fetchById(Integer rno);
    public List<Student> fetchAllStudent();
    public List<Student> fetchAllStudentById(List<Integer> ids);

}
