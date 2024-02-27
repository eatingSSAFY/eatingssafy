package com.A204.service;

import com.A204.domain.DailyVisitHistory;
import com.A204.dto.response.DailyVisitHistoryResponse;
import com.A204.repository.DailyVisitHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DailyVisitHistoryService {
    private final DailyVisitHistoryRepository dailyVisitHistoryRepository;

    /**
     * 방문이력 등록/갱신
     */
    @Transactional
    public void save(Long userId) {
        Date today = Date.valueOf(LocalDate.now());
        DailyVisitHistory dailyVisitHistory = dailyVisitHistoryRepository.findDailyVisitHistory(userId, today).orElse(
                DailyVisitHistory.builder()
                        .userId(userId)
                        .cnt(0)
                        .visitedAt(today)
                        .build()
        );
        dailyVisitHistory.setCnt(dailyVisitHistory.getCnt() + 1);
        dailyVisitHistoryRepository.save(dailyVisitHistory);
    }

    /**
     * 방문이력 검색
     */
    public DailyVisitHistoryResponse findDailyVisitHistory(Long userId) {
        Date today = Date.valueOf(LocalDate.now());
        Integer cnt = dailyVisitHistoryRepository.findCntOfUser(userId, today);
        if (cnt == null) return DailyVisitHistoryResponse.builder()
                .cnt(0)
                .build();
        else return DailyVisitHistoryResponse.builder()
                .cnt(cnt)
                .build();
    }
}
