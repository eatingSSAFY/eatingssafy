package com.A204.service;

import com.A204.domain.AllergyCode;
import com.A204.repository.AllergyCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * DB 테이블의 값이 수정되지 않는 데이터를 1번만 읽어 오도록 구현한 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class StaticDBService {
    private final AllergyCodeRepository allergyCodeRepository;

    private Map<Integer, AllergyCode> allergyCodeMap;

    public Map<Integer, AllergyCode> allergyCodeMap() {
        return allergyCodeMap == null || allergyCodeMap.isEmpty() ? allergyCodeMap = allergyCodeRepository.findAll()
                .stream().collect(Collectors.toMap(AllergyCode::getId, Function.identity())) : allergyCodeMap;
    }
}
