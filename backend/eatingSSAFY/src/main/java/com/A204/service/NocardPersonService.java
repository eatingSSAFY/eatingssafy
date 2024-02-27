package com.A204.service;


import com.A204.domain.NocardPerson;
import com.A204.dto.request.AddNocardPersonRequest;
import com.A204.repository.NocardPersonRepository;
import com.A204.validation.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NocardPersonService {
    private final NocardPersonRepository nocardPersonRepository;
    private final ExcelService excelService;

    private final Logger logger = LoggerFactory.getLogger(NocardPersonService.class);

    /**
     * 카드 미소지자 추가 메서드
     */
    @Transactional
    public void save(AddNocardPersonRequest request) {
        if (nocardPersonRepository.findDuplicatedNocardPerson(
                Timestamp.valueOf(LocalDate.now().atStartOfDay()),
                Timestamp.valueOf(LocalDate.now().plusDays(1).atStartOfDay().minusSeconds(1)),
                request.personName(),
                request.personId()
        ).equals(0))
            nocardPersonRepository.save(NocardPerson.convert(request));
        else {
            logger.error("카드 미소지자 등록이 완료된 사용자입니다.");
            throw new CustomException(HttpStatus.ALREADY_REPORTED, "카드 미소지자 등록이 완료된 사용자입니다.");
        }
    }

    @Transactional
    public String export() {
        List<NocardPerson> list = nocardPersonRepository.findNocardPersonOfDay(
                Timestamp.valueOf(LocalDate.now().atStartOfDay()),
                Timestamp.valueOf(LocalDate.now().plusDays(1).atStartOfDay().minusSeconds(1))
        );
        String filePath = excelService.writeNocardPersonExcel(list);
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        list.forEach(o -> {
            o.setExportedAt(now);
            o.setExportedCnt(o.getExportedCnt() + 1);
        });
        if (!list.isEmpty())
            nocardPersonRepository.saveAll(list);
        return filePath;
    }
}
