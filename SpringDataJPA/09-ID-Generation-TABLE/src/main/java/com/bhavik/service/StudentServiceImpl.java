package com.bhavik.service;

import com.bhavik.entity.Student;
import com.bhavik.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("studentService")
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> saveAllStudents(List<Student> students) {
        return (List<Student>) studentRepository.saveAll(students);
    }

    @Override
    public List<Student> fetchAll() {
        return (List<Student>) studentRepository.findAll();
    }
}
