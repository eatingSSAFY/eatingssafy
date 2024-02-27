package com.A204.controller;

import com.A204.dto.response.UserResponse;
import com.A204.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserApiController {
    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<UserResponse> findUser(@RequestParam(value = "userId") String userId) {
        return ResponseEntity.ok().body(userService.findUserByKakaoId(Long.parseLong(userId)));
    }
}
