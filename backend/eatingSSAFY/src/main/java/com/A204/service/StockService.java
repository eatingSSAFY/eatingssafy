package com.A204.service;

import com.A204.code.CategoryCode;
import com.A204.domain.Stock;
import com.A204.repository.StockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockService {
    private final ExcelService excelService;
    private final StockRepository stockRepository;

    /**
     * fileUrl에서 엑셀 파일 다운로드 후, 발주(초기 재고) 데이터 파싱
     */
    @Transactional
    public void regist(String fileUrl) {
        List<Stock> dbList = new ArrayList<>();
        Map<Date, Map<CategoryCode, Integer>> stockGroupByDate = excelService.readStockExcel(fileUrl);
        for (Date date : stockGroupByDate.keySet()) {
            for (CategoryCode categoryCode : stockGroupByDate.get(date).keySet()) {
                dbList.add(Stock.builder()
                        .categoryCode(categoryCode)
                        .servingAt(date)
                        .cnt(stockGroupByDate.get(date).get(categoryCode))
                        .build());
            }
        }

        stockRepository.saveAll(dbList);
    }
}
