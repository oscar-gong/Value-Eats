package com.nuggets.valueeats.integration;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HealthServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testTest() throws Exception {
        this.mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test")));
    }

    @Test
    public void testRegisterDiner() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("alias", "bob");
        body.put("email", "bob@gmail.com");
        body.put("address", "Sydney");
        body.put("password", "12rwqeDsad@");
        System.out.println(new JSONObject(body));

        this.mockMvc.perform(
                post("/register/diner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(new JSONObject(body)))
                )
                .andExpect(status().isOk())
                .andExpect(content().json("A"));
    }
}
