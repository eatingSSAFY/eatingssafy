package com.A204.dto.parsing.ocr.batch;

import com.A204.dto.parsing.ocr.response.FieldResponse;
import com.A204.dto.parsing.ocr.response.VerticesResponse;
import lombok.Builder;

/**
 * OCR 기술로 인식된 텍스트 + 레이블 박스 정보
 */
@Builder
public record TextLabelBox(
        String text,
        float left,
        float right,
        float top,
        float down
) implements Comparable<TextLabelBox> {


    public static TextLabelBox generate(FieldResponse o) {
        return TextLabelBox.builder()
                .text( o.inferText().trim())
                .left((float) o.boundingPoly().vertices().stream().mapToDouble(VerticesResponse::x).min().orElse(0))
                .right((float) o.boundingPoly().vertices().stream().mapToDouble(VerticesResponse::x).max().orElse(0))
                .top((float) o.boundingPoly().vertices().stream().mapToDouble(VerticesResponse::y).max().orElse(0))
                .down((float) o.boundingPoly().vertices().stream().mapToDouble(VerticesResponse::y).min().orElse(0))
                .build();
    }

    @Override
    public int compareTo(TextLabelBox o) {
        int leftDiff = (int) Math.abs(left - o.left);
        int topDiff = (int) Math.abs(top - o.top);

        if (leftDiff <= 5 && topDiff <= 5) return 0;
        if (topDiff <= 5) return left > o.left ? 1 : -1;
        return top > o.top ? 1 : -1;
    }

    /**
     * 한 줄 인식 허용 범위 판독
     */
    public boolean allowableOneLine(TextLabelBox o) {
        return Math.abs(top - o.top) <= 5 && Math.abs(down - o.down) <= 5;
    }
}
