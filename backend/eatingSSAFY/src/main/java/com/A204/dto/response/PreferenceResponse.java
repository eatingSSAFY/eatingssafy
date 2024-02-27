package com.A204.dto.response;

import lombok.Builder;

@Builder
public record PreferenceResponse(
        int foodId, // food id
        String content // food content
) {
}
