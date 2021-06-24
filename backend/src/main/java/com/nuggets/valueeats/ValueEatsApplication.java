package com.nuggets.valueeats;

import com.nuggets.valueeats.controllers.DinerController;
import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

@SpringBootApplication
@RestController
public class ValueEatsApplication {
	//@Autowired
	//private final JdbcTemplate jdbcTemplate;

	//@Autowired
	//private DinerController dinerController;

	public static void main(String[] args) {

		SpringApplication.run(ValueEatsApplication.class, args);
	}
	// 	Install postgres version 42.2.14 or others.
	// 	Then run those commands:
	// 	sudo su postgres
	// 	psql
	//	create database demoau;
	// 	create user valueeats with encrypted password 'valueeats';
	//	ALTER USER valueeats WITH SUPERUSER;
}
