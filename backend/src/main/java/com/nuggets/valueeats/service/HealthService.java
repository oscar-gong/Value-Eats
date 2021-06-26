package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.repository.EateryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthService {
    @Autowired
    private DinerRepository dinerRepository;
    @Autowired
    private EateryRepository eateryRepository;

    public List<Diner> listDiner() {
        return dinerRepository.findAll();
    }

    public List<Eatery> listEatery() {
        return eateryRepository.findAll();
    }
}
