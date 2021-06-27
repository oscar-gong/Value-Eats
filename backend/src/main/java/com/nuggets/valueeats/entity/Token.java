package com.nuggets.valueeats.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public final class Token {
    @Id
    private Long id;
    @Column(unique = true)
    private String token;
    private Timestamp dateCreated;

    public Token(Long id, String token) {
        this.id = id;
        this.token = token;
        this.dateCreated = new Timestamp(System.currentTimeMillis());
    }
}
