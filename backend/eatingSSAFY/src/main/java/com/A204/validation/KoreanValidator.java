package com.A204.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class KoreanValidator implements ConstraintValidator<KoreanCheck, String> {
    @Override
    public boolean isValid(String str, ConstraintValidatorContext context) {
        if (str == null || str.isEmpty()) return false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.getType(str.charAt(i)) != 5) return false;
        }
        return true;
    }
}
