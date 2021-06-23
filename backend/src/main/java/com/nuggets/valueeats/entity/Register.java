package com.nuggets.valueeats.entity;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;

@Entity(name = "register")
public class Register {
    @Id
    @SequenceGenerator(
        name = "register_sequence",
        sequenceName = "register_sequence",
        allocationSize = 1
    )
    @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String password;

    //Add methods to them.
}
