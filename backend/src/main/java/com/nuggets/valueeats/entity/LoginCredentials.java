package com.nuggets.valueeats.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class LoginCredentials {
    @Id
    private long id;
    @Column(unique=true)
    private String email;
    private String password;

    public long getId () {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
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
