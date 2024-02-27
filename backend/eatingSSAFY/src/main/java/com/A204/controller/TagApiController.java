package com.A204.controller;

import com.A204.dto.request.AddTagRequest;
import com.A204.service.TagService;
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
public class TagApiController {
    private final TagService tagService;

    @PostMapping("/tag")
    public ResponseEntity<Object> addTag(@Valid @RequestBody AddTagRequest request) {
        tagService.save(request);
        return ResponseEntity.ok().body(HttpStatus.OK.value());
    }
}
