package com.nuggets.valueeats;

import com.nuggets.valueeats.entity.User;
import com.nuggets.valueeats.repository.UserRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserManagementControllerTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private UserRepository<User> userRepository;

	@Test
	void testRegisterDiner_validDiner() throws Exception {
		this.userRepository.deleteAll();
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

	@Test
	void testRegisterDiner_aliasAlreadyExists() throws Exception {
		this.userRepository.deleteAll();
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

	@Test
	void testRegisterDiner_invalidEmail() throws Exception {
		this.userRepository.deleteAll();
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

	@Test
	void testRegisterDiner() throws Exception {
		this.userRepository.deleteAll();
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

	@Test
	void testRegisterEatery_success() throws Exception {
		this.userRepository.deleteAll();
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

	@Test
	void testRegisterEatery_aliasExists() throws Exception {
		this.userRepository.deleteAll();
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

	@Test
	void testRegisterEatery_invalidEmail() throws Exception {
		this.userRepository.deleteAll();
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

	@Test
	void testRegisterEatery_invalidPassword() throws Exception {
		this.userRepository.deleteAll();
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

	@Test
	void testDinerLogin_success() throws Exception {
		this.userRepository.deleteAll();
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

	@Test
	void testDinerLogin_invalidDetails() throws Exception {
		this.userRepository.deleteAll();
		Map<String, String> body = new HashMap<>();
		body.put("alias", "diner1");
		body.put("email", "diner1@gmail.com");
		body.put("address", "Sydney");
		body.put("password", "12rwqeDsad@");

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

		this.mockMvc.perform(
			post("/login")
							.contentType(MediaType.APPLICATION_JSON)
							.content(String.valueOf(new JSONObject(body)))
			)
			.andExpect(status().is4xxClientError());
	}

	@Test
	void testEateryLogin_success() throws Exception {
		this.userRepository.deleteAll();
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

	@Test
	void testEateryLogin_invalidDetails() throws Exception {
		this.userRepository.deleteAll();
		Map<String, String> body = new HashMap<>();
		body.put("alias", "eatery1");
		body.put("email", "eatery1@gmail.com");
		body.put("address", "Sydney");
		body.put("password", "12rwqeDsad@");

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

		this.mockMvc.perform(
			post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.valueOf(new JSONObject(body)))
			)
			.andExpect(status().is4xxClientError());
	}

	@Test
	void testUpdateDiner_success() throws Exception {
		this.userRepository.deleteAll();
		this.userRepository.deleteAll();
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

		body = new HashMap<>();
		body.put("alias", "superman");
		System.out.println(new JSONObject(body));

		this.mockMvc.perform(
				post("/update/diner")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", token)
						.content(String.valueOf(new JSONObject(body)))
		)
				.andExpect(status().isOk());
	}

	@Test
	void testUpdateDiner_invalidInformation() throws Exception {
		this.userRepository.deleteAll();
		this.userRepository.deleteAll();
		Map<String, String> body = new HashMap<>();
		body.put("alias", "diner1");
		body.put("email", "diner1@gmail.com");
		body.put("address", "Sydney");
		body.put("password", "12rwqeDsad@");

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

		body = new HashMap<>();
		body.put("alias", "superman");
		body.put("password", "1234");

		this.mockMvc.perform(
				post("/update/diner")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", token)
						.content(String.valueOf(new JSONObject(body)))
		)
				.andExpect(status().is4xxClientError());
	}

	@Test
	void testUpdateEatery_validInformation() throws Exception {
		this.userRepository.deleteAll();
		this.userRepository.deleteAll();
		Map<String, String> body = new HashMap<>();
		body.put("alias", "eatery1");
		body.put("email", "eatery1@gmail.com");
		body.put("address", "Sydney");
		body.put("password", "12rwqeDsad@");
		System.out.println(new JSONObject(body));

		String result = this.mockMvc.perform(
		post("/register/eatery")
			.contentType(MediaType.APPLICATION_JSON)
			.content(String.valueOf(new JSONObject(body)))
		)
		.andReturn()
		.getResponse()
		.getContentAsString();
		JSONObject data = new JSONObject(result);
		String token = data.getJSONObject("data").getString("token");

		body = new HashMap<>();
		body.put("alias", "superman");
		System.out.println(new JSONObject(body));

		this.mockMvc.perform(
			post("/update/eatery")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", token)
				.content(String.valueOf(new JSONObject(body)))
			)
			.andExpect(status().isOk());
	}

	@Test
	void testUpdateEatery_invalidInformation() throws Exception {
		this.userRepository.deleteAll();
		this.userRepository.deleteAll();
		Map<String, String> body = new HashMap<>();
		body.put("alias", "eatery1");
		body.put("email", "eatery1@gmail.com");
		body.put("address", "Sydney");
		body.put("password", "12rwqeDsad@");

		String result = this.mockMvc.perform(
				post("/register/eatery")
			.contentType(MediaType.APPLICATION_JSON)
			.content(String.valueOf(new JSONObject(body)))
		)
		.andReturn()
		.getResponse()
		.getContentAsString();
		JSONObject data = new JSONObject(result);
		String token = data.getJSONObject("data").getString("token");

		body = new HashMap<>();
		body.put("alias", "superman");
		body.put("email","1234");

		this.mockMvc.perform(
			post("/update/eatery")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", token)
				.content(String.valueOf(new JSONObject(body)))
			)
			.andExpect(status().is4xxClientError());
	}

	@Test
	void testDinerLogout_activeToken() throws Exception {
		this.userRepository.deleteAll();
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

		String result = this.mockMvc.perform(
			post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.valueOf(new JSONObject(body)))
			)
		.andReturn()
		.getResponse()
		.getContentAsString();

		JSONObject data = new JSONObject(result);
		String token = data.getJSONObject("data").getString("token");

		body = new HashMap<>();
		body.put("alias", "superman");
		System.out.println(new JSONObject(body));

		this.mockMvc.perform(
			post("/logout")
							.contentType(MediaType.APPLICATION_JSON)
							.header("Authorization", token)
			)
			.andExpect(status().isOk());
	}

	@Test
	void testDinerLogout_invalidToken() throws Exception {
		this.userRepository.deleteAll();
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
			);

		String token = "invalidtoken";

		body = new HashMap<>();
		body.put("alias", "superman");
		System.out.println(new JSONObject(body));

		this.mockMvc.perform(
			post("/logout")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", token)
			)
			.andExpect(status().is4xxClientError());
	}

	@Test
	void testEateryLogout_validToken() throws Exception {
		this.userRepository.deleteAll();
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

		String result = this.mockMvc.perform(
			post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.valueOf(new JSONObject(body)))
			)
		.andReturn()
		.getResponse()
		.getContentAsString();

		JSONObject data = new JSONObject(result);
		String token = data.getJSONObject("data").getString("token");

		body = new HashMap<>();
		body.put("alias", "superman");
		System.out.println(new JSONObject(body));

		this.mockMvc.perform(
			post("/logout")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", token)
			)
			.andExpect(status().isOk());
	}

	@Test
	void testEateryLogout_invalidToken() throws Exception {
		this.userRepository.deleteAll();
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
			);

		String token = "invalidtoken";

		body = new HashMap<>();
		body.put("alias", "superman");
		System.out.println(new JSONObject(body));

		this.mockMvc.perform(
			post("/logout")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", token)
			)
			.andExpect(status().is4xxClientError());
	}
}
