package com.nuggets.valueeats;

import com.nuggets.valueeats.controllers.database.DatabaseConnector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ValueeatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValueeatsApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

	@GetMapping("/test")
	public String test() throws Exception {
		DatabaseConnector.createDatabase();
		DatabaseConnector.connect();

		return "Test successful";
	}
}
