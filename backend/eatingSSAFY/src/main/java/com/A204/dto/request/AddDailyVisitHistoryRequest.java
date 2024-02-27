package com.A204.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder
public record AddDailyVisitHistoryRequest(
        @Min(value = 1)
        @Max(value = 1)
        Integer cnt
) {
}
