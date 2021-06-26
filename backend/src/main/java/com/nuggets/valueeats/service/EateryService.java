package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.repository.EateryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class EateryService {
    @Autowired
    private EateryRepository eateryRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public String registerEatery(Eatery eatery) {
        if (!isValidInput(eatery)) {
            return "Please fill in all required fields.";
        }

        String result = userService.validInputChecker(eatery);
        if (result != null) {
            return result;
        }

        if (eateryRepository.existsByEmail(eatery.getEmail())) {
            return "Eatery already exists in the database.";
        }

        eatery.setId(null == eateryRepository.findMaxId() ? 0 : eateryRepository.findMaxId() + 1);
        eateryRepository.save(eatery);

        return "Eatery record created successfully.";
    }

    private boolean isValidInput(final Eatery eatery) {
        return (eatery.getAddress() != null &&
                        eatery.getPassword() != null &&
                        eatery.getName() != null &&
                        eatery.getEmail() != null);
    }
}
