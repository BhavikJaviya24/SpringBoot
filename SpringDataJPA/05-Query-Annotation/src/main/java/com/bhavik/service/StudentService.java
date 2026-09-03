package com.bhavik.service;

import com.bhavik.entity.Gender;
import com.bhavik.entity.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentService {

    public List<Student> fetchAllStudent();
    public List<Student> fetchPercentageRange(Double start, Double end);
    public List<Object[]> fetchGenderNamePer(Double per, Gender gender);
}
