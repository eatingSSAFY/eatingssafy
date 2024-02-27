package com.A204.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@JsonSerialize
public record MenuBatchRequest(
        String restaurant,
        String fileUrl
) {
}
