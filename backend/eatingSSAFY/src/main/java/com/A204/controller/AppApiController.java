package com.A204.controller;

import com.A204.domain.Notification;
import com.A204.dto.request.AppTokenRequest;
import com.A204.dto.response.NotificationConfigResponse;
import com.A204.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AppApiController {
    private final NotificationService notificationService;

    @PostMapping("/app-token")
    public ResponseEntity<?> upsertAppToken(@Valid @RequestBody AppTokenRequest request) {
        notificationService.upsert(request);
        return ResponseEntity.ok(HttpStatus.OK.value());
    }

    @GetMapping("/notification/config")
    public ResponseEntity<NotificationConfigResponse> getNotificationConfig(@RequestParam String appToken) {
        Notification notification = notificationService.findNotification(appToken);
        return ResponseEntity.ok(
                NotificationConfigResponse.builder()
                        .preferenceNoti(notification.getPreference())
                        .amountNoti(notification.getAmount())
                        .build()
        );
    }
}
