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
    public List<Student> fetchAllStudent() {
        return studentRepository.giveAllStudent();
    }

    @Override
    public List<Student> fetchPercentageRange(Double start, Double end) {
        return studentRepository.findPercentageRange(start, end);
    }

    @Override
    public List<Object[]> fetchGenderNamePer(Double per, Gender gender) {
        return studentRepository.findGenderNamePercentage(per, gender);
    }
}
