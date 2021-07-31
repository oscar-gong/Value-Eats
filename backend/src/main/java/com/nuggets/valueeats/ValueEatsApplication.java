package com.nuggets.valueeats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class ValueEatsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ValueEatsApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // Setting Spring Boot SetTimeZone
        //   TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Australia/Sydney")));
    }
}
