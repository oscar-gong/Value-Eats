package com.nuggets.valueeats.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public final class UserToken extends Token {
    private Timestamp dateCreated;

    public UserToken(final Long id, final String token) {
        this.setId(id);
        this.setToken(token);
        this.dateCreated = new Timestamp(System.currentTimeMillis());
    }
}
