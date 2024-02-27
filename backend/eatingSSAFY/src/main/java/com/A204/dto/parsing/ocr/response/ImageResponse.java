package com.A204.dto.parsing.ocr.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

import java.util.List;

@Builder
@JsonSerialize
public record ImageResponse(
        String uid,
        String name,
        String inferResult,
        String message,
        List<FieldResponse> fields
) {
}
