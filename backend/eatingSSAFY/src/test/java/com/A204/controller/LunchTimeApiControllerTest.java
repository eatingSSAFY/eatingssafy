package com.A204.controller;

import com.A204.dto.response.LunchTimeResponse;
import com.A204.service.LunchTimeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LunchTimeApiController.class)
class LunchTimeApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    LunchTimeService lunchTimeService;

    final String url = "/api/lunchtime";
    protected ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("LunchTimeApiController findLunchTimeList Test")
    class findLunchTimeListTest {

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {
            @Test
            void findAmountListTest() throws Exception {
                //given
                List<LunchTimeResponse> result = List.of(
                        LunchTimeResponse.builder()
                                .floor(1)
                                .lunchTime("12:00")
                                .build(),
                        LunchTimeResponse.builder()
                                .floor(2)
                                .lunchTime("12:10")
                                .build()
                );

                //when
                lenient().when(lunchTimeService.findLunchTimeList()).thenReturn(result);

                //then
                ResultActions resultActions = mockMvc.perform(get(url))
                        .andExpect(status().isOk());
            }
        }
    }


}
