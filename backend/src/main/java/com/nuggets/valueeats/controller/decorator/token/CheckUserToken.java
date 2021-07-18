package com.nuggets.valueeats.controller.decorator.token;

import com.nuggets.valueeats.controller.exception.InvalidTokenException;
import com.nuggets.valueeats.entity.User;
import com.nuggets.valueeats.repository.EateryRepository;
import com.nuggets.valueeats.repository.UserRepository;
import com.nuggets.valueeats.utils.JwtUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckUserToken {
}


