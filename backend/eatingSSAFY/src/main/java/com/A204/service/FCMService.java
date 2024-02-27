package com.A204.service;

import com.A204.dto.fcm.FCMNotificationRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FCMService {
    private final FirebaseMessaging firebaseMessaging;

    private final Logger logger = LoggerFactory.getLogger(FCMService.class);

    /**
     * FCM 서버에 요청, 앱 토큰 정보를 통해 푸쉬 알림 송신
     * return true: 알림 성공
     * return false: 알림 실패 -> 앱 토큰 정보 삭제
     */
    public boolean send(FCMNotificationRequest request) {
        Message message = Message.builder()
                .setToken(request.token())
                .setNotification(Notification.builder() // com.google.firebase.messaging.Notification
                        .setTitle(request.title())
                        .setBody(request.body())
                        .build())
                .build();
        try {
            firebaseMessaging.send(message);
            return true;
        } catch (FirebaseMessagingException e) {
            logger.error(e.getLocalizedMessage());
            logger.error("알림 보내기에 실패하였습니다.");
            return false;
        }
    }
}
