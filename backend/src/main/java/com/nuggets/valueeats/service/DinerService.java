package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.repository.DinerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.regex.*;

@Service
public class DinerService {
    
    @Autowired
    private DinerRepository dinerRepository;

    @Transactional
    public String registerDiner(Diner diner){
        try {
            // Checks if email exists in the database
            if (dinerRepository.existsByEmail(diner.getEmail())){
                return "Diner already exists in the database.";
            }else if(!isValidInput(diner)){
                return "Please fill in all required fields.";
            }else if(!isValidEmail(diner.getEmail())){
                return "Invalid Email Format.";
            }else if(!isValidPassword(diner.getPassword())){
                return "Password must be between 8 to 32 characters long, and contain a lower and uppercase character.";
            }else {
                diner.setId(null == dinerRepository.findMaxId()? 0 : dinerRepository.findMaxId() + 1);
                // Saves diner in database
                dinerRepository.save(diner);
                return "Diner record created successfully.";
            }
        }catch (Exception e){
            throw e;
        }
    }

    private static boolean isValidInput(Diner diner){
        return (diner.getAddress() != null && diner.getPassword() != null && diner.getUsername() != null && diner.getEmail() != null);
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

    public List<Diner> listDiner(){
        return dinerRepository.findAll();
    }
}
