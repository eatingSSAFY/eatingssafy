package com.A204.service;

import com.A204.dto.request.AddTagRequest;
import com.A204.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    @InjectMocks
    private TagService tagService;

    @Mock
    TagRepository tagRepository;

    @Nested
    @DisplayName("TagService save Test")
    class saveTest {
        private String added;

        private AddTagRequest addTagRequest;

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {
            @Test
            void saveTest() throws Exception {
                //given
                added = "1";
                addTagRequest = new AddTagRequest(added);

                //when
                tagService.save(addTagRequest);

                //then - repository 동작 확인
                verify(tagRepository, times(1)).save(any());
            }
        }
    }

}
