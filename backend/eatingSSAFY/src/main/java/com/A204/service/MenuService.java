package com.A204.service;

import com.A204.code.CategoryCode;
import com.A204.code.RestaurantCode;
import com.A204.domain.AllergyCode;
import com.A204.domain.Food;
import com.A204.domain.Menu;
import com.A204.dto.parsing.ocr.batch.TextLabelBox;
import com.A204.dto.parsing.ocr.response.FieldResponse;
import com.A204.dto.response.FoodResponse;
import com.A204.dto.response.MenuResponse;
import com.A204.repository.FoodRepository;
import com.A204.repository.MenuRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.dhatim.fastexcel.reader.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final ImageService imageService;
    private final OCRService ocrService;
    private final ExcelService excelService;
    private final MenuRepository menuRepository;
    private final FoodRepository foodRepository;
    private final StaticDBService staticDBService;
    private final CommonService commonService;

    private final Logger logger = LoggerFactory.getLogger(MenuService.class);

    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\([^)]+\\)");
    private static final Pattern NUMBER_COMMA_PATTERN = Pattern.compile("(\\d+)(\\,|\\.)?(\\d+)?");
    private static final Pattern SERVING_AT_PATTERN = Pattern.compile("(\\d+.\\d+\\(\\W\\))");

    /**
     * fileUrl 에서 이미지 파일을 다운로드, 지정된 사이즈로 크롭 후 OCR API 이용하여 메뉴 텍스트 파싱, DB 등록
     */
    @Transactional
    public void regist10(String fileUrl) {
        Map<Date, Map<CategoryCode, Menu>> dbMenuMap = new HashMap<>();
        Map<Date, Map<CategoryCode, List<IdxFood>>> dbMenuFoodMap = new HashMap<>();
        Map<String, Food> dbFoodMap = new HashMap<>();

        LocalDateTime now = LocalDateTime.now();
        imageService.process(fileUrl).forEach(o -> {
            Map<CategoryCode, List<String>> menuGroupByCategory = gathering10MenuText(
                    ocrService.requestOCR(o).images().get(0).fields()
            );
            if (menuGroupByCategory.isEmpty()) return; // 메뉴 정보가 없는 날

            String dateStr = menuGroupByCategory.get(CategoryCode.SERVED_AT).get(0);
            int month = Integer.parseInt(dateStr.substring(0, dateStr.indexOf('.')).replaceAll(" ", ""));
            int day = Integer.parseInt(dateStr.substring(dateStr.indexOf('.') + 1, dateStr.indexOf('(')).replaceAll(" ", ""));
            int year = (month == 1 && now.getMonth() == Month.DECEMBER) ? now.getYear() + 1 : now.getYear();
            Date servingAt = Date.valueOf(LocalDate.of(year, month, day)); // 배식일

            menuGroupByCategory.keySet().forEach(cc -> {
                if (cc.getRestaurantCode() == null || !cc.getRestaurantCode().equals(RestaurantCode.FLOOR_10)) return;

                // DB에 등록할 데이터 기록
                Map<CategoryCode, Menu> ccMenuMap = dbMenuMap.getOrDefault(servingAt, new HashMap<>());
                ccMenuMap.put(cc, Menu.builder()
                        .servingAt(servingAt)
                        .updatedAt(Timestamp.valueOf(now))
                        .categoryCode(cc)
                        .menuFoodList(new ArrayList<>())
                        .build());
                dbMenuMap.put(servingAt, ccMenuMap);

                // date, categoryCode 마다 파싱한 음식 데이터를 기반으로 DB에 등록할 데이터 기록
                Map<String, IdxFood> foodMap = parse10DataToFoodMap(getVerifiedString(String.join("\n", menuGroupByCategory.get(cc))));
                // dbFoodMap putAll
                foodMap.keySet().forEach(key -> {
                    dbFoodMap.put(foodMap.get(key).food.getContent(), foodMap.get(key).food);
                });
                // menu에 연결할 음식 데이터 기록
                Map<CategoryCode, List<IdxFood>> ccFoodStrMap = dbMenuFoodMap.getOrDefault(servingAt, new HashMap<>());
                ccFoodStrMap.put(cc, foodMap.values().stream().sorted(Comparator.comparing(IdxFood::getIdx)).toList());
                dbMenuFoodMap.put(servingAt, ccFoodStrMap);
            });
        });

        menuRepository.saveAll(
                getInsertMenuList(insertFood(dbFoodMap), dbMenuMap, dbMenuFoodMap)
        );
    }

    /**
     * 오타 정보를 수정한 한줄로 이루어진 문자열에서
     * 음식(알러지 포함) 정보를 파싱하여 {음식 이름:음식} 의 Map 형태로 가공
     */
    private Map<String, IdxFood> parse10DataToFoodMap(String verifiedStr) {
        Map<Integer, AllergyCode> allergyCodeMap = staticDBService.allergyCodeMap();
        Map<String, IdxFood> foodMap = new HashMap<>();
        AtomicInteger idx = new AtomicInteger();
        Arrays.stream(verifiedStr.split("[\\/|&|\\n]"))
                .forEach(it -> {
                            // 반찬 별로 분류된 문자열 리스트에서 메뉴 정보, 알레르기 정보 검출하여 Entity 객체 변환
                            Set<Integer> allergySet = new HashSet<>();
                            String origin = it;
                            Matcher matcher = BRACKET_PATTERN.matcher(it); // 괄호 찾기
                            while (matcher.find()) {
                                // 숫자 필터링 준비: 괄호/공백 제거
                                String bracketStr = matcher.group();
                                String withoutBracket = bracketStr.replaceAll("[\\(|\\)]", "").replaceAll(" ", "");
                                if (!NUMBER_COMMA_PATTERN.matcher(withoutBracket).find()) continue;
                                for (String num : withoutBracket.split("[\\,|\\.]")) {
                                    allergySet.add(Integer.parseInt(num));
                                }
                                origin = origin.replace(bracketStr, "");
                            }
                            if (!origin.isEmpty()) {
                                String foodStr = origin.trim();
                                Food food = Food.builder()
                                        .content(foodStr)
                                        .foodAllergyCodeList(new ArrayList<>())
                                        .build();
                                food.setAllergyCodeList(allergySet.stream().map(allergyCodeMap::get).filter(Objects::nonNull));
                                foodMap.put(foodStr, new IdxFood(
                                        idx.getAndIncrement(), food
                                ));
                            }
                        }
                );
        return foodMap;
    }

    /**
     * fileUrl에서 엑셀 파일 다운로드 후, 20층 메뉴 데이터 파싱, DB 등록
     */
    @Transactional
    public void regist20(String fileUrl) {
        Map<Date, Map<CategoryCode, Menu>> dbMenuMap = new HashMap<>();
        Map<Date, Map<CategoryCode, List<IdxFood>>> dbMenuFoodStrMap = new HashMap<>();
        Map<String, Food> dbFoodMap = new HashMap<>();

        LocalDateTime now = LocalDateTime.now();
        Map<Date, Map<CategoryCode, Cell>> menuDataGroupByDate = excelService.read20MenuExcel(fileUrl);
        for (Date servingAt : menuDataGroupByDate.keySet()) {
            for (CategoryCode categoryCode : menuDataGroupByDate.get(servingAt).keySet()) {
                // DB에 등록할 데이터 기록
                Map<CategoryCode, Menu> ccMenuMap = dbMenuMap.getOrDefault(servingAt, new HashMap<>());
                ccMenuMap.put(categoryCode, Menu.builder()
                        .servingAt(servingAt)
                        .menuFoodList(new ArrayList<>())
                        .updatedAt(Timestamp.valueOf(now))
                        .categoryCode(categoryCode)
                        .build());
                dbMenuMap.put(servingAt, ccMenuMap);

                // date, categoryCode 마다 파싱한 음식 데이터를 기반으로 DB에 등록할 데이터 기록
                Map<String, IdxFood> foodMap = parse20DataToFoodMap(menuDataGroupByDate
                        .get(servingAt)
                        .get(categoryCode)
                        .getRawValue());
                // dbFoodMap putAll
                foodMap.keySet().forEach(key -> {
                    dbFoodMap.put(foodMap.get(key).food.getContent(), foodMap.get(key).food);
                });

                // menu에 연결할 음식 데이터 기록
                Map<CategoryCode, List<IdxFood>> ccFoodStrMap = dbMenuFoodStrMap.getOrDefault(servingAt, new HashMap<>());
                ccFoodStrMap.put(categoryCode, foodMap.values().stream().sorted(Comparator.comparing(IdxFood::getIdx)).toList());
                dbMenuFoodStrMap.put(servingAt, ccFoodStrMap);
            }
        }

        menuRepository.saveAll(
                getInsertMenuList(insertFood(dbFoodMap), dbMenuMap, dbMenuFoodStrMap)
        );
    }

    /**
     * DB에 등록할 Menu 데이터에 Food 연결, 최종 입력 리스트 형태로 반환
     */
    private List<Menu> getInsertMenuList(
            Map<String, Food> savedFoodMap,
            Map<Date, Map<CategoryCode, Menu>> dbMenuMap,
            Map<Date, Map<CategoryCode, List<IdxFood>>> dbMenuFoodMap) {
        List<Menu> dbMenuList = new ArrayList<>();
        dbMenuMap.forEach((servingAt, ccMap) -> {
            ccMap.forEach((cc, menu) -> {
                for (IdxFood idxFood : dbMenuFoodMap.get(servingAt).get(cc)) {
                    if (savedFoodMap.containsKey(idxFood.food.getContent())) {
                        menu.addFood(savedFoodMap.get(idxFood.food.getContent()));
                        dbMenuList.add(menu);
                    }
                    // dbFoodMap 데이터를 전부 DB에 입력했다는 것을 전제하므로 else 상정하지 않음
                }
            });
        });
        return dbMenuList;
    }

    /**
     * 기존 등록된 Food 를 제외하고 DB 등록
     * 전체 Food 데이터에 대해 Id 값 설정 후 반환
     */
    private Map<String, Food> insertFood(Map<String, Food> foodMap) {
        // 기존에 DB에 등록되어 있는 Food 검색
        Map<String, Food> savedFoodMap = foodRepository.findByContentIn(foodMap.keySet().stream().toList())
                .stream().collect(Collectors.toMap(Food::getContent, Function.identity()));
        // DB에 저장되어 있지 않은 Food 필터링 -> DB 저장
        Map<String, Food> insertedFoodMap = foodRepository.saveAll(
                foodMap.values().stream().filter(food -> !savedFoodMap.containsKey(food.getContent())).toList()
        ).stream().collect(Collectors.toMap(Food::getContent, Function.identity()));

        savedFoodMap.putAll(insertedFoodMap);
        return savedFoodMap;
    }

    private Map<String, IdxFood> parse20DataToFoodMap(String cellStr) {
        Map<String, IdxFood> foodMap = new HashMap<>();
        AtomicInteger idx = new AtomicInteger();
        Arrays.stream(cellStr.split(String.valueOf(BRACKET_PATTERN))).filter(o -> !o.isEmpty() && !o.isBlank())
                .forEach(content -> {
                    Arrays.stream(content.split("\n|\\*|/|&")).forEach(foodStr -> {
                        if (foodStr.isEmpty() || foodStr.isBlank()) return;
                        // 20층 데이터는 알러지 정보 제공x
                        foodMap.put(foodStr, new IdxFood(
                                idx.getAndIncrement(),
                                Food.builder()
                                        .content(foodStr)
                                        .foodAllergyCodeList(new ArrayList<>())
                                        .build()
                        ));
                    });
                });
        return foodMap;
    }

    /**
     * OCR 결과에 8이 &로 읽혀있거나 하는 등의 잘못된 문자를 수정
     */
    private String getVerifiedString(String value) {
        if (SERVING_AT_PATTERN.matcher(value.replaceAll(" ", "")).find()) return value.replaceAll(" ", "");
        StringBuilder sb = new StringBuilder();
        ArrayDeque<Character> que = new ArrayDeque<>();
        for (char c : value.toCharArray()) {
            char appendC = c;
            if (c == '(') que.addLast('(');
            else if (c == ')' && !que.isEmpty()) que.pollLast();
            else if (c == '8' && que.isEmpty()) appendC = '&';
            else if (c == '&' && !que.isEmpty()) appendC = '8';
            else if (c >= '1' && c <= '9' && que.isEmpty()) { // 여는 괄호 증발
                sb.append('(');
                que.add('(');
            }
            sb.append(appendC);
        }
        return sb.toString();
    }

    /**
     * 세로 분할한 메뉴 텍스트 취합
     * 10층 메뉴 전용
     */
    private Map<CategoryCode, List<String>> gathering10MenuText(List<FieldResponse> fields) {
        // 그룹화
        Map<CategoryCode, List<TextLabelBox>> groupByVerticalSection =
                fields.stream()
                        .map(TextLabelBox::generate)
                        .collect(
                                Collectors.groupingBy(o -> CategoryCode.getByCode(imageService.getVerticalSection(o.left())),
                                        Collectors.mapping(Function.identity(), Collectors.toList()))
                        );

        // 같은 줄/한 단어 텍스트 결합
        Map<CategoryCode, List<String>> result = new HashMap<>();
        groupByVerticalSection.keySet().forEach(key -> {
            List<TextLabelBox> values = groupByVerticalSection.get(key);
            Collections.sort(values);
            // 도시락: labeling box 차이, 괄호 유무로 결합
            List<String> contents = new ArrayList<>();
            if (key.equals(CategoryCode.LUNCH_BOX)) {
                ArrayDeque<TextLabelBox> oneLineQ = new ArrayDeque<>();
                values.forEach(o -> {
                    String trimTxt = o.text().trim();
                    if (oneLineQ.isEmpty() || (
                            !oneLineQ.getLast().allowableOneLine(o) &&
                                    (!trimTxt.startsWith("(") && !trimTxt.startsWith(")"))
                    )) {
                        oneLineQ.addLast(o);
                    } else {
                        assert oneLineQ.peekLast() != null;
                        TextLabelBox tmp = oneLineQ.pollLast();
                        oneLineQ.addLast(
                                TextLabelBox.builder()
                                        .text(tmp.text() + " " + trimTxt)
                                        .left(tmp.left())
                                        .right(tmp.right())
                                        .top(tmp.top())
                                        .down(tmp.down())
                                        .build()
                        );
                    }
                });
                contents.addAll(oneLineQ.stream().map(o -> getVerifiedString(o.text())).toList());
            }
            // 샌드위치/샐러드: '/' 값으로 split
            else {
                StringBuilder sb = new StringBuilder();
                for (TextLabelBox obj : values) sb.append(obj.text()).append(' ');
                String[] verifiedStringList = getVerifiedString(sb.toString()).replaceAll("&", "").split("/");
                List<String> finalStrList = new ArrayList<>();
                // 알러지 정보 괄호 유무 통해 한 번 더 나누기
                for (String s : verifiedStringList) {
                    String trimS = s.trim();
                    Matcher matcher = BRACKET_PATTERN.matcher(trimS);
                    if (matcher.find()) {
                        finalStrList.add(trimS.substring(0, matcher.end()));
                        trimS = trimS.substring(matcher.end());
                    }
                    if (!trimS.isEmpty()) finalStrList.add(trimS);
                }
                contents.addAll(finalStrList);
            }

            result.put(key, contents);
        });

        return result;
    }

    public List<MenuResponse> findMenuList() {
        CommonService.DBBetweenDate dbBetweenDate = commonService.getDBBetweenDate();
        List<Menu> menuList = menuRepository.findMenuOfWeek(dbBetweenDate.start, dbBetweenDate.end);
        List<MenuResponse> menuResponseList = new ArrayList<>();
        if (menuList.isEmpty())
            logger.error("menu를 찾지 못했습니다.");

        menuList.forEach(menu -> {
            List<FoodResponse> foodList = new ArrayList<>();
            menu.getMenuFoodList().forEach(menuFood -> {
                foodList.add(
                        FoodResponse.builder()
                                .content(menuFood.getFood().getContent())
                                .allergyList(((CategoryCode) menu.getCategoryCode()).getRestaurantCode().equals(RestaurantCode.FLOOR_10) ? menuFood.getFood().getFoodAllergyCodeList().stream().map(
                                        allergyCode -> allergyCode.getAllergyCode().getIngredient()
                                ).toList() : List.of())
                                .build()
                );
            });

            menuResponseList.add(MenuResponse.builder()
                    .servingAt(menu.getServingAt())
                    .category(((CategoryCode) menu.getCategoryCode()).getTitle())
                    .foodList(foodList)
                    .build());
        });
        return menuResponseList;
    }

    /**
     * 음식 순서를 저장하기 위한 객체
     */
    @Data
    @AllArgsConstructor
    static class IdxFood {
        Integer idx;
        Food food;
    }
}
