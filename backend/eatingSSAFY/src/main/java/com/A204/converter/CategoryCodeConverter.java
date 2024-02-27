package com.A204.converter;

import com.A204.code.CategoryCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class CategoryCodeConverter implements AttributeConverter<CategoryCode, String> {
    @Override
    public String convertToDatabaseColumn(CategoryCode categoryCode) {
        if (categoryCode == null) return null;
        return categoryCode.getTitle();
    }

    @Override
    public CategoryCode convertToEntityAttribute(String s) {
        if (s == null) return null;
        return Stream.of(CategoryCode.values())
                .filter(o -> o.getTitle().equals(s))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
