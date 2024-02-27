package com.A204.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record FoodResponse(
        String content,
        List<String> allergyList
) {
}

