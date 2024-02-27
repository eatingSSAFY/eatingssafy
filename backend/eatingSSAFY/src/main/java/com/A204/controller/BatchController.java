package com.A204.controller;

import com.A204.code.RestaurantCode;
import com.A204.dto.email.EmailMessage;
import com.A204.dto.request.MenuBatchRequest;
import com.A204.dto.request.StockBatchRequest;
import com.A204.service.EmailService;
import com.A204.service.MenuService;
import com.A204.service.NocardPersonService;
import com.A204.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
@PropertySource("classpath:application.yaml")
public class BatchController {

    private final MenuService menuService;
    private final EmailService emailService;
    private final StockService stockService;
    private final NocardPersonService nocardPersonService;

    @Value("${admin.email}")
    private String adminEmail;

    /**
     * 한 주 메뉴 자동 입력 API
     */
    @PostMapping("/menu")
    public ResponseEntity<Object> regist(@RequestBody MenuBatchRequest request) {
        switch (Objects.requireNonNull(RestaurantCode.getRestaurantCodeByTitle(request.restaurant()))) {
            case FLOOR_10 -> menuService.regist10(request.fileUrl());
            case FLOOR_20 -> menuService.regist20(request.fileUrl());
        }
        return ResponseEntity.ok().body(HttpStatus.OK.value());
    }

    /**
     * 초기 재고 자동 입력 API
     */
    @PostMapping("/stock")
    public ResponseEntity<Object> registStock(@RequestBody StockBatchRequest request) {
        stockService.regist(request.fileUrl());
        return ResponseEntity.ok().body(HttpStatus.OK.value());
    }

    /**
     * 당일 입력된 카드 미소지자 데이터를 엑셀 형식으로 가공, 이메일 송신
     */
    @PostMapping("/nocard-person")
    public ResponseEntity<Object> autoNoCardPerson() {
        String filePath = nocardPersonService.export();
        LocalDate now = LocalDate.now();
        //영양사님께 이메일 전송
        emailService.send(
                EmailMessage.builder()
                        .subject(now + "일자 카드 미소지자 데이터")
                        .text(now + "일자 카드 미소지자 데이터 송부드립니다.")
                        .to(adminEmail)
                        .attachFileName(filePath.substring(filePath.lastIndexOf('/') + 1))
                        .attachFilePath(filePath)
                        .build()
        );

        return ResponseEntity.ok().body(HttpStatus.OK.value());
    }
}
