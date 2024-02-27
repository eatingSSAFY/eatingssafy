package com.A204.dto.parsing.ocr.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

import java.util.List;

@Builder
@JsonSerialize
public record OCRRequest(
        String version, // 버전 정보 필수로 V1, 혹은 V2를 입력
        String requestId, // API 호출 UUID
        Long timestamp, // API 호출 Timestamp 값
        String lang, // ko
        List<ImageRequest> images
) {
}
