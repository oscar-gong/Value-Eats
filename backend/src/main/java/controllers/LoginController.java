package controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginInUser(String username, String paassword) {
        // Need to read info from the datbase
    }
}
