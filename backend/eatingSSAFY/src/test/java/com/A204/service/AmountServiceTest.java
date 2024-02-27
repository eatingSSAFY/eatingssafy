package com.A204.service;

import com.A204.code.CategoryCode;
import com.A204.domain.ICntValueOfCategory;
import com.A204.dto.request.AddAmountListRequest;
import com.A204.dto.request.AddAmountRequest;
import com.A204.dto.response.AmountResponse;
import com.A204.repository.AmountRepository;
import com.A204.repository.NocardPersonRepository;
import com.A204.repository.StockRepository;
import com.A204.repository.TagRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AmountServiceTest {
    @InjectMocks
    private AmountService amountService;
    @Mock
    AmountRepository amountRepository;
    @Mock
    NocardPersonRepository nocardPersonRepository;
    @Mock
    TagRepository tagRepository;
    @Mock
    StockRepository stockRepository;

    @Nested
    @DisplayName("AmountService save Test")
    class saveTest {
        private String name;
        private static Integer cameraId = 1;
        private Integer value;
        private AddAmountListRequest addAmountListRequest;

        @Nested
        @DisplayName("정상 케이스")
        class SucecessCase {
            @Test
            void saveTest() throws Exception {
                //given
                name = "도시락";
                value = 3;
                addAmountListRequest = AddAmountListRequest.builder().data(
                        Arrays.asList(AddAmountRequest.builder()
                                .name(name)
                                .value(value)
                                .build())
                ).build();

                //when
                amountService.save(addAmountListRequest, cameraId);

                //then
                verify(amountRepository, times(1)).saveAll(any());
            }
        }
    }

    @Nested
    @DisplayName("AmountService findAmountList Test")
    class findAmountListTest {
        private int nocardPersonCntOfDay;
        private int tagCntOfDay;
        private List<ICntValueOfCategory> iCntValueOfCategoryListOfAmount;
        private List<ICntValueOfCategory> iCntValueOfCategoryListOfStock;
        private List<AmountResponse> expectedAmountResponseList;
        private long intervalMin;

        @BeforeEach
        void init() {
            LocalDateTime timeAtEleven = LocalDate.now().atTime(11, 00, 00);
            Duration duration = Duration.between(timeAtEleven.toLocalTime(), LocalDateTime.now().toLocalTime());
            intervalMin = duration.toMinutes();
        }

        @Nested
        @DisplayName("정상 케이스")
        class SucecessCase {
            @Test
            @DisplayName("모든 값이 양수인 경우")
            void findAmountListTest() throws Exception {
                //given
                nocardPersonCntOfDay = 10;
                tagCntOfDay = 5;
                iCntValueOfCategoryListOfAmount = new ArrayList<>();
                iCntValueOfCategoryListOfAmount.add(iCntValueOfCategoryConstructor(CategoryCode.SANDWICH, Long.valueOf("7")));
                iCntValueOfCategoryListOfAmount.add(iCntValueOfCategoryConstructor(CategoryCode.SALAD, Long.valueOf("3")));
                iCntValueOfCategoryListOfStock = new ArrayList<>();
                iCntValueOfCategoryListOfStock.add(iCntValueOfCategoryConstructor(CategoryCode.LUNCH_BOX, Long.valueOf("100")));
                iCntValueOfCategoryListOfStock.add(iCntValueOfCategoryConstructor(CategoryCode.SANDWICH, Long.valueOf("30")));
                iCntValueOfCategoryListOfStock.add(iCntValueOfCategoryConstructor(CategoryCode.SALAD, Long.valueOf("50")));

                expectedAmountResponseList = Arrays.asList(
                        AmountResponse.builder()
                                .category(CategoryCode.SANDWICH.getTitle())
                                .value(23)
                                .stock(30)
                                .velocity(calcAmountVelocity(30, 7, intervalMin))
                                .build(),
                        AmountResponse.builder()
                                .category(CategoryCode.SALAD.getTitle())
                                .value(47)
                                .stock(50)
                                .velocity(calcAmountVelocity(50, 3, intervalMin))
                                .build(),
                        AmountResponse.builder()
                                .category(CategoryCode.LUNCH_BOX.getTitle())
                                .value(95)
                                .stock(100)
                                .velocity(calcAmountVelocity(100, 5, intervalMin))
                                .build()
                );

                //when
                lenient().when(nocardPersonRepository.findNocardPersonCntOfDay(any(), any())).thenReturn(nocardPersonCntOfDay);
                lenient().when(tagRepository.findTagCntOfDay(any(), any())).thenReturn(tagCntOfDay);
                lenient().when(amountRepository.findAmountOfCategory(any())).thenReturn(iCntValueOfCategoryListOfAmount);
                lenient().when(stockRepository.findStockOfDay((any()))).thenReturn(iCntValueOfCategoryListOfStock);

                List<AmountResponse> amountResponseList = amountService.findAmountList();

                //then
                verify(nocardPersonRepository, times(1)).findNocardPersonCntOfDay(any(), any());
                verify(tagRepository, times(1)).findTagCntOfDay(any(), any());
                verify(amountRepository, times(1)).findAmountOfCategory(any());
                verify(stockRepository, times(1)).findStockOfDay(any());

                Assertions.assertThat(amountResponseList).isEqualTo(expectedAmountResponseList);
            }

            @Test
            @DisplayName("배식시작하기 전의 상태")
            void findDefaultAmountListTest() throws Exception {
                //given
                nocardPersonCntOfDay = 0;
                tagCntOfDay = 0;
                iCntValueOfCategoryListOfAmount = new ArrayList<>();
                iCntValueOfCategoryListOfStock = new ArrayList<>();
                iCntValueOfCategoryListOfStock.add(iCntValueOfCategoryConstructor(CategoryCode.LUNCH_BOX, Long.valueOf("100")));
                iCntValueOfCategoryListOfStock.add(iCntValueOfCategoryConstructor(CategoryCode.SANDWICH, Long.valueOf("30")));
                iCntValueOfCategoryListOfStock.add(iCntValueOfCategoryConstructor(CategoryCode.SALAD, Long.valueOf("50")));

                expectedAmountResponseList = Arrays.asList(
                        AmountResponse.builder()
                                .category(CategoryCode.SANDWICH.getTitle())
                                .value(30)
                                .stock(30)
                                .velocity(calcAmountVelocity(30, 0, intervalMin))
                                .build(),
                        AmountResponse.builder()
                                .category(CategoryCode.SALAD.getTitle())
                                .value(50)
                                .stock(50)
                                .velocity(calcAmountVelocity(50, 0, intervalMin))
                                .build(),
                        AmountResponse.builder()
                                .category(CategoryCode.LUNCH_BOX.getTitle())
                                .value(100)
                                .stock(100)
                                .velocity(calcAmountVelocity(100, 0, intervalMin))
                                .build()
                );

                //when
                lenient().when(nocardPersonRepository.findNocardPersonCntOfDay(any(), any())).thenReturn(nocardPersonCntOfDay);
                lenient().when(tagRepository.findTagCntOfDay(any(), any())).thenReturn(tagCntOfDay);
                lenient().when(amountRepository.findAmountOfCategory(any())).thenReturn(iCntValueOfCategoryListOfAmount);
                lenient().when(stockRepository.findStockOfDay((any()))).thenReturn(iCntValueOfCategoryListOfStock);

                List<AmountResponse> amountResponseList = amountService.findAmountList();

                //then
                verify(nocardPersonRepository, times(1)).findNocardPersonCntOfDay(any(), any());
                verify(tagRepository, times(1)).findTagCntOfDay(any(), any());
                verify(amountRepository, times(1)).findAmountOfCategory(any());
                verify(stockRepository, times(1)).findStockOfDay(any());

                Assertions.assertThat(amountResponseList).isEqualTo(expectedAmountResponseList);
            }
        }

        @Nested
        @DisplayName("오류 케이스")
        class FailCase {
            @Test
            @DisplayName("SALAD Stock이 존재하지 않는 경우")
            void saladStockNotExistTest() throws Exception {
                //given
                nocardPersonCntOfDay = 10;
                tagCntOfDay = 5;
                List<ICntValueOfCategory> iCntValueOfCategoryListOfAmount = new ArrayList<>();
                iCntValueOfCategoryListOfAmount.add(iCntValueOfCategoryConstructor(CategoryCode.SANDWICH, Long.valueOf("7")));
                iCntValueOfCategoryListOfAmount.add(iCntValueOfCategoryConstructor(CategoryCode.SALAD, Long.valueOf("3")));
                List<ICntValueOfCategory> iCntValueOfCategoryListOfStock = new ArrayList<>();
                iCntValueOfCategoryListOfStock.add(iCntValueOfCategoryConstructor(CategoryCode.LUNCH_BOX, Long.valueOf("100")));
                iCntValueOfCategoryListOfStock.add(iCntValueOfCategoryConstructor(CategoryCode.SANDWICH, Long.valueOf("30")));

                List<AmountResponse> expectedAmountResponseList = Arrays.asList(
                        AmountResponse.builder()
                                .category(CategoryCode.SANDWICH.getTitle())
                                .value(23)
                                .stock(30)
                                .velocity(calcAmountVelocity(30, 7, intervalMin))
                                .build(),
                        AmountResponse.builder()
                                .category(CategoryCode.SALAD.getTitle())
                                .value(0)
                                .stock(0)
                                .velocity(0.0f)
                                .build(),
                        AmountResponse.builder()
                                .category(CategoryCode.LUNCH_BOX.getTitle())
                                .value(92)
                                .stock(100)
                                .velocity(calcAmountVelocity(100, 8, intervalMin))
                                .build()
                );
                //when
                lenient().when(nocardPersonRepository.findNocardPersonCntOfDay(any(), any())).thenReturn(nocardPersonCntOfDay);
                lenient().when(tagRepository.findTagCntOfDay(any(), any())).thenReturn(tagCntOfDay);
                lenient().when(amountRepository.findAmountOfCategory(any())).thenReturn(iCntValueOfCategoryListOfAmount);
                lenient().when(stockRepository.findStockOfDay((any()))).thenReturn(iCntValueOfCategoryListOfStock);

                List<AmountResponse> amountResponseList = amountService.findAmountList();

                //then
                verify(nocardPersonRepository, times(1)).findNocardPersonCntOfDay(any(), any());
                verify(tagRepository, times(1)).findTagCntOfDay(any(), any());
                verify(amountRepository, times(1)).findAmountOfCategory(any());
                verify(stockRepository, times(1)).findStockOfDay(any());

                Assertions.assertThat(amountResponseList).isEqualTo(expectedAmountResponseList);
            }

            @Test
            @DisplayName("당일 메뉴의 재고 정보가 존재하지 않는 경우")
            void stockListNotExistTest() throws Exception {
                //given
                nocardPersonCntOfDay = 10;
                tagCntOfDay = 5;
                List<ICntValueOfCategory> iCntValueOfCategoryListOfAmount = new ArrayList<>();
                iCntValueOfCategoryListOfAmount.add(iCntValueOfCategoryConstructor(CategoryCode.SANDWICH, Long.valueOf("7")));
                iCntValueOfCategoryListOfAmount.add(iCntValueOfCategoryConstructor(CategoryCode.SALAD, Long.valueOf("3")));
                List<ICntValueOfCategory> iCntValueOfCategoryListOfStock = new ArrayList<>();
                iCntValueOfCategoryListOfStock.add(iCntValueOfCategoryConstructor(CategoryCode.LUNCH_BOX, Long.valueOf("0")));
                iCntValueOfCategoryListOfStock.add(iCntValueOfCategoryConstructor(CategoryCode.SANDWICH, Long.valueOf("0")));
                iCntValueOfCategoryListOfStock.add(iCntValueOfCategoryConstructor(CategoryCode.SALAD, Long.valueOf("0")));

                List<AmountResponse> expectedAmountResponseList = Arrays.asList(
                        AmountResponse.builder()
                                .category(CategoryCode.SANDWICH.getTitle())
                                .value(0)
                                .stock(0)
                                .velocity(calcAmountVelocity(0, 0, intervalMin))
                                .build(),
                        AmountResponse.builder()
                                .category(CategoryCode.SALAD.getTitle())
                                .value(0)
                                .stock(0)
                                .velocity(calcAmountVelocity(0, 0, intervalMin))
                                .build(),
                        AmountResponse.builder()
                                .category(CategoryCode.LUNCH_BOX.getTitle())
                                .value(0)
                                .stock(0)
                                .velocity(calcAmountVelocity(0, 0, intervalMin))
                                .build()
                );
                //when
                lenient().when(nocardPersonRepository.findNocardPersonCntOfDay(any(), any())).thenReturn(nocardPersonCntOfDay);
                lenient().when(tagRepository.findTagCntOfDay(any(), any())).thenReturn(tagCntOfDay);
                lenient().when(amountRepository.findAmountOfCategory(any())).thenReturn(iCntValueOfCategoryListOfAmount);
                lenient().when(stockRepository.findStockOfDay((any()))).thenReturn(iCntValueOfCategoryListOfStock);

                List<AmountResponse> amountResponseList = amountService.findAmountList();

                //then
                verify(nocardPersonRepository, times(1)).findNocardPersonCntOfDay(any(), any());
                verify(tagRepository, times(1)).findTagCntOfDay(any(), any());
                verify(amountRepository, times(1)).findAmountOfCategory(any());
                verify(stockRepository, times(1)).findStockOfDay(any());

                Assertions.assertThat(amountResponseList).isEqualTo(expectedAmountResponseList);
            }

            @Test
            @DisplayName("SALAD 남은 양이 0미만인 오류")
            void saladRemainUnderZeroTest() throws Exception {
                //given
                nocardPersonCntOfDay = 60;
                tagCntOfDay = 5;
                iCntValueOfCategoryListOfAmount = new ArrayList<>();
                iCntValueOfCategoryListOfAmount.add(iCntValueOfCategoryConstructor(CategoryCode.SANDWICH, Long.valueOf("7")));
                iCntValueOfCategoryListOfAmount.add(iCntValueOfCategoryConstructor(CategoryCode.SALAD, Long.valueOf("55")));
                iCntValueOfCategoryListOfStock = new ArrayList<>();
                iCntValueOfCategoryListOfStock.add(iCntValueOfCategoryConstructor(CategoryCode.LUNCH_BOX, Long.valueOf("100")));
                iCntValueOfCategoryListOfStock.add(iCntValueOfCategoryConstructor(CategoryCode.SANDWICH, Long.valueOf("30")));
                iCntValueOfCategoryListOfStock.add(iCntValueOfCategoryConstructor(CategoryCode.SALAD, Long.valueOf("50")));

                List<AmountResponse> expectedAmountResponseList = Arrays.asList(
                        AmountResponse.builder()
                                .category(CategoryCode.SANDWICH.getTitle())
                                .value(23)
                                .stock(30)
                                .velocity(calcAmountVelocity(30, 7, intervalMin))
                                .build(),
                        AmountResponse.builder()
                                .category(CategoryCode.SALAD.getTitle())
                                .value(0)
                                .stock(50)
                                .velocity(calcAmountVelocity(50, 50, intervalMin))
                                .build(),
                        AmountResponse.builder()
                                .category(CategoryCode.LUNCH_BOX.getTitle())
                                .value(92)
                                .stock(100)
                                .velocity(calcAmountVelocity(100, 8, intervalMin))
                                .build()
                );
                //when
                lenient().when(nocardPersonRepository.findNocardPersonCntOfDay(any(), any())).thenReturn(nocardPersonCntOfDay);
                lenient().when(tagRepository.findTagCntOfDay(any(), any())).thenReturn(tagCntOfDay);
                lenient().when(amountRepository.findAmountOfCategory(any())).thenReturn(iCntValueOfCategoryListOfAmount);
                lenient().when(stockRepository.findStockOfDay((any()))).thenReturn(iCntValueOfCategoryListOfStock);

                List<AmountResponse> amountResponseList = amountService.findAmountList();

                //then
                verify(nocardPersonRepository, times(1)).findNocardPersonCntOfDay(any(), any());
                verify(tagRepository, times(1)).findTagCntOfDay(any(), any());
                verify(amountRepository, times(1)).findAmountOfCategory(any());
                verify(stockRepository, times(1)).findStockOfDay(any());

                Assertions.assertThat(amountResponseList).isEqualTo(expectedAmountResponseList);
            }
        }

        private ICntValueOfCategory iCntValueOfCategoryConstructor(CategoryCode categoryCode, Long value) {
            return new ICntValueOfCategory() {
                @Override
                public CategoryCode getCategoryCode() {
                    return categoryCode;
                }

                @Override
                public Long getValue() {
                    return value;
                }
            };
        }

        private float calcAmountVelocity(int stock, int servedAmount, long intervalMin) {
            //재고가 없는 경우
            if (stock == 0)
                return 0;
            //11시에 음식을 가져간 경우
            if (intervalMin == 0)
                return 0;
            float amountVelocity = ((servedAmount / (float) stock) * 100) / (float) intervalMin;
            //소숫점 2번째 자리까지만
            String formatResult = String.format("%.2f", amountVelocity);
            return Float.parseFloat(formatResult);
        }
    }
}