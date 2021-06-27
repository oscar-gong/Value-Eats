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
    private Long id;
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<UserToken> userTokens = new HashSet<>();

    public void addToken(final UserToken userToken) {
        userTokens.add(userToken);
    }

    public void removeToken(final String token) {
        userTokens.removeIf(a -> a.getToken().equals(token));
    }
}
