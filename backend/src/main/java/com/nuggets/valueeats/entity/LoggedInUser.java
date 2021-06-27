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
public abstract class LoggedInUser extends User {
    @Id
    private long id;
    @OneToMany
    private Set<Token> tokens = new HashSet<>();
}
