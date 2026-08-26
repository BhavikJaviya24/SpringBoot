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
    public boolean studentExistById(Integer id) {
        return studentRepository.existsById(id);
    }

    @Override
    public Student saveOrUpdateStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> saveAllStudent(List<Student> students) {
        return (List<Student>) studentRepository.saveAll(students);
    }

    @Override
    public boolean deleteStudent(Student student) {
        studentRepository.delete(student);
        return true;
    }

    @Override
    public boolean deleteAllStudent() {
        studentRepository.deleteAll();
        return true;
    }

    @Override
    public boolean deleteAllStudent(List<Student> students) {
        if(!students.isEmpty()){
            studentRepository.deleteAll();
            return true;
        }
        else{
            return false;
        }
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

    @Override
    public boolean deleteAllStudentById(List<Integer> ids) {
        if(!ids.isEmpty()){
            studentRepository.deleteAllById(ids);
            return true;
        }
        else{
            return false;
        }
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
    public List<Student> fetchAllStudentById(List<Integer> ids) {
        if(!ids.isEmpty()){
            return (List<Student>) studentRepository.findAllById(ids);

        }
        else{
            return List.of();
        }
    }
}
