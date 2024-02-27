package com.A204.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

@Service
public class CommonService {

    /**
     * 오늘 날짜 기준으로 이번주 데이터를 보여줄 것인가, 다음주 데이터를 보여줄 것인가 결정하는 start, end 데이터 반환
     */
    public DBBetweenDate getDBBetweenDate() {
        LocalDate now = LocalDate.now();

        int day = now.get(ChronoField.DAY_OF_WEEK);
        Date start = null;
        Date end = null;
        if (day == DayOfWeek.SATURDAY.getValue()) {
            start = Date.valueOf(now.plusDays(1));
            end = Date.valueOf(now.plusDays(7));

        } else {
            if (day == DayOfWeek.SUNDAY.getValue())
                day = 0;
            start = Date.valueOf(now.minusDays(day));
            end = Date.valueOf(now.plusDays(6 - day));
        }

        return new DBBetweenDate(start, end);
    }

    @AllArgsConstructor
    static class DBBetweenDate {
        Date start, end;
    }
}
