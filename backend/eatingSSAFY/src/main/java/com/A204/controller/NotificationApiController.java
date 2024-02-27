package com.A204.controller;

import com.A204.service.AmountService;
import com.A204.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notification")
public class NotificationApiController {
    private final AmountService amountService;
    private final NotificationService notificationService;

    @PostMapping("/preference")
    public ResponseEntity<?> sendPreference() {
        notificationService.sendPreference();
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/amount")
    public ResponseEntity<?> sendAmount() {
        notificationService.amountNotificationCheck(amountService.findAmountList());
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
