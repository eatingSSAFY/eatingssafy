package com.A204.service;

import com.A204.domain.LunchTime;
import com.A204.dto.response.LunchTimeResponse;
import com.A204.repository.LunchTimeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LunchTimeService {
    private final LunchTimeRepository lunchTimeRepository;

    private final Logger logger = LoggerFactory.getLogger(LunchTimeService.class);

    public List<LunchTimeResponse> findLunchTimeList() {
        List<LunchTime> lunchTimeList = lunchTimeRepository.findLunchTimeOfWeek(Date.valueOf(LocalDate.now()));
        lunchTimeList.sort(Comparator.comparing(LunchTime::getLunchTime));
        DateFormat format = new SimpleDateFormat("HH:mm");
        if (lunchTimeList.isEmpty())
            logger.error("lunch_time을 찾지 못했습니다.");
        return lunchTimeList.stream().map(o -> {
            StringBuilder sb = new StringBuilder();
            return LunchTimeResponse.builder()
                    .floor(o.getFloor())
                    .lunchTime(
                            sb.append(format.format(o.getLunchTime())).append('~').append(format.format(Time.valueOf(o.getLunchTime().toLocalTime().plusHours(1)))).toString()
                    )
                    .build();
        }).toList();
    }
}
