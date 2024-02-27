package com.A204.dto.parsing.ocr.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

import java.util.List;

@Builder
@JsonSerialize
public record OCRResponse(
        String version,
        String requestId,
        Long timestamp,
        List<ImageResponse> images
) {
}
