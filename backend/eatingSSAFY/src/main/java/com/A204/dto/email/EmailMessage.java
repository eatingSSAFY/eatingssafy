package com.A204.dto.email;

import lombok.Builder;

@Builder
public record EmailMessage(
        String to,
        String subject,
        String text,
        String attachFileName,
        String attachFilePath
) {
}
