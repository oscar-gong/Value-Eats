package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.User;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.repository.EateryRepository;
import com.nuggets.valueeats.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class HealthService {
    @Autowired
    private DinerRepository dinerRepository;
    @Autowired
    private EateryRepository eateryRepository;
    @Autowired
    private UserRepository<User> userRepository;

    public List<Diner> listDiner() {
        return dinerRepository.findAll();
    }

    public List<Eatery> listEatery() {
        return eateryRepository.findAll();
    }

    public List<User> listUser() {
        return userRepository.findAll();
    }
}
