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
public class DinerFunctionalityController {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ReviewRepository reviewRepository;

	@Autowired
	private MockMvc mockMvc;

  // Test successfully create a review.
  @Test
  void dinerCreaateReviewTest1() throws Exception {
    this.userRepository.deleteAll();
    this.reviewRepository.deleteAll();
    Map<String, String> diner = new HashMap<>();
		diner.put("alias", "diner1");
		diner.put("email", "diner1@gmail.com");
		diner.put("address", "Sydney");
		diner.put("password", "12rwqeDsad@");

		String result = this.mockMvc.perform(
			post("/register/diner")
							.contentType(MediaType.APPLICATION_JSON)
							.content(String.valueOf(new JSONObject(diner)))
    )
    .andReturn()
		.getResponse()
		.getContentAsString();

    JSONObject data = new JSONObject(result);
		String token = data.getJSONObject("data").getString("token");

    Map<String, String> eatery = new HashMap<>();
		eatery.put("alias", "eatery1");
		eatery.put("email", "eatery1@gmail.com");
		eatery.put("address", "Sydney");
		eatery.put("password", "12rwqeDsad@");

    this.mockMvc.perform(
			post("/register/eatery")
							.contentType(MediaType.APPLICATION_JSON)
							.content(String.valueOf(new JSONObject(eatery)))
    );

    Map<String, String> review = new HashMap<>();
    review.put("dinerId","0");
    review.put("eateryId","1");
    review.put("message","hello");
    review.put("rating","2");

    this.mockMvc.perform(
			post("/diner/createreview")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", token)
        .content(String.valueOf(new JSONObject(review)))
    )
    .andExpect(status().isOk());
  }
}
