package com.A204.controller;

import com.A204.code.CategoryCode;
import com.A204.dto.response.MenuResponse;
import com.A204.service.MenuService;
import com.A204.validation.CustomException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuApiController.class)
class MenuApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    MenuService menuService;

    @Nested
    @DisplayName("MenuApiController findMenuList Test")
    class findMenuListTest {
        final String url = "/api/menu";
        private List<MenuResponse> menuResponseList;

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @Test
            void findMenuListTest() throws Exception {
                //given
                given(menuService.findMenuList()).willReturn(List.of(
                        MenuResponse.builder()
                                .servingAt(Date.valueOf(LocalDate.now()))
                                .foodList(new ArrayList<>())
                                .category(CategoryCode.LUNCH_BOX.getTitle())
                                .build()
                ));

                //when
                lenient().when(menuService.findMenuList()).thenReturn(menuResponseList);

                //then
                mockMvc.perform(get(url))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("오류 케이스")
        class FailCase {
            @Test
            void findMenuListTest() throws Exception {
                //when
                when(menuService.findMenuList()).thenThrow(new CustomException(HttpStatus.NOT_FOUND));
                //then
                Assertions.assertThrows(CustomException.class, () -> {
                    menuService.findMenuList();
                });
            }
        }
    }
}
