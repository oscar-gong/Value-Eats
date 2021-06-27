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
public abstract class User extends LoginCredentials {
    @Id
    private Long id;
    @Column(unique=true)
    private String alias;
    private String address;
    private String token;
}
