package com.nuggets.valueeats;

import com.nuggets.valueeats.entity.User;
import com.nuggets.valueeats.repository.ReviewRepository;
import com.nuggets.valueeats.repository.UserRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DinerFunctionalityControllerTests {
    @Autowired
    private UserRepository<User> userRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateReview_haventDinedAt() throws Exception {
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
        review.put("dinerId", "0");
        review.put("eateryId", "1");
        review.put("message", "hello");
        review.put("rating", "2");

        this.mockMvc.perform(
                post("/diner/createreview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(String.valueOf(new JSONObject(review)))
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testCreateReview_invalidEateryId() throws Exception {
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
        review.put("dinerId", "100");
        review.put("eateryId", "100");
        review.put("message", "hello");
        review.put("rating", "2");

        this.mockMvc.perform(
                post("/diner/createreview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(String.valueOf(new JSONObject(review)))
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testCreateReview_invalidRating() throws Exception {
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
        review.put("dinerId", "0");
        review.put("eateryId", "1");
        review.put("message", "hello");
        review.put("rating", "5.3");

        this.mockMvc.perform(
                post("/diner/createreview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(String.valueOf(new JSONObject(review)))
        )
                .andExpect(status().is4xxClientError());
    }

//    @Test
//    public void testRemoveReview_invalidInput() throws Exception {
//        this.userRepository.deleteAll();
//        this.reviewRepository.deleteAll();
//        Map<String, String> diner = new HashMap<>();
//        diner.put("alias", "diner1");
//        diner.put("email", "diner1@gmail.com");
//        diner.put("address", "Sydney");
//        diner.put("password", "12rwqeDsad@");
//
//        String result = this.mockMvc.perform(
//                post("/register/diner")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(String.valueOf(new JSONObject(diner)))
//        )
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        JSONObject data = new JSONObject(result);
//        String token = data.getJSONObject("data").getString("token");
//
//        Map<String, String> eatery = new HashMap<>();
//        eatery.put("alias", "eatery1");
//        eatery.put("email", "eatery1@gmail.com");
//        eatery.put("address", "Sydney");
//        eatery.put("password", "12rwqeDsad@");
//
//        this.mockMvc.perform(
//                post("/register/eatery")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(String.valueOf(new JSONObject(eatery)))
//        );
//
//        Map<String, String> review = new HashMap<>();
//        review.put("id", "0");
//        review.put("dinerId", "0");
//        review.put("eateryId", "1");
//        review.put("message", "hello");
//        review.put("rating", "4");
//
//        this.mockMvc.perform(
//                post("/diner/createreview")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", token)
//                        .content(String.valueOf(new JSONObject(review)))
//        );
//
//        this.mockMvc.perform(
//                MockMvcRequestBuilders
//                        .delete("/diner/removereview")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", token)
//                        .content(String.valueOf(new JSONObject(review)))
//        )
//                .andExpect(status().isOk());
//    }

    @Test
    public void testRemoveReview_invalidToken() throws Exception {
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
        review.put("id", "0");
        review.put("dinerId", "0");
        review.put("eateryId", "1");
        review.put("message", "hello");
        review.put("rating", "4");

        this.mockMvc.perform(
                post("/diner/createreview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(String.valueOf(new JSONObject(review)))
        );

        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/diner/removereview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "InvalidToken")
                        .content(String.valueOf(new JSONObject(review)))
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testRemoveReview_invalidMessageId() throws Exception {
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
        review.put("id", "100");
        review.put("dinerId", "0");
        review.put("eateryId", "1");
        review.put("message", "hello");
        review.put("rating", "4");

        this.mockMvc.perform(
                post("/diner/createreview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(String.valueOf(new JSONObject(review)))
        );

        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/diner/removereview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(String.valueOf(new JSONObject(review)))
        )
                .andExpect(status().is4xxClientError());
    }

//    @Test
//    public void testEditReview_validInput() throws Exception {
//        this.userRepository.deleteAll();
//        this.reviewRepository.deleteAll();
//        Map<String, String> diner = new HashMap<>();
//        diner.put("alias", "diner1");
//        diner.put("email", "diner1@gmail.com");
//        diner.put("address", "Sydney");
//        diner.put("password", "12rwqeDsad@");
//
//        String result = this.mockMvc.perform(
//                post("/register/diner")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(String.valueOf(new JSONObject(diner)))
//        )
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        JSONObject data = new JSONObject(result);
//        String token = data.getJSONObject("data").getString("token");
//
//        Map<String, String> eatery = new HashMap<>();
//        eatery.put("alias", "eatery1");
//        eatery.put("email", "eatery1@gmail.com");
//        eatery.put("address", "Sydney");
//        eatery.put("password", "12rwqeDsad@");
//
//        this.mockMvc.perform(
//                post("/register/eatery")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(String.valueOf(new JSONObject(eatery)))
//        );
//
//        Map<String, String> review1 = new HashMap<>();
//        review1.put("id", "0");
//        review1.put("dinerId", "0");
//        review1.put("eateryId", "1");
//        review1.put("message", "hello");
//        review1.put("rating", "4");
//
//        this.mockMvc.perform(
//                post("/diner/createreview")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", token)
//                        .content(String.valueOf(new JSONObject(review1)))
//        );
//
//        Map<String, String> review2 = new HashMap<>();
//        review2.put("id", "0");
//        review2.put("dinerId", "0");
//        review2.put("eateryId", "1");
//        review2.put("message", "hahahaha");
//        review2.put("rating", "2");
//
//        this.mockMvc.perform(
//                MockMvcRequestBuilders
//                        .post("/diner/editreview")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", token)
//                        .content(String.valueOf(new JSONObject(review2)))
//        )
//                .andExpect(status().isOk());
//    }

    @Test
    public void testEditReview_invalidToken() throws Exception {
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

        Map<String, String> review1 = new HashMap<>();
        review1.put("id", "0");
        review1.put("dinerId", "0");
        review1.put("eateryId", "1");
        review1.put("message", "hello");
        review1.put("rating", "4");

        this.mockMvc.perform(
                post("/diner/createreview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(String.valueOf(new JSONObject(review1)))
        );

        Map<String, String> review2 = new HashMap<>();
        review2.put("id", "0");
        review2.put("dinerId", "0");
        review2.put("eateryId", "1");
        review2.put("message", "hahahaha");
        review2.put("rating", "2");

        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/diner/editreview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "InvalidToken")
                        .content(String.valueOf(new JSONObject(review2)))
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testEditReview_invalidEateryId() throws Exception {
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

        Map<String, String> review1 = new HashMap<>();
        review1.put("id", "0");
        review1.put("dinerId", "0");
        review1.put("eateryId", "1");
        review1.put("message", "hello");
        review1.put("rating", "4");

        this.mockMvc.perform(
                post("/diner/createreview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(String.valueOf(new JSONObject(review1)))
        );

        Map<String, String> review2 = new HashMap<>();
        review2.put("id", "0");
        review2.put("dinerId", "0");
        review2.put("eateryId", "10");
        review2.put("message", "hahahaha");
        review2.put("rating", "");

        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/diner/editreview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(String.valueOf(new JSONObject(review2)))
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testEditReview_invalidRating() throws Exception {
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

        Map<String, String> review1 = new HashMap<>();
        review1.put("id", "0");
        review1.put("dinerId", "0");
        review1.put("eateryId", "1");
        review1.put("message", "hello");
        review1.put("rating", "4");

        this.mockMvc.perform(
                post("/diner/createreview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(String.valueOf(new JSONObject(review1)))
        );

        Map<String, String> review2 = new HashMap<>();
        review2.put("id", "0");
        review2.put("dinerId", "0");
        review2.put("eateryId", "1");
        review2.put("message", "hahahaha");
        review2.put("rating", "20");

        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/diner/editreview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(String.valueOf(new JSONObject(review2)))
        )
                .andExpect(status().is4xxClientError());
    }
}
