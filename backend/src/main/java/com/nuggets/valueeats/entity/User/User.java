package com.nuggets.valueeats.entity.User;

import lombok.Data;

public interface User {
    String username = null;
    String email = null;
    String password = null;

    String getUsername();
    String getEmail();
    String getPassword();
}
