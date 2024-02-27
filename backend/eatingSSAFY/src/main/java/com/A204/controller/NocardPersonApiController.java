package com.A204.controller;

import com.A204.dto.request.AddNocardPersonRequest;
import com.A204.service.NocardPersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class NocardPersonApiController {
    private final NocardPersonService nocardPersonService;

    @PostMapping("/nocard-person")
    public ResponseEntity<Object> addNocardPerson(@Valid @RequestBody AddNocardPersonRequest request) {
        nocardPersonService.save(request);
        return ResponseEntity.ok().body(HttpStatus.OK.value());
    }
}
