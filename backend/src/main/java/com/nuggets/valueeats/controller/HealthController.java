package com.nuggets.valueeats.controller;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.Review;
import com.nuggets.valueeats.entity.User;
import com.nuggets.valueeats.service.HealthService;
import com.nuggets.valueeats.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = ControllerConstants.URL)
@RestController
public final class HealthController {
    @Autowired
    private HealthService healthService;
    @Autowired
    private JwtUtils jwtUtils;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "Test";
    }

    @RequestMapping(value = "/encode/{string}", method = RequestMethod.GET)
    public String encode(@PathVariable final String string) {
        String a = jwtUtils.encode(string);
        String b = jwtUtils.decode(a);

        return "String: " + string + "\nEncode: " + a + "\nDecode: " + b;
    }

    @RequestMapping(value = "list/diners", method = RequestMethod.GET)
    public List<Diner> listDiner() {
        return healthService.listDiner();
    }

    @RequestMapping(value = "list/eateries", method = RequestMethod.GET)
    public List<Eatery> listEatery() {
        return healthService.listEatery();
    }

    @RequestMapping(value = "list/users", method = RequestMethod.GET)
    public List<User> listUsers() {
        return healthService.listUser();
    }

    @RequestMapping(value = "list/reviews", method = RequestMethod.GET)
    public List<Review> listReviews() {
        return healthService.listReview();
    }

    @RequestMapping(value = "list/cuisine", method = RequestMethod.GET)
    public List listCuisines() {
        return healthService.listCuisines();
    }
}
