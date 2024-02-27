package com.A204.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@JsonSerialize
public record PreferenceRequest(
        @NotNull(message = "Null 허용하지 않습니다.")
        Integer foodId,
        @NotNull(message = "Null 허용하지 않습니다.")
        Integer value
) {
    @AssertTrue(message = "value 데이터는 -1, 0, 1만 가능합니다.")
    public boolean isValueData() {
        return this.value != null && this.value <= 1 && this.value >= -1; // -1: 싫어요, 0: 삭제, 1: 좋아요
    }
}
