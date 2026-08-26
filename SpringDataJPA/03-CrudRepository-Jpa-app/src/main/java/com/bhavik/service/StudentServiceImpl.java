package com.bhavik.service;

import com.bhavik.entity.Student;
import com.bhavik.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("studentService")
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> saveAllStudent(List<Student> students) {
        return (List<Student>) studentRepository.saveAll(students);
    }

    @Override
    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student saveOrUpdateStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> fetchById(Integer rno) {
        return studentRepository.findById(rno);
    }

    @Override
    public List<Student> fetchAllStudent() {
        return (List<Student>) studentRepository.findAll();
    }

    @Override
    public boolean deleteStudentById(Integer rno) {
        if(studentRepository.existsById(rno)){
            studentRepository.deleteById(rno);
            return true;
        }
        else{
            return false;
        }
    }




}
