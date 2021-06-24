package com.nuggets.valueeats;

import com.nuggets.valueeats.controllers.RegisterController;
import com.nuggets.valueeats.controllers.database.DatabaseController;
import com.nuggets.valueeats.entity.User.Diner;
import com.nuggets.valueeats.service.database.DatabaseAuthenticationTableService;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private RegisterController registerController;

	public static void main(String[] args) throws SQLException {
		DatabaseController.createDatabase();
		DatabaseAuthenticationTableService.createNewTable();

		SpringApplication.run(ValueEatsApplication.class, args);
	}

	@GetMapping("/register")
	public boolean register(@RequestParam(value = "username", defaultValue = "") String username) throws SQLException {
		String password = "todo";
		String email = "todo@todo.com";

		return registerController.registerDiner(new Diner(username, email, password));
	}

	@GetMapping("/health")
	public String health(@RequestParam(value = "name", defaultValue = "World") String name) {
		return "Hello " + name;
	}

	@GetMapping("/database_connection")
	public String database_connection() throws SQLException {
		ResultSet resultSet = DatabaseController.processReadRequest("SELECT 1");

		return "Test successful with true and 1: " + resultSet.next() + " " + resultSet.getRow();
	}
}
