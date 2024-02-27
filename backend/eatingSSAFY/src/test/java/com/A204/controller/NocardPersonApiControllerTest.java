package com.A204.controller;

import com.A204.dto.request.AddNocardPersonRequest;
import com.A204.service.NocardPersonService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NocardPersonApiController.class)
class NocardPersonApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    NocardPersonService nocardPersonService;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("NocardPersonApiController addNocardPerson Test")
    class addNocardPersonTest {
        final String url = "/nocard-person";
        private String personName;
        private String personId;
        private AddNocardPersonRequest addNocardPersonRequest;

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {
            @Test
            void addNocardPersonTest() throws Exception {
                //given
                personName = "이름";
                personId = "10480000";
                addNocardPersonRequest = new AddNocardPersonRequest(personName, personId);
                final String requestBody = objectMapper.writeValueAsString(addNocardPersonRequest);

                //when
                ResultActions resultActions = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody));

                //then
                resultActions.andExpect(status().isOk());
                verify(nocardPersonService).save(addNocardPersonRequest);
            }
        }

        @Nested
        @DisplayName("오류 케이스")
        class FailCase {
            @Test
            void addNocardPersonTest() throws Exception {
                //given
                List<String> personNameList = Arrays.asList("boo", "", "임", "오스트랄로피테쿠스", "김싸피", "김역삼", "김강남", "부재중");
                List<String> personIdList = Arrays.asList("1040202", "1023847", "1047278", "20183813", " ", "", "12345", "컨설턴트님 김싸피님 12345678910.123456789");

                for (int i = 0; i < personNameList.size(); i++) {
                    personName = personNameList.get(i);
                    personId = personIdList.get(i);
                    addNocardPersonRequest = new AddNocardPersonRequest(personName, personId);
                    final String requestBody = objectMapper.writeValueAsString(addNocardPersonRequest);

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

}
