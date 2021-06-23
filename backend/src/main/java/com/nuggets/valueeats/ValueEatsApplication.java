package com.nuggets.valueeats;

import com.nuggets.valueeats.controllers.database.DatabaseConnector;
import com.nuggets.valueeats.controllers.database.DatabaseManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;

@SpringBootApplication
@RestController
public class ValueEatsApplication {
	public static void main(String[] args) throws SQLException {
		DatabaseConnector.createDatabase();
		DatabaseConnector.createNewTable(); // TODO: Bad naming and placement, I am not too sure where to put this

		SpringApplication.run(ValueEatsApplication.class, args);
	}

	@GetMapping("/health")
	public String health(@RequestParam(value = "name", defaultValue = "World") String name) {
		return "Hello " + name;
	}

	@GetMapping("/database_connection")
	public String database_connection() throws SQLException {
		ResultSet resultSet = DatabaseManager.processReadRequest("SELECT 1");

		return "Test successful with true and 1: " + resultSet.next() + " " + resultSet.getRow();
	}
}
