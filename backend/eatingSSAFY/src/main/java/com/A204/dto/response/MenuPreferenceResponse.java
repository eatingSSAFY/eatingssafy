package com.A204.dto.response;

import lombok.Builder;

import java.sql.Date;

@Builder
public record MenuPreferenceResponse(
        Date servingAt,
        String category,
        int foodId,
        long likeCnt,
        long dislikeCnt,

        // 로그인 유저인 경우 포함
        Boolean like
) {
}
