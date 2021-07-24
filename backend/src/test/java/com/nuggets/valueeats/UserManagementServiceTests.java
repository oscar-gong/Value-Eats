package com.nuggets.valueeats;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nuggets.valueeats.controller.*;
import com.nuggets.valueeats.entity.*;
import com.nuggets.valueeats.service.*;
import com.nuggets.valueeats.repository.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.json.JSONString;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserManagementServiceTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	// Test valid diner.
	@Test
	void dinerRegisterTest1() throws Exception {
		Map<String, String> body = new HashMap<>();
		body.put("alias", "diner1");
		body.put("email", "diner1@gmail.com");
		body.put("address", "Sydney");
		body.put("password", "12rwqeDsad@");
		System.out.println(new JSONObject(body));

		this.mockMvc.perform(
			post("/register/diner")
							.contentType(MediaType.APPLICATION_JSON)
							.content(String.valueOf(new JSONObject(body)))
		  )
			.andExpect(status().isOk());
	}

	// Test diner with the duplicate alias.
	@Test
	void dinerRegisterTest2() throws Exception {
		Map<String, String> body = new HashMap<>();
		body.put("alias", "diner1");
		body.put("email", "diner1@gmail.com");
		body.put("address", "Sydney");
		body.put("password", "12rwqeDsad@");
		System.out.println(new JSONObject(body));

		this.mockMvc.perform(
			post("/register/diner")
							.contentType(MediaType.APPLICATION_JSON)
							.content(String.valueOf(new JSONObject(body)))
		  );

		this.mockMvc.perform(
			post("/register/diner")
							.contentType(MediaType.APPLICATION_JSON)
							.content(String.valueOf(new JSONObject(body)))
			)
			.andExpect(status().is4xxClientError());
	}

	// Test diner with the invalid email.
	@Test
	void dinerRegisterTest3() throws Exception {
		Map<String, String> body = new HashMap<>();
		body.put("alias", "diner2");
		body.put("email", "diner2");
		body.put("address", "Sydney");
		body.put("password", "12rwqeDsad@");
		System.out.println(new JSONObject(body));


		this.mockMvc.perform(
			post("/register/diner")
							.contentType(MediaType.APPLICATION_JSON)
							.content(String.valueOf(new JSONObject(body)))
			)
			.andExpect(status().is4xxClientError());
	}

// Test diner with the invalid password.
@Test
void dinerRegisterTest4() throws Exception {
	Map<String, String> body = new HashMap<>();
	body.put("alias", "diner2");
	body.put("email", "diner2@gmail.com");
	body.put("address", "Sydney");
	body.put("password", "1234");
	System.out.println(new JSONObject(body));


	this.mockMvc.perform(
		post("/register/diner")
						.contentType(MediaType.APPLICATION_JSON)
						.content(String.valueOf(new JSONObject(body)))
		)
		.andExpect(status().is4xxClientError());
}

// Test valid diner.
@Test
void eateryRegisterTest1() throws Exception {
	Map<String, String> body = new HashMap<>();
	body.put("alias", "eatery1");
	body.put("email", "eatery1@gmail.com");
	body.put("address", "Sydney");
	body.put("password", "12rwqeDsad@");
	System.out.println(new JSONObject(body));

	this.mockMvc.perform(
		post("/register/eatery")
						.contentType(MediaType.APPLICATION_JSON)
						.content(String.valueOf(new JSONObject(body)))
		)
		.andExpect(status().isOk());
}

