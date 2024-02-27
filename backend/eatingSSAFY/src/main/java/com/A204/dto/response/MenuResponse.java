package com.A204.dto.response;

import lombok.Builder;

import java.sql.Date;
import java.util.List;

@Builder
public record MenuResponse(
        Date servingAt,
        String category,
        List<FoodResponse> foodList
) {
}
