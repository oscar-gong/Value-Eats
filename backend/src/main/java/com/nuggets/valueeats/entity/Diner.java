package com.nuggets.valueeats.entity;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Diner {

    @Id
    private int id;

    private String username;
    private String email;
    private String address;
    private String password;
    
    public Diner() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Diner{" +
                "id=" + id +
                ", name='" + username + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
