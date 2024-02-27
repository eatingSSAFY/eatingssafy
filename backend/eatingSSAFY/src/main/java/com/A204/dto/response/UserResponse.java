package com.A204.dto.response;

import lombok.Builder;

@Builder
public record UserResponse (
    Long kakaoId,
    String personNickname
)
{
}
