package com.nuggets.valueeats.entity;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;

@Entity (name = "eatery")
public class Eatery {
    @Id
    @SequenceGenerator(
        name = "eatery_sequence",
        sequenceName = "eatery_sequence",
        allocationSize = 1
    )
    @GeneratedValue
    private Long id;
    private String eateryname;
    private String email;
    private String password;
    private String address;
    private String cuisines;

    Eatery (Long id, String eateryname,  String email, String password, String address, String cuisines) {
        this.id = id;
        this.eateryname = eateryname;
        this.email = email;
        this.password = password;
        this.address = address;
        this.cuisines = cuisines;
    }

    public Long getId () {
        return this.id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getEateryname () {
        return this.eateryname;
    }

    public void setEateryname (String eateryname) {
        this.eateryname = eateryname;
    }

    public String getEmail () {
        return this.email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getPassword () {
        return this.password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public String getAddress () {
        return this.address;
    }

    public void setAddress (String address) {
        this.address = address;
    }

    public String getCuisines () {
        return this.cuisines;
    }

    public void setCuisines (String cuisines) {
        this.cuisines = cuisines;
    }
}
