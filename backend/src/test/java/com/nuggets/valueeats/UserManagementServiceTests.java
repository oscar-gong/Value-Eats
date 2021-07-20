package com.nuggets.valueeats;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nuggets.valueeats.controller.*;
import com.nuggets.valueeats.entity.*;
import com.nuggets.valueeats.service.*;
import com.nuggets.valueeats.repository.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.minidev.json.JSONObject;


class UserManagementServiceTests {
	@Autowired
	DinerRepository dinerRepository;

	@Autowired
	EateryRepository eateryRepository;

	@Test
	void contextLoads() {
		UserManagementController userManagementController = new UserManagementController();
		Diner diner = new Diner();
		diner.setAlias("diner1");
		diner.setEmail("diner1@gmail.com");
		diner.setPassword("Abcd1234");
		diner.setAddress("sydney");
    System.out.println(diner);
		userManagementController.registerDiner(diner);
		assertEquals(1, dinerRepository.findAll().size());
	}

}
