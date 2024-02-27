package com.A204.service;

import com.A204.code.CategoryCode;
import com.A204.domain.Amount;
import com.A204.dto.request.AddAmountListRequest;
import com.A204.dto.response.AmountResponse;
import com.A204.repository.AmountRepository;
import com.A204.repository.NocardPersonRepository;
import com.A204.repository.StockRepository;
import com.A204.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AmountService {
    private final AmountRepository amountRepository;
    private final TagRepository tagRepository;
    private final NocardPersonRepository nocardPersonRepository;
    private final StockRepository stockRepository;

    private final Logger logger = LoggerFactory.getLogger(ImageService.class);

    @Transactional
    public void save(AddAmountListRequest request, Integer cameraId) {
        List<Amount> amountList = new ArrayList<>();
        Date servingAt = Date.valueOf(LocalDate.now());
        Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
        request.data().forEach(o ->
                amountList.add(Amount.builder()
                        .cameraId(cameraId)
                        .categoryCode(CategoryCode.valueOf(o.name()))
                        .cnt(o.value())
                        .createdAt(createdAt)
                        .servingAt(servingAt)
                        .build()));
        amountRepository.saveAll(amountList);
    }

    @Transactional
    public List<AmountResponse> findAmountList() {
        List<AmountResponse> amountResponseList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        Date servingAt = Date.valueOf(today);
        LocalDateTime timeAtEleven = today.atTime(11, 00, 00);
        Duration duration = Duration.between(timeAtEleven.toLocalTime(), LocalDateTime.now().toLocalTime());
        long intervalMin = duration.toMinutes();

        // 오늘자 00:00 ~ 23:59 사이에 생성된 NocardPerson 인원 검색
        int cntNocardPerson = nocardPersonRepository.findNocardPersonCntOfDay(
                Timestamp.valueOf(today.atStartOfDay()),
                Timestamp.valueOf(today.plusDays(1).atStartOfDay().minusSeconds(1)));
        int cntTag = tagRepository.findTagCntOfDay(
                Timestamp.valueOf(today.atStartOfDay()),
                Timestamp.valueOf(today.plusDays(1).atStartOfDay().minusSeconds(1)));

        // 카테고리 별로 오늘 소진된 재고 수
        Map<CategoryCode, Integer> servedAmountMap = new HashMap<>();
        amountRepository.findAmountOfCategory(servingAt).forEach(o -> {
            servedAmountMap.put(o.getCategoryCode(), Math.max(o.getValue().intValue() * (-1), 0));
        });
        List<CategoryCode> floor10CategoryList = CategoryCode.getFloor10List();
        floor10CategoryList.forEach(o -> {
            if (!servedAmountMap.containsKey(o)) servedAmountMap.put(o, 0);
        });

        // 카테고리 별 초기 재고 수
        Map<CategoryCode, Integer> stockMap = new HashMap<>();
        stockRepository.findStockOfDay(servingAt).forEach(o -> {
            stockMap.put(o.getCategoryCode(), o.getValue().intValue());
        });
        floor10CategoryList.forEach(o -> {
            if (!stockMap.containsKey(o)) {
                stockMap.put(o, 0);
                logger.error(o.getTitle() + "의 stock이 존재하지 않습니다.");
            }
        });

        floor10CategoryList.forEach(o -> {
            if (!o.equals(CategoryCode.LUNCH_BOX))
                amountResponseList.add(convertToAmountResponse(o, servedAmountMap, stockMap, intervalMin));
        });

        // 최종 결과값을 기준으로 LunchBox의 먹은 재고량 계산
        int servedAmountOfLunchBox = (cntNocardPerson + cntTag) -
                (
                        (amountResponseList.get(0).stock() + amountResponseList.get(1).stock())
                                - (amountResponseList.get(0).value() + amountResponseList.get(1).value())
                );

        amountResponseList.add(AmountResponse.builder()
                .category(CategoryCode.LUNCH_BOX.getTitle())
                .value(stockMap.get(CategoryCode.LUNCH_BOX) < servedAmountOfLunchBox ? 0 : stockMap.get(CategoryCode.LUNCH_BOX) - servedAmountOfLunchBox)
                .stock(stockMap.get(CategoryCode.LUNCH_BOX))
                .velocity(calcAmountVelocity(stockMap.get(CategoryCode.LUNCH_BOX),
                        stockMap.get(CategoryCode.LUNCH_BOX) < servedAmountOfLunchBox ? stockMap.get(CategoryCode.LUNCH_BOX) : servedAmountOfLunchBox,
                        intervalMin))
                .servedAmountPerMin(calcServedAmountPerMin(stockMap.get(CategoryCode.LUNCH_BOX) < servedAmountOfLunchBox ? stockMap.get(CategoryCode.LUNCH_BOX) : servedAmountOfLunchBox, intervalMin))
                .build());

        return amountResponseList;
    }

    private AmountResponse convertToAmountResponse(
            CategoryCode cc,
            Map<CategoryCode, Integer> servedAmountMap,
            Map<CategoryCode, Integer> stockMap,
            long intervalMin) {
        Integer stock = stockMap.get(cc);
        Integer servedAmount = servedAmountMap.get(cc);
        Integer amount = stock < servedAmount ? 0 : stock - servedAmount;

        return AmountResponse.builder()
                .category(cc.getTitle())
                .value(amount)
                .stock(stock)
                .velocity(calcAmountVelocity(stock,
                        stock < servedAmount ? stock : servedAmount,
                        intervalMin))
                .servedAmountPerMin(calcServedAmountPerMin(servedAmount, intervalMin))
                .build();
    }

    //velocity 값 계산
    private float calcAmountVelocity(int stock, int servedAmount, long intervalMin) {
        //재고가 없는 경우
        if (stock == 0)
            return 0;
        //11시에 음식을 가져간 경우
        if (intervalMin == 0)
            return 0;
        float amountVelocity = ((servedAmount / (float) stock) * 1000) / (float) intervalMin;
        //소숫점 2번째 자리까지만
        String formatResult = String.format("%.2f", amountVelocity);
        return Float.parseFloat(formatResult);
    }

    //분당 재고 소진량 계산
    private float calcServedAmountPerMin(int servedAmount, long intervalMin) {
        float servedAmountPerMin;
        if (servedAmount == 0 || intervalMin <= 0) servedAmountPerMin = 0f;
        else servedAmountPerMin = (float) servedAmount / (float) intervalMin;
        String formatResult = String.format("%.2f", servedAmountPerMin);
        return Float.parseFloat(formatResult);
    }
}
