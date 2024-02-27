package com.A204.controller;

import com.A204.dto.request.PageRequest;
import com.A204.dto.request.PreferenceRequest;
import com.A204.dto.response.MenuPreferenceResponse;
import com.A204.service.PreferenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/preference")
public class PreferenceApiController {
    private final PreferenceService preferenceService;

    @PostMapping
    public ResponseEntity<Object> addPreference(@RequestParam(value = "userId") String userId, @Valid @RequestBody PreferenceRequest request) {
        preferenceService.save(Long.parseLong(userId), request);
        return ResponseEntity.ok(HttpStatus.OK.value());
    }

    @GetMapping("/list")
    public ResponseEntity<List<?>> getPreferenceList(
            @RequestParam String userId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        PageRequest request = PageRequest.builder().page(page == null ? 0 : page).pageSize(pageSize == null ? 10 : pageSize).build();
        return ResponseEntity.ok(preferenceService.findList(Long.parseLong(userId), request));
    }

    @GetMapping
    public ResponseEntity<List<MenuPreferenceResponse>> getPreference(@RequestParam(value = "userId", required = false) String userId) {
        return ResponseEntity.ok(preferenceService.findMenuPreferenceList(userId == null ? null : Long.valueOf(userId)));
    }
}
