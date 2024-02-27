package com.A204.service;

import com.A204.dto.parsing.ocr.request.ImageRequest;
import com.A204.dto.parsing.ocr.request.OCRRequest;
import com.A204.dto.parsing.ocr.response.OCRResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OCRService {
    private final Logger logger = LoggerFactory.getLogger(OCRService.class);

    private final WebClient ocrWebClient;

    /**
     * OCR API를 사용하여, 텍스트 추출
     */
    public OCRResponse requestOCR(String base64Image) {
        logger.debug("OCR Request");
        Timestamp timestamp = new Timestamp(new Date().getTime());
        return ocrWebClient.post()
                .uri("/")
                .bodyValue(
                        OCRRequest.builder()
                                .version("V2")
                                .requestId(timestamp.toString())
                                .lang("ko")
                                .timestamp(timestamp.toInstant().getEpochSecond())
                                .images(
                                        List.of(
                                                ImageRequest.builder()
                                                        .format("jpg")
                                                        .name("ocr")
                                                        .data(base64Image)
                                                        .build()
                                        )
                                )
                                .build()
                )
                .retrieve()
                .bodyToMono(OCRResponse.class)
                .block();
    }
}
