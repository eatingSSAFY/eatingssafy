package com.A204.service;

import com.A204.domain.Tag;
import com.A204.dto.request.AddTagRequest;
import com.A204.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TagService {
    private final TagRepository tagRepository;

    /**
     * 카드 소지자 태깅 정보 업데이트
     *
     */
    @Transactional
    public void save(AddTagRequest request) {
        tagRepository.save(Tag.convert(request));
    }
}
