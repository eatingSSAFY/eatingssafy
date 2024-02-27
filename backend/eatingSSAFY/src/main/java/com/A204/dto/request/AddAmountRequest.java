package com.A204.dto.request;

import com.A204.validation.CategoryCodeCheck;
import jakarta.validation.constraints.AssertTrue;
import lombok.Builder;

@Builder
public record AddAmountRequest(
        @CategoryCodeCheck
        String name,
        Integer value
) {
    @AssertTrue(message = "value 데이터는 0이 되어서는 안됩니다.")
    public boolean isValue() {
        return this.value != null && this.value != 0;
    }
}
