package com.A204.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Pattern;

@JsonSerialize
public record AddTagRequest(
        //added: 카드 태그 인식 시, 1 들어온다. 잘못 들어올 경우: 0
        @Pattern(regexp = "1", message = "added is not 1")
        String added
) {
}
