package com.teamproject.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class Teamproject1BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(Teamproject1BackendApplication.class, args);
    }

}
