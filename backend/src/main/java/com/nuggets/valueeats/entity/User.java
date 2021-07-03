package com.nuggets.valueeats.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private String profilePic;
}
