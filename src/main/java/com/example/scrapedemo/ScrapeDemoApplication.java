package com.example.scrapedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScrapeDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScrapeDemoApplication.class, args);
    }

}
