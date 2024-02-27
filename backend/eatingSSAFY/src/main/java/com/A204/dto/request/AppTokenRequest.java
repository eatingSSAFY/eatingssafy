package com.A204.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AppTokenRequest(
        @NotNull(message = "app token은 NULL을 허용하지 않습니다.")
        String appToken,
        Long userId,
        Boolean preferenceNotification,
        Boolean amountNotification
) {
}
