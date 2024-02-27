package com.A204.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record StockBatchRequest(
        String fileUrl
) {
}
