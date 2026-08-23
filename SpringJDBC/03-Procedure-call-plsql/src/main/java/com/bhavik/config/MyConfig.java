package com.bhavik.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.sql.DataSource;

@Configuration
public class MyConfig {

    @Bean
    public SimpleJdbcCall getGradeCall(DataSource dataSource){
        return new SimpleJdbcCall(dataSource).withFunctionName("get_grade");
        // if you want to call another function then you cannot call it because
        // the object is already associated with "get_grade"
        // to avoid it if you just return new SimpleJdbcCall object without associating it with any function.
        // but before using the object you associated it using object.withFunctionName("funct_name"), still same error.
        //eg. return new SimpleJdbcCall(dataSource);
    }

    //to solve this you need to make different objects for every function call.
    // in our project there are two plsql function calls get_grade() and avg_percentage(),
    // so we need to write two different bean methods for these two plsql function.

    @Bean
    public SimpleJdbcCall getPercentageCall(DataSource dataSource){
        return new SimpleJdbcCall(dataSource).withFunctionName("avg_percentage");
    }
}
