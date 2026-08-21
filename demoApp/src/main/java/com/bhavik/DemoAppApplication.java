package com.bhavik;

import com.bhavik.beans.A;
import com.bhavik.beans.C;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoAppApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoAppApplication.class, args);
        System.out.println(A.b);
    }

}
