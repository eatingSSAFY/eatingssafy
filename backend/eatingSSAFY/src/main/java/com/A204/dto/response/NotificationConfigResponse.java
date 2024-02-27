package com.A204.dto.response;

import lombok.Builder;

@Builder
public record NotificationConfigResponse(
        Boolean preferenceNoti,
        Boolean amountNoti
) {
}
