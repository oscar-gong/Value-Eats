package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Diner;

import com.nuggets.valueeats.repository.DinerRepository;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class DinerService {
	@Autowired
  DinerRepository dinerRepository;

  public List<Diner> findAllDiners () {
    List<Diner> dinerList = dinerRepository.findAll();
    return dinerList;
  }

  public void registerDiner(@RequestBody Diner diner) {
    dinerRepository.save(diner);
  }
}
