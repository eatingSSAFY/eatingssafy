package com.A204.validation;

import com.A204.code.CategoryCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CategoryCodeValidator implements ConstraintValidator<CategoryCodeCheck, String> {
    @Override
    public boolean isValid(String str, ConstraintValidatorContext context) {
        return CategoryCode.getByTitle(str) != null;
    }
}
