package com.A204.dto.parsing.ocr.request;

import lombok.Builder;

@Builder
public record ImageRequest(
        String format,
        String name,
        String data
) {
}
