package com.A204.dto.request;

import com.A204.validation.KoreanCheck;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AddNocardPersonRequest(
        @NotBlank(message = "빈칸을 허용하지 않습니다.")
        @KoreanCheck
        String personName,
        @NotEmpty(message = "NULL과 빈 문자열을 허용하지 않습니다.")
        String personId
) {
}
