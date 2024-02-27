package com.A204.dto.response;

import lombok.Builder;

@Builder
public record AmountResponse(
        String category,
        int value,
        int stock,
        float velocity,
        float servedAmountPerMin
) {
}
