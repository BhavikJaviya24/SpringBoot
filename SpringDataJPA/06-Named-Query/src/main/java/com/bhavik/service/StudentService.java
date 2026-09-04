package com.bhavik.service;

import com.bhavik.entity.Gender;
import com.bhavik.entity.Student;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    public List<Student> fetchAllPerAndGender(Double per, Gender gender);
    public void modifyName(String name, Integer rno);
    public List<Object[]> fetchByGenderAndName(String name, Gender gender);

    public List<Object[]> fetchGenderCount();
}
