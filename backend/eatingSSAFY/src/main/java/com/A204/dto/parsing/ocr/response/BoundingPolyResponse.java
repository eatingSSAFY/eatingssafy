package com.A204.dto.parsing.ocr.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

import java.util.List;

@Builder
@JsonSerialize
public record BoundingPolyResponse(
        List<VerticesResponse> vertices
) {
}
