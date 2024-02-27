package com.A204.service;

import com.A204.domain.LunchTime;
import com.A204.dto.response.LunchTimeResponse;
import com.A204.repository.LunchTimeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LunchTimeServiceTest {
    @InjectMocks
    private LunchTimeService lunchTimeService;

    @Mock
    private LunchTimeRepository lunchTimeRepository;

    @Nested
    @DisplayName("LunchTimeService findLunchTimeList Test")
    class findLunchTimeListTest {
        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {
            @Test
            void findLunchTimeListTest() {
                // given
                LocalDate now = LocalDate.now();
                Date periodStart = Date.valueOf(now.minusDays(1));
                Date periodEnd = Date.valueOf(now.plusDays(1));
                List<LunchTime> lunchTimeList = List.of(
                        LunchTime.builder()
                                .id(1)
                                .floor(1)
                                .lunchTime(Time.valueOf("12:00:00"))
                                .periodStart(periodStart)
                                .periodEnd(periodEnd)
                                .build(),
                        LunchTime.builder()
                                .id(2)
                                .floor(2)
                                .lunchTime(Time.valueOf("12:10:00"))
                                .periodStart(periodStart)
                                .periodEnd(periodEnd)
                                .build()
                );
                DateFormat format = new SimpleDateFormat("HH:mm");

                // when
                lenient().when(lunchTimeRepository.findLunchTimeOfWeek(any())).thenReturn(lunchTimeList);

                // then
                List<LunchTimeResponse> result = lunchTimeService.findLunchTimeList();
                verify(lunchTimeRepository, times(1)).findLunchTimeOfWeek(any());
                Assertions.assertThat(result).isEqualTo(lunchTimeList.stream().map(o -> {
                    StringBuilder sb = new StringBuilder();
                    return LunchTimeResponse.builder()
                            .floor(o.getFloor())
                            .lunchTime(
                                    sb.append(format.format(o.getLunchTime())).append('~').append(format.format(Time.valueOf(o.getLunchTime().toLocalTime().plusHours(1)))).toString()
                            )
                            .build();
                }).toList());
            }
        }
    }
}
