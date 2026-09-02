package com.bhavik.repository;

import com.bhavik.entity.Gender;
import com.bhavik.entity.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {
    // We can start custom method name by find/get/read
    //syntax :: <find/get/read>by<fieldName><condition/expression>(<arguments>);

    public List<Student> findByGender(Gender gender);
    public List<Student> getByBirthDate(LocalDate birthDate);
    public List<Student> readByPerGreaterThanEqual(Double per);
    public List<Student> findByPerBetween(Double startPer, Double endPer); // inclusive of start and end
    public List<Student> findByBirthDateAfter(LocalDate date);
    public List<Student> findByBirthDateBefore(LocalDate date);
    public List<Student> findByPerBefore(Double per);
    public List<Student> findByPerAfter(Double per);
    public List<Student> findByNameStartingWithIgnoreCase(String name);

    public List<Student> findTop3ByOrderByPerDesc();
    //findTop3 — limits results to 3 rows (Spring Data recognizes Top / First keywords for this).
    //By — starts the query condition (empty here, so it fetches from all rows).
    //OrderByPerDesc — sorts by the per field in descending order.
}
