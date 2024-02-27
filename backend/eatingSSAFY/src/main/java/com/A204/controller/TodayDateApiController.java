package com.A204.controller;

import com.A204.dto.response.TodayDateResponse;
import com.A204.service.TodayDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TodayDateApiController {
    private final TodayDateService todayDateService;

    @GetMapping("/today-date")
    public ResponseEntity findTodayDate() {
        return ResponseEntity.ok().body(TodayDateResponse.builder().todayDate(todayDateService.findTodayDate()).build());
    }

}
