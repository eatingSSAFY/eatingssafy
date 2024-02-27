package com.A204.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record AddAmountListRequest(
        List<AddAmountRequest> data
) {
}
