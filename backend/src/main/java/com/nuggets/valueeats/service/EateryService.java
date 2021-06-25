package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.repository.EateryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.regex.*;

@Service
public class EateryService {
    
    @Autowired
    private EateryRepository eateryRepository;

    @Transactional
    public String registerEatery(Eatery eatery){
        try {
            // Checks if email exists in the database
            if (eateryRepository.existsByEmail(eatery.getEmail())){
                return "Eatery already exists in the database.";
            }else if(!isValidInput(eatery)){
                return "Please fill in all required fields.";
            }else if(!isValidEmail(eatery.getEmail())){
                return "Invalid Email Format.";
            }else if(!isValidPassword(eatery.getPassword())){
                return "Password must be between 8 to 32 characters long, and contain a lower and uppercase character.";
            }else {
                eatery.setId(null == eateryRepository.findMaxId()? 0 : eateryRepository.findMaxId() + 1);
                // Saves eatery in database
                eateryRepository.save(eatery);
                return "Eatery record created successfully.";
            }
        }catch (Exception e){
            throw e;
        }
    }

    private static boolean isValidInput(Eatery eatery){
        return (eatery.getAddress() != null && eatery.getPassword() != null && eatery.getName() != null && eatery.getEmail() != null);
    }

    // Checks if email is valid (can probs use something other than regex)
    private static boolean isValidEmail(String email){
        String regex = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";
        return Pattern.matches(regex, email);
    }

    // Checks if password is: between 8 to 32 char long, contains one uppercase and lowercase
    private static boolean isValidPassword(String password){
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\w\\W]{8,32}$";
        return Pattern.matches(regex, password);
    }

    public List<Eatery> listEatery(){
        return eateryRepository.findAll();
    }
}
