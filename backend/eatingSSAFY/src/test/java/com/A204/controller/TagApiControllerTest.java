package com.A204.controller;

import com.A204.dto.request.AddTagRequest;
import com.A204.service.TagService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagApiController.class)
class TagApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TagService tagService;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("TagApiController addTag Test")
    class addTagTest {
        final String url = "/tag";
        private String added;

        private AddTagRequest addTagRequest;

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {
            @Test
            void addTagTest() throws Exception {
                //given
                added = "1";
                addTagRequest = new AddTagRequest(added);
                String requestBody = objectMapper.writeValueAsString(addTagRequest);

                //when
                ResultActions resultActions = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody));

                //then
                resultActions.andExpect(status().isOk());
                verify(tagService).save(any());
            }
        }

        @Nested
        @DisplayName("오류 케이스")
        class FailCase {
            @Test
            void addTagTest() throws Exception {
                //given
                List<String> addedList = Arrays.asList("2", "5", "123456789", "", " ", "h", "ㄱ", ".", "helloworld!!!!");

                for (int i = 0; i < addedList.size(); i++) {
                    added = addedList.get(i);
                    addTagRequest = new AddTagRequest(added);
                    String requestBody = objectMapper.writeValueAsString(addTagRequest);

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