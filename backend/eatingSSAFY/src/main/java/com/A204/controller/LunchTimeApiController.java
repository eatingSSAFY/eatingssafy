package com.A204.controller;

import com.A204.dto.response.LunchTimeResponse;
import com.A204.service.LunchTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/lunchtime")
public class LunchTimeApiController {
    private final LunchTimeService lunchTimeService;

    @GetMapping
    public ResponseEntity<List<LunchTimeResponse>> findLunchTimeList() {
        return ResponseEntity.ok().body(
                lunchTimeService.findLunchTimeList()
        );
    }
}
