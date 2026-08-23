package com.bhavik.repository;

import com.bhavik.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StudentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Student student) {
        String sql= "insert into student values(?, ?, ?, ?)";
        jdbcTemplate.update(sql, student.getRno(), student.getName(), student.getPer(), student.getCity());
        System.out.println("Record Inserted!!");
    }
    public int delete(int rno) {
        String sql= "delete from student where rno = ?";
        return jdbcTemplate.update(sql, rno);
    }

    public int update(Student student) {
        String sql = "update student set name = ?, per = ?, city = ? where rno = ?";
        return jdbcTemplate.update(sql, student.getName(), student.getPer(), student.getCity(), student.getRno());
    }
}
