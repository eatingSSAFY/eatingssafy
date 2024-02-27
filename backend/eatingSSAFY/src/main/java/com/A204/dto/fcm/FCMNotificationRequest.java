package com.A204.dto.fcm;

import lombok.Builder;

@Builder
public record FCMNotificationRequest(
        String token,
        String title,
        String body
) {
}
