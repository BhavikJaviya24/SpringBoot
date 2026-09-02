package com.bhavik.service;

import com.bhavik.entity.Gender;
import com.bhavik.entity.Student;
import com.bhavik.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service("studentService")
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Optional<Student> fetchById(Integer rno) {
        return studentRepository.findById(rno);
    }

    @Override
    public List<Student> fetchAllStudent() {
        return (List<Student>) studentRepository.findAll();
    }

    @Override
    public List<Student> fetchByGender(Gender gender) {
        return (List<Student>) studentRepository.findByGender(gender);
    }

    @Override
    public List<Student> fetchByBirthDate(LocalDate birthDate) {
        return (List<Student>) studentRepository.getByBirthDate(birthDate);
    }

    @Override
    public List<Student> fetchByDistinction() {
        return (List<Student>) studentRepository.readByPerGreaterThanEqual(74.00);
    }

    @Override
    public List<Student> fetchByPerBetween(Double startPer, Double endPer) {
        return (List<Student>) studentRepository.findByPerBetween(startPer, endPer);
    }

    @Override
    public List<Student> fetchByPerAfter(Double per) {
        return (List<Student>) studentRepository.findByPerAfter(per);
    }

    @Override
    public List<Student> fetchByPerBefore(Double per) {
        return (List<Student>) studentRepository.findByPerBefore(per);
    }

    @Override
    public List<Student> fetchByDateAfter(LocalDate date) {
        return (List<Student>) studentRepository.findByBirthDateAfter(date);
    }

    @Override
    public List<Student> fetchByDateBefore(LocalDate date) {
        return (List<Student>) studentRepository.findByBirthDateBefore(date);
    }

    @Override
    public List<Student> fetchByNameStartingWith(String name) {
        return (List<Student>) studentRepository.findByNameStartingWithIgnoreCase(name);
    }

    @Override
    public List<Student> fetchTop3ByPer() {
        return (List<Student>)  studentRepository.findTop3ByOrderByPerDesc();
    }


}
