package com.A204.controller;

import com.A204.dto.response.MenuResponse;
import com.A204.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MenuApiController {
    private final MenuService menuService;

    @GetMapping("/menu")
    public ResponseEntity<List<MenuResponse>> findMenuList() {
        List<MenuResponse> menuList = menuService.findMenuList();
        return ResponseEntity.ok().body(menuList);
    }
}
