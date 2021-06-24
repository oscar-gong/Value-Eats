package com.nuggets.valueeats.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Diner implements User {
    String username;
    String email;
    String password;
}
