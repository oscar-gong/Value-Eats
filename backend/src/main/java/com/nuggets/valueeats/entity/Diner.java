package com.nuggets.valueeats.entity;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;

@Entity (name = "diner")
public class Diner {
    @Id
    @SequenceGenerator(
        name = "diner_sequence",
        sequenceName = "diner_sequence",
        allocationSize = 1
    )
    @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String password;

    Diner (Long id, String username,  String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getId () {
        return this.id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getUsername () {
        return this.username;
    }

    public void setUsername (String username) {
        this.username = username;
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
}