// Test eatery with the duplicate alias.
@Test
void eateryRegisterTest2() throws Exception {
	Map<String, String> body = new HashMap<>();
	body.put("alias", "eatery1");
	body.put("email", "eatery1@gmail.com");
	body.put("address", "Sydney");
	body.put("password", "12rwqeDsad@");
	System.out.println(new JSONObject(body));

	this.mockMvc.perform(
		post("/register/eatery")
						.contentType(MediaType.APPLICATION_JSON)
						.content(String.valueOf(new JSONObject(body)))
		);

	this.mockMvc.perform(
		post("/register/eatery")
						.contentType(MediaType.APPLICATION_JSON)
						.content(String.valueOf(new JSONObject(body)))
		)
		.andExpect(status().is4xxClientError());
}

	// Test eatery with the invalid email.
	@Test
	void eateryRegisterTest3() throws Exception {
		Map<String, String> body = new HashMap<>();
		body.put("alias", "eatery2");
		body.put("email", "eatery2");
		body.put("address", "Sydney");
		body.put("password", "12rwqeDsad@");
		System.out.println(new JSONObject(body));


		this.mockMvc.perform(
			post("/register/eatery")
							.contentType(MediaType.APPLICATION_JSON)
							.content(String.valueOf(new JSONObject(body)))
			)
			.andExpect(status().is4xxClientError());
	}

	// Test eatery with the invalid password.
	@Test
	void eateryRegisterTest4() throws Exception {
		Map<String, String> body = new HashMap<>();
		body.put("alias", "eatery2");
		body.put("email", "eatery2@gmail.com");
		body.put("address", "Sydney");
		body.put("password", "1234");
		System.out.println(new JSONObject(body));


		this.mockMvc.perform(
		post("/register/eatery")
						.contentType(MediaType.APPLICATION_JSON)
						.content(String.valueOf(new JSONObject(body)))
		)
		.andExpect(status().is4xxClientError());
	}

	// Test diner login with valid detail.
	@Test
	void dinerLoginTest1() throws Exception {
		Map<String, String> body = new HashMap<>();
		body.put("alias", "diner1");
		body.put("email", "diner1@gmail.com");
		body.put("address", "Sydney");
		body.put("password", "12rwqeDsad@");
		System.out.println(new JSONObject(body));

		this.mockMvc.perform(
			post("/register/diner")
							.contentType(MediaType.APPLICATION_JSON)
							.content(String.valueOf(new JSONObject(body)))
		);

		this.mockMvc.perform(
			post("/login")
							.contentType(MediaType.APPLICATION_JSON)
							.content(String.valueOf(new JSONObject(body)))
			)
			.andExpect(status().isOk());
	}

		// Test diner login with invalid detail.
		@Test
		void dinerLoginTest2() throws Exception {
			Map<String, String> body = new HashMap<>();
			body.put("alias", "diner1");
			body.put("email", "diner1@gmail.com");
			body.put("address", "Sydney");
			body.put("password", "12rwqeDsad@");
			System.out.println(new JSONObject(body));
	
			this.mockMvc.perform(
				post("/register/diner")
								.contentType(MediaType.APPLICATION_JSON)
								.content(String.valueOf(new JSONObject(body)))
			);
	
			body = new HashMap<>();
			body.put("alias", "diner1");
			body.put("email", "diner@gmail.com");
			body.put("address", "Sydney");
			body.put("password", "12rwqeDsad@");
			System.out.println(new JSONObject(body));

			this.mockMvc.perform(
				post("/login")
								.contentType(MediaType.APPLICATION_JSON)
								.content(String.valueOf(new JSONObject(body)))
				)
				.andExpect(status().is4xxClientError());
		}

			// Test eatery login with valid detail.
	@Test
	void eateryLoginTest1() throws Exception {
		Map<String, String> body = new HashMap<>();
		body.put("alias", "eatery1");
		body.put("email", "eatery1@gmail.com");
		body.put("address", "Sydney");
		body.put("password", "12rwqeDsad@");
		System.out.println(new JSONObject(body));

		this.mockMvc.perform(
			post("/register/eatery")
							.contentType(MediaType.APPLICATION_JSON)
							.content(String.valueOf(new JSONObject(body)))
		);

		this.mockMvc.perform(
			post("/login")
							.contentType(MediaType.APPLICATION_JSON)
							.content(String.valueOf(new JSONObject(body)))
			)
			.andExpect(status().isOk());
	}

		// Test eatery login with invalid detail.
		@Test
		void eateryLoginTest2() throws Exception {
			Map<String, String> body = new HashMap<>();
			body.put("alias", "eatery1");
			body.put("email", "eatery1@gmail.com");
			body.put("address", "Sydney");
			body.put("password", "12rwqeDsad@");
			System.out.println(new JSONObject(body));
	
			this.mockMvc.perform(
				post("/register/eatery")
								.contentType(MediaType.APPLICATION_JSON)
								.content(String.valueOf(new JSONObject(body)))
			);
	
			body = new HashMap<>();
			body.put("alias", "eatery1");
			body.put("email", "eatery@gmail.com");
			body.put("address", "Sydney");
			body.put("password", "12rwqeDsad@");
			System.out.println(new JSONObject(body));

			this.mockMvc.perform(
				post("/login")
								.contentType(MediaType.APPLICATION_JSON)
								.content(String.valueOf(new JSONObject(body)))
				)
				.andExpect(status().is4xxClientError());
		}

	@Test
	void dinerUpdateTest1() throws Exception {
		Map<String, String> body = new HashMap<>();
		body.put("alias", "diner1");
		body.put("email", "diner1@gmail.com");
		body.put("address", "Sydney");
		body.put("password", "12rwqeDsad@");
		System.out.println(new JSONObject(body));

		String result = this.mockMvc.perform(
		post("/register/diner")
						.contentType(MediaType.APPLICATION_JSON)
						.content(String.valueOf(new JSONObject(body)))
		)
		.andReturn()
		.getResponse()
		.getContentAsString();
		JSONObject data = new JSONObject(result);
		String token = data.getJSONObject("data").getString("token");
		
		this.mockMvc.perform(
			post("/update/diner")
							.contentType(MediaType.APPLICATION_JSON)
							.content(String.valueOf(new JSONObject(body)))
			)
			.andExpect(status().isOk());
	}
}
