package com.nuggets.valueeats.controller.decorator;

import com.nuggets.valueeats.controller.exception.InvalidTokenException;
import com.nuggets.valueeats.entity.User;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.repository.EateryRepository;
import com.nuggets.valueeats.repository.UserRepository;
import com.nuggets.valueeats.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private DinerRepository dinerRepository;

    @Autowired
    private EateryRepository eateryRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Before("@annotation(com.nuggets.valueeats.controller.decorator.CheckToken)")
    public void checkSession(JoinPoint joinPoint) {
        if (joinPoint.getArgs().length == 2) {
            if (jwtUtils.decode((String) joinPoint.getArgs()[1]) == null || !userRepository.existsByToken((String) joinPoint.getArgs()[1])) {
                throw new InvalidTokenException("Token is invalid");
            }
        }
    }

    @Before("@annotation(com.nuggets.valueeats.controller.decorator.CheckToken)")
    public void checkDinerSession(JoinPoint joinPoint) {
        if (joinPoint.getArgs().length == 2) {
            if (jwtUtils.decode((String) joinPoint.getArgs()[1]) == null || !dinerRepository.existsByToken((String) joinPoint.getArgs()[1])) {
                throw new InvalidTokenException("Token is not a diner token");
            }
        }
    }

    @Before("@annotation(com.nuggets.valueeats.controller.decorator.CheckToken)")
    public void checkEaterySession(JoinPoint joinPoint) {
        if (joinPoint.getArgs().length == 2) {
            if (jwtUtils.decode((String) joinPoint.getArgs()[1]) == null || !eateryRepository.existsByToken((String) joinPoint.getArgs()[1])) {
                throw new InvalidTokenException("Token is not an eatery token");
            }
        }
    }
}
