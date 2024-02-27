package com.A204.dto.parsing.ocr.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

@Builder
@JsonSerialize
public record FieldResponse(
        String valueType,
        String inferText,
        Float inferConfidence,
        String type,
        Boolean lineBreak,
        BoundingPolyResponse boundingPoly
) {
}
