package com.A204.controller;

import com.A204.dto.request.AddAmountListRequest;
import com.A204.dto.response.AmountResponse;
import com.A204.service.AmountService;
import com.A204.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/amount")
public class AmountApiController {
    private final AmountService amountService;

    @PostMapping
    public ResponseEntity<Object> addAmount(@RequestParam(value = "cameraId") Integer cameraId, @Valid @RequestBody AddAmountListRequest request) {
        amountService.save(request, cameraId);
        return ResponseEntity.ok().body(HttpStatus.OK.value());
    }

    @GetMapping
    public ResponseEntity<List<AmountResponse>> findAmountList() {
        return ResponseEntity.ok().body(amountService.findAmountList());
    }
}
