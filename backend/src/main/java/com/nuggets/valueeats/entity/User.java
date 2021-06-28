package com.nuggets.valueeats.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Data
public class User {
    @Id
    private Long id;
    @Column(unique=true)
    private String email;
    private String password;
    private String alias;
    private String address;
    private String token;
}
