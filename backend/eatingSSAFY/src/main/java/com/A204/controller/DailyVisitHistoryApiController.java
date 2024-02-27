package com.A204.controller;

import com.A204.dto.request.AddDailyVisitHistoryRequest;
import com.A204.dto.response.DailyVisitHistoryResponse;
import com.A204.service.DailyVisitHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/daily-visit")
public class DailyVisitHistoryApiController {
    private final DailyVisitHistoryService dailyVisitHistoryService;

    @PostMapping
    public ResponseEntity<Object> addDailyVisitHistory(@RequestParam(value = "userId") String userId, @Valid @RequestBody AddDailyVisitHistoryRequest request) {
        dailyVisitHistoryService.save(Long.parseLong(userId));
        return ResponseEntity.ok().body(HttpStatus.OK.value());
    }

    @GetMapping
    public ResponseEntity<DailyVisitHistoryResponse> findDailyVisitHistory(@RequestParam(value = "userId") String userId) {
        DailyVisitHistoryResponse dailyVisitHistoryResponse = dailyVisitHistoryService.findDailyVisitHistory(Long.parseLong(userId));
        return ResponseEntity.ok().body(dailyVisitHistoryResponse);
    }

}
