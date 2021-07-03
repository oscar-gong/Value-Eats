package com.nuggets.valueeats.controller.decorator;

import com.nuggets.valueeats.controller.exception.InvalidTokenException;
import com.nuggets.valueeats.entity.User;
import com.nuggets.valueeats.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
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
public @interface CheckToken {
}

@Slf4j
@Aspect
@Component
class CheckTokenAspect<T extends User> {
    @Autowired
    private UserRepository<T> userRepository;

    @Before("@annotation(com.nuggets.valueeats.controller.decorator.CheckToken)")
    public void checkSession(JoinPoint joinPoint) {
        if (joinPoint.getArgs().length == 2) {
            if (!userRepository.existsByToken((String) joinPoint.getArgs()[1])) {
                throw new InvalidTokenException("Token is invalid");
            }
        }
    }
}
