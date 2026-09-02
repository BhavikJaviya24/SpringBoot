package com.bhavik.service;

import com.bhavik.entity.Gender;
import com.bhavik.entity.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    public Optional<Student> fetchById(Integer rno);
    public List<Student> fetchAllStudent();
    public List<Student> fetchByGender(Gender gender);
    public List<Student> fetchByBirthDate(LocalDate birthDate);
    public List<Student> fetchByDistinction();
    public List<Student> fetchByPerBetween(Double startPer, Double endPer);
    public List<Student> fetchByPerAfter(Double per);
    public List<Student> fetchByPerBefore(Double per);
    public List<Student> fetchByDateAfter(LocalDate date);
    public List<Student> fetchByDateBefore(LocalDate date);
    public List<Student> fetchByNameStartingWith(String name);
    public List<Student> fetchTop3ByPer();

}
