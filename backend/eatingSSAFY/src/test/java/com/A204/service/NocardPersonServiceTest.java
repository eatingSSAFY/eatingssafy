package com.A204.service;

import com.A204.domain.NocardPerson;
import com.A204.dto.request.AddNocardPersonRequest;
import com.A204.repository.NocardPersonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NocardPersonServiceTest {
    @InjectMocks
    private NocardPersonService nocardPersonService;

    @Mock
    NocardPersonRepository nocardPersonRepository;

    @Mock
    ExcelService excelService;

    @Nested
    @DisplayName("NocardPersonService save Test")
    class saveTest {
        private String personName;
        private String personId;

        private AddNocardPersonRequest addNocardPersonRequest;

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {
            @Test
            void saveTest() throws Exception {
                //given
                personName = "이름";
                personId = "1048000";
                addNocardPersonRequest = new AddNocardPersonRequest(personName, personId);

                //when
                nocardPersonService.save(addNocardPersonRequest);

                //then - repository 동작 확인
                verify(nocardPersonRepository, times(1)).save(any());
            }
        }

        @Nested
        @DisplayName("배치 정상 케이스")
        class SuccessCaseByBatch {
            @Captor
            ArgumentCaptor<List<NocardPerson>> listArgumentCaptor;

            @Test
            void exportNormalCase() {
                //when: DB 조회 결과수가 있을 때
                List<NocardPerson> dbResult = new ArrayList<>();
                Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
                int exportedCnt = 0;
                dbResult.add(
                        NocardPerson.builder()
                                .id(1)
                                .personName("이름")
                                .createdAt(createdAt)
                                .exportedAt(null)
                                .exportedCnt(exportedCnt)
                                .build()
                );
                lenient().when(nocardPersonRepository.findNocardPersonOfDay(any(), any())).thenReturn(dbResult);
                nocardPersonService.export();

                //then
                verify(nocardPersonRepository, times(1)).findNocardPersonOfDay(any(), any());
                verify(excelService, times(1)).writeNocardPersonExcel(dbResult);
                verify(nocardPersonRepository, times(1)).saveAll(listArgumentCaptor.capture());
                listArgumentCaptor.getValue().forEach(o -> {
                    assertEquals(o.getExportedCnt(), exportedCnt + 1);
                    assertNotNull(o.getExportedAt());
                    assertTrue(o.getExportedAt().after(createdAt));
                });
            }

            @Test
            void exportEmptyCase() {
                //when: DB 조회 결과수가 0일 때
                nocardPersonService.export();
                lenient().when(nocardPersonRepository.findNocardPersonOfDay(any(), any())).thenReturn(Collections.emptyList());

                //then
                verify(nocardPersonRepository, times(1)).findNocardPersonOfDay(any(), any());
                verify(excelService, times(1)).writeNocardPersonExcel(Collections.emptyList());
                verify(nocardPersonRepository, never()).saveAll(any());
            }
        }
    }
}
