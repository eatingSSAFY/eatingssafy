package com.A204.dto.request;

import lombok.Builder;

@Builder
public record PageRequest (
        Integer page,
        Integer pageSize
) {
}
