package com.bhavik.repository;

import com.bhavik.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class StudentRepository {

    @Autowired
    private SimpleJdbcCall getGradeCall;

    @Autowired
    private SimpleJdbcCall getPercentageCall;

    public Object findGrade(int rno){
        // if a function with same name is present in another database then we get error
        //to solve it use:
        //jdbcCall.withCatalogName("database_name");
        // in our project 'database_name' would be 'sbai02'
         return getGradeCall.execute(rno).get("returnvalue");
         //     --------------------   ----------------
        //       this returns map         map.get(key)

        // "returnvalue" is default key
        // if using MySql then the key is "return"
    }
    public Object findAverage(String city){

        return getPercentageCall.execute(city).get("returnvalue");
    }
}
