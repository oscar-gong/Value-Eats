package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.repository.DinerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DinerService {
    
    @Autowired
    private DinerRepository dinerRepository;

    @Transactional
    public String registerDiner(Diner diner){
        try {
            if (!dinerRepository.existsByEmail(diner.getEmail())){
                diner.setId(null == dinerRepository.findMaxId()? 0 : dinerRepository.findMaxId() + 1);
                dinerRepository.save(diner);
                return "Diner record created successfully.";
            }else {
                return "Diner already exists in the database.";
            }
        }catch (Exception e){
            throw e;
        }
    }


}
