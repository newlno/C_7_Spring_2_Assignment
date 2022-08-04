package com.example.c_7_spring_2_assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan("lecturer")
@SpringBootApplication
public class C7Spring2AssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(C7Spring2AssignmentApplication.class, args);
    }
}