package com.bhavik.repository;

import com.bhavik.entity.Gender;
import com.bhavik.entity.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {

    @Query("select s from Student s")
    List<Student> findAllStudent();     // using JPQL

    @Query(value = "select * from student", nativeQuery = true)
    List<Student> giveAllStudent();     // using SQL
    /*
    - if your requirements are not being fulfilled by JPQL then go for native SQL.
    - it is database specific.
    - to specify that it's a SQL, we provide 2 parameters to @Query
        1. value = "query"
        2. nativeQuery = true
    - native SQL is avoided because, if we change db in future then we need to manually change the query in
      source code depending on the db being used ( hectic for large projects with multiple tables)
     */

    //@Query("select s from Student s where s.per>=?1 and s.per<=?2")
    @Query("select s from Student s where s.per>=:start and s.per<=:end")
    List<Student> findPercentageRange(@Param("start") Double start, @Param("end") Double end);
    /*  using JPQL
    - here s.per is field in entity class Student
    - in s.per>=?1 and s.per<=?2,
        ?1 positional parameter for start
        ?2 positional parameter for end
    - in s.per>=:start and s.per<=:end
        :start (this can be any name/character/word) named parameter for start because of @Param("start") Double start
        :end (this can be any name/character/word) named parameter for end because of @Param("end") Double end
    - same syntax for SQL queries
     */

    @Query("select s.gender, s.name, s.per from Student s where s.per>:one and s.gender=:two")
    List<Object[]> findGenderNamePercentage( @Param("one") Double per, @Param("two") Gender gender);
    /*
    - selecting specific columns is called scaler projection.
    - List<Student> means list of Student objects with all fields. But for scaler projection use List<Object[]>
    - List<Object[]> means list of arrays of type Object, because we are only selecting specific columns not all 5 columns.
     */

}
