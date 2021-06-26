package com.nuggets.valueeats.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Data
public class User extends LoginCredentials {
    @Id
    private long id;
    private String address;
    @OneToMany
    private Set<Token> tokens = new HashSet<>();
}
