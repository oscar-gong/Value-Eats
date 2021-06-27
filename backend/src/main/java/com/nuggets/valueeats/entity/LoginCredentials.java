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
    private Long id;
    @Column(unique=true)
    private String email;
    private String password;
}
