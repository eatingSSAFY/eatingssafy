package com.A204.controller;

import com.A204.dto.request.AddAmountListRequest;
import com.A204.dto.request.AddAmountRequest;
import com.A204.dto.response.AmountResponse;
import com.A204.service.AmountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AmountApiController.class)
class AmountApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AmountService amountService;

    final String url = "/api/amount";
    protected ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("AmountApiController addAmount Test")
    class addAmountTest {
        private String name;
        private Integer value;
        private AddAmountListRequest amountListRequest;
        private List<AddAmountRequest> addAmountRequestList = new ArrayList<>();
        private static Integer cameraId = 1;

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {
            @Test
            void addAmountTest() throws Exception {
                //given
                name = "도시락";
                cameraId = 1;
                value = 100;
                addAmountRequestList.add(AddAmountRequest.builder()
                        .name(name)
                        .value(value)
                        .build());

                amountListRequest = AddAmountListRequest.builder()
                        .data(addAmountRequestList)
                        .build();

                final String requestBody = objectMapper.writeValueAsString(amountListRequest);

                //when
                ResultActions resultActions = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody));

                //then
                resultActions.andExpect(status().isOk());
                verify(amountService).save(amountListRequest, cameraId);
            }
        }

        @Nested
        @DisplayName("request validation 오류 케이스")
        class FailCase {
            @Test
            void addAmountTest() throws Exception {
                //given
                List<String> nameList = Arrays.asList("", " ", null, "이름", "도시룩", "도시락");
                List<Integer> valueList = Arrays.asList(1, 1, -1, -1, 1, 0);
                for (int i = 0; i < nameList.size(); i++) {
                    name = nameList.get(i);
                    value = valueList.get(i);

                    addAmountRequestList.add(AddAmountRequest.builder()
                            .name(name)
                            .value(value)
                            .build());

                    amountListRequest = AddAmountListRequest.builder().data(addAmountRequestList).build();

                    final String requestBody = objectMapper.writeValueAsString(amountListRequest);

                    //when
                    ResultActions resultActions = mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(requestBody));

                    //then
                    resultActions.andExpect(status().isBadRequest());
                }
            }
        }
    }

    @Nested
    @DisplayName("AmountApiController findAmountList Test")
    class findAmountListTest {
        private List<AmountResponse> amountResponseList = new ArrayList<>();

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {
            @Test
            void findAmountListTest() throws Exception {
                //given
                amountResponseList.add(AmountResponse.builder()
                        .category("도시락")
                        .value(130)
                        .stock(135)
                        .velocity(0.04f)
                        .build());
                amountResponseList.add(AmountResponse.builder()
                        .category("샌드위치")
                        .value(49)
                        .stock(50)
                        .velocity(0.02f)
                        .build());
                amountResponseList.add(AmountResponse.builder()
                        .category("샐러드")
                        .value(33)
                        .stock(45)
                        .velocity(0.03f)
                        .build());

                //when
                lenient().when(amountService.findAmountList()).thenReturn(amountResponseList);

                //then
                ResultActions resultActions = mockMvc.perform(get(url))
                        .andExpect(status().isOk());

            }
        }
    }


}
