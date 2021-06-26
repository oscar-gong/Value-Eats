package com.nuggets.valueeats.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Data
public class Token {
    @Id
    private long id;
    private String token;
    private Timestamp dateCreated;

    public Token(String token) {
        this.token = token;
        this.dateCreated = new Timestamp(System.currentTimeMillis());
    }
}
