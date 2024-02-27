package com.A204.service;

import com.A204.code.CategoryCode;
import com.A204.domain.*;
import com.A204.dto.response.FoodResponse;
import com.A204.dto.response.MenuResponse;
import com.A204.repository.MenuRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @InjectMocks
    private MenuService menuService;

    @Mock
    private StaticDBService staticDBService;

    @Mock
    MenuRepository menuRepository;

    @BeforeEach
    void init() {
        menuRepository.deleteAll();
    }

    @Nested
    @DisplayName("MenuService findMenuList Test")
    class findMenuListTest {
        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {
            @Test
            void findMenuListTest() throws Exception {
                //given
                Date servingAt = Date.valueOf(LocalDate.now());
                List<Menu> menuList = new ArrayList<>();
                AllergyCode allergyCode = AllergyCode.builder()
                        .id(1)
                        .ingredient("알러지")
                        .build();
                int idx = 1;
                for (CategoryCode categoryCode : CategoryCode.values()) {
                    if (categoryCode.getRestaurantCode() == null) continue;
                    Menu menu = Menu.builder()
                            .id(idx)
                            .servingAt(servingAt)
                            .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                            .categoryCode(categoryCode)
                            .menuFoodList(new ArrayList<>())
                            .build();
                    Food food = Food.builder()
                            .id(idx)
                            .content(categoryCode.getTitle())
                            .foodAllergyCodeList(new ArrayList<>())
                            .build();
                    food.setAllergyCodeList(Stream.of(allergyCode));
                    menu.addFood(food);
                    menuList.add(menu);
                }

                given(menuRepository.findMenuOfWeek(any(), any())).willReturn(menuList);

                //when
                lenient().when(menuRepository.findMenuOfWeek(any(), any())).thenReturn(menuList);
                List<MenuResponse> result = menuService.findMenuList();

                //then
                verify(menuRepository, times(1)).findMenuOfWeek(any(), any());
                Assertions.assertThat(result).isEqualTo(menuList.stream().map(o -> MenuResponse.builder()
                        .servingAt(o.getServingAt())
                        .category(((CategoryCode) o.getCategoryCode()).getTitle())
                        .foodList(
                                o.getMenuFoodList().stream().map(it -> FoodResponse.builder()
                                                .content(it.getFood().getContent())
                                                .allergyList(
                                                        it.getFood().getFoodAllergyCodeList()
                                                                .stream()
                                                                .map(it2 -> it2.getAllergyCode().getIngredient())
                                                                .toList())
                                                .build())
                                        .toList()
                        ).build()).toList());
            }
        }

        @Nested
        @DisplayName("오류 케이스")
        class FailCase {
            @Test
            void findMenuListTest() throws Exception {
                //given
                List<Menu> menuList = new ArrayList<>();

                //when
                lenient().when(menuRepository.findMenuOfWeek(any(), any())).thenReturn(menuList);
                menuService.findMenuList();

                //then
                verify(menuRepository, times(1)).findMenuOfWeek(any(), any());
            }
        }
    }
}
