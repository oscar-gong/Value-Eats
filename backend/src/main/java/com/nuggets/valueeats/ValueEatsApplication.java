package com.nuggets.valueeats;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ValueEatsApplication {
	public static void main(String[] args) {
		SpringApplication.run(ValueEatsApplication.class, args);
	}

	@PostConstruct
    public void init(){
      // Setting Spring Boot SetTimeZone
	//   TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Australia/Sydney"))); 
    }
}
