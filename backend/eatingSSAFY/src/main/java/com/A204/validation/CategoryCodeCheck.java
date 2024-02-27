package com.A204.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoryCodeValidator.class)
public @interface CategoryCodeCheck {
    String message() default "적합한 메뉴 코드가 아닙니다.";

    Class[] groups() default {};

    Class[] payload() default {};
}
