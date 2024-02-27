package com.A204.service;

import com.A204.domain.User;
import com.A204.dto.response.UserResponse;
import com.A204.repository.UserRepository;
import com.A204.validation.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public void upsert(User user) {
        User newUser = User.generate(user, userRepository.findUserById(user.getId()));
        userRepository.save(newUser);
    }

    @Transactional
    public UserResponse findUserByKakaoId(Long userId) {
        User user = userRepository.findUserById(userId).orElse(null);
        if (user == null) {
            logger.error("user을 찾지 못했습니다.");
            throw new CustomException(HttpStatus.NOT_FOUND, "user을 찾지 못했습니다.");
        }
        return UserResponse.builder()
                .kakaoId(user.getId())
                .personNickname(user.getPersonNickname())
                .build();
    }

}
