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
    public List<Student> fetchAllPerAndGender(Double per, Gender gender) {
        return studentRepository.findAllPerAndGender(per, gender);
    }

    @Override
    public void modifyName(String name, Integer rno) {
        studentRepository.updateName(name, rno);
    }

    @Override
    public List<Object[]> fetchByGenderAndName(String name, Gender gender) {
        return studentRepository.findByGenderAndName(name, gender);
    }

    @Override
    public List<Object[]> fetchGenderCount() {
        return studentRepository.findGenderCount();
    }
}
