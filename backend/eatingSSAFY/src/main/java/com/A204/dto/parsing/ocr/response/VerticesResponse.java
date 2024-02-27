package com.A204.dto.parsing.ocr.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

@Builder
@JsonSerialize
public record VerticesResponse(
        Float x,
        Float y
) {
}
