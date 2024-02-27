package com.A204.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = KoreanValidator.class)
public @interface KoreanCheck{
    String message() default "한국어가 아닙니다.";

    Class[] groups() default {};

    Class[] payload() default {};
}
