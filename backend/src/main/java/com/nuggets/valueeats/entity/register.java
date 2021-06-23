package com.nuggets.valueeates.entity;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;

@entity (name = "register")
public class register {
    @Id
    @SequenceGenerator(
        name = "register_sequence",
        sequenceName = "register_sequence",
        allocationSize = 1;
    }
    @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String password;
    
    //Add methods to them.
}
