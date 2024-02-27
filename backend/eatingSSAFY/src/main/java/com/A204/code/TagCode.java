package com.A204.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TagCode {
    DUPLICATED_TAGGED(-1),
    TAGGED(1);

    private final Integer tagged;
}
