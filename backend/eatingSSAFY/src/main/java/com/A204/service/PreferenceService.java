package com.A204.service;

import com.A204.domain.*;
import com.A204.dto.request.PageRequest;
import com.A204.dto.request.PreferenceRequest;
import com.A204.dto.response.MenuPreferenceResponse;
import com.A204.dto.response.PreferenceResponse;
import com.A204.repository.MenuFoodRepository;
import com.A204.repository.MenuRepository;
import com.A204.repository.PreferenceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PreferenceService {
    private final PreferenceRepository preferenceRepository;
    private final MenuRepository menuRepository;
    private final MenuFoodRepository menuFoodRepository;
    private final CommonService commonService;

    @Transactional
    public void save(Long userId, PreferenceRequest request) {
        if (request.value().equals(0)) {
            preferenceRepository.deleteById(userId, request.foodId());
            return;
        }

        preferenceRepository.save(Preference.builder()
                .userId(userId)
                .foodId(request.foodId())
                .value(request.value() > 0)
                .build());
    }

    /**
     * 단일 유저의 좋아요 리스트 반환
     */
    public List<PreferenceResponse> findList(Long userId, PageRequest page) {
        List<PreferenceResponse> result = new ArrayList<>();
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page.page(), page.pageSize());
        preferenceRepository.findByUserIdAndValue(userId, true, pageable).get().forEachOrdered(o -> {
            result.add(PreferenceResponse.builder()
                    .foodId(o.getFoodId())
                    .content(o.getFood().getContent())
                    .build());
        });

        return result;
    }

    /**
     * 이번주 메뉴에 대해 좋아요/싫어요 개수 및 로그인 유저의 좋아요/싫어요 데이터 리스트 반환
     */
    public List<MenuPreferenceResponse> findMenuPreferenceList(Long userId) {
        CommonService.DBBetweenDate dbBetweenDate = commonService.getDBBetweenDate();
        List<IMenuFoodContent> menuFoodList = menuFoodRepository.findTopFoodContent(dbBetweenDate.start, dbBetweenDate.end);
        Map<Integer, ILikeCntByFoodId> result = preferenceRepository.findCountByFoodIdIn(
                        menuFoodList.stream()
                                .map(IMenuFoodContent::getFoodId)
                                .toList()
                )
                .stream().collect(Collectors.toMap(ILikeCntByFoodId::getFoodId, Function.identity()));

        Map<Integer, Preference> userPreferenceMap = new HashMap<>();
        // 로그인 유저인 경우, 해당 메뉴에 대해 좋아요/싫어요를 눌렀는지 정보를 포함
        if (userId != null) {
            for (Preference preference : preferenceRepository.findByUserIdAndFoodIdIn(userId, result.keySet().stream().toList())) {
                userPreferenceMap.put(
                        preference.getFoodId(),
                        preference
                );
            }
        }

        return menuFoodList.stream().map(o -> MenuPreferenceResponse.builder()
                .servingAt(o.getServingAt())
                .category(o.getCategoryCode())
                .foodId(o.getFoodId())
                .likeCnt(result.containsKey(o.getFoodId()) ? result.get(o.getFoodId()).getLikeCnt() : 0)
                .dislikeCnt(result.containsKey(o.getFoodId()) ? result.get(o.getFoodId()).getDislikeCnt() : 0)
                .like(userPreferenceMap.containsKey(o.getFoodId()) ? userPreferenceMap.get(o.getFoodId()).getValue() : null)
                .build()).toList();
    }
}
