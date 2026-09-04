package com.bhavik.repository;

import com.bhavik.entity.Gender;
import com.bhavik.entity.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {
    public List<Student> findAllPerAndGender(@Param("per") Double per, @Param("gender") Gender gender);
    @Modifying
    @Transactional
    public void updateName( @Param("name") String name, @Param("rno") Integer rno);

    public List<Object[]> findByGenderAndName(@Param("name") String name, @Param("gender") Gender gender);

    public List<Object[]> findGenderCount();
}
