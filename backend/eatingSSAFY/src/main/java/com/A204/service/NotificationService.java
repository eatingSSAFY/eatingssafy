package com.A204.service;

import com.A204.code.CategoryCode;
import com.A204.domain.AmountNotification;
import com.A204.domain.AmountNotificationId;
import com.A204.domain.IPreferenceNoti;
import com.A204.domain.Notification;
import com.A204.dto.fcm.FCMNotificationRequest;
import com.A204.dto.request.AppTokenRequest;
import com.A204.dto.response.AmountResponse;
import com.A204.repository.AmountNotificationRepository;
import com.A204.repository.NotificationRepository;
import com.A204.repository.PreferenceRepository;
import com.A204.validation.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final PreferenceRepository preferenceRepository;
    private final AmountNotificationRepository amountNotificationRepository;
    private final FCMService fcmService;

    private final static String PREFERENCE_TITLE = "오늘 좋아요 등록한 메뉴가 제공돼요!";
    private final static String AMOUNT_TITLE = "재고가 얼마 남지 않았어요!";

    public Notification findNotification(String appToken) {
        Optional<Notification> notification = notificationRepository.findById(appToken.getBytes());
        if (notification.isEmpty()) throw new CustomException(HttpStatus.BAD_REQUEST, "앱 토큰 정보가 저장되어 있지 않습니다.");
        return notification.get();
    }

    /**
     * 앱으로부터 전송받은 APP TOKEN을 DB에 저장
     */
    @Transactional
    public void upsert(AppTokenRequest request) {
        Notification notification = Notification.generate(request, notificationRepository.findById(request.appToken().getBytes()));
        notificationRepository.save(notification);
    }

    /**
     * 오늘 사용자의 선호 메뉴가 나오는지 확인 후 푸쉬 알림
     */
    @Transactional
    public void sendPreference() {
        // 등록된 데이터 중, 선호 알림 ON & 회원가입 된 유저의 앱 토큰 필터링
        List<Long> userIdList = notificationRepository.findAll().stream()
                .filter(o -> o.getPreference() && o.getUserId() != null)
                .map(Notification::getUserId)
                .toList();
        List<IPreferenceNoti> preferenceList = preferenceRepository.findByTargetDateAndUserIdIn(Date.valueOf(LocalDate.now()), userIdList);
        Map<String, List<IPreferenceNoti>> groupByUserId = preferenceList.stream().collect(Collectors.groupingBy(o -> new String(o.getAppToken())));
        List<FCMNotificationRequest> requestList = new ArrayList<>();
        for (String appToken : groupByUserId.keySet()) {
            StringBuilder sb = new StringBuilder();
            for (IPreferenceNoti noti : groupByUserId.get(appToken)) {
                sb.append(noti.getContent()).append("\n");
            }
            requestList.add(
                    FCMNotificationRequest.builder()
                            .token(appToken)
                            .title(PREFERENCE_TITLE)
                            .body(sb.toString())
                            .build()
            );
        }

        // FCM 송신 실패한 경우, 토큰 정보 삭제하기 위한 리스트
        List<String> invalidTokenList = new ArrayList<>();
        requestList.forEach(o -> {
            if (!fcmService.send(o)) invalidTokenList.add(o.token());
        });

        // delete
        if (!invalidTokenList.isEmpty())
            deleteInvalidAppTokens(invalidTokenList);
    }

    /**
     * 재고 소진 임박 푸쉬 알림
     */
    @Transactional
    public void sendAmount(List<AmountNotification> list) {
        // 등록된 데이터 중, 재고 알림 ON인 경우의 앱 토큰 필터링
        List<String> appTokenList = notificationRepository.findAll().stream()
                .filter(Notification::getAmount)
                .map(o -> new String(o.getAppToken()))
                .toList();
        List<FCMNotificationRequest> requestList = new ArrayList<>();
        for (String appToken : appTokenList) {
            StringBuilder sb = new StringBuilder();
            for (AmountNotification amountNotification : list) {
                sb.append(((CategoryCode) amountNotification.getCategoryCode()).getTitle()).append('\n');
            }
            requestList.add(
                    FCMNotificationRequest.builder()
                            .token(appToken)
                            .title(AMOUNT_TITLE)
                            .body(sb.toString())
                            .build()
            );
        }

        // FCM 송신 실패한 경우, 토큰 정보 삭제하기 위한 리스트
        List<String> invalidTokenList = new ArrayList<>();
        requestList.forEach(o -> {
            if (!fcmService.send(o)) invalidTokenList.add(o.token());
        });

        // delete
        if (!invalidTokenList.isEmpty())
            deleteInvalidAppTokens(invalidTokenList);
    }

    /**
     * 계산된 재고를 바탕으로 소진 임박 여부를 확인합니다. </p>
     * 남은 재고량이 초기 수량의 10% 이하인 경우, 앱 사용자에게 소진 임박 푸시를 송신합니다.
     */
    @Transactional
    public void amountNotificationCheck(List<AmountResponse> amountResponseList) {
        List<CategoryCode> categoryCodeList = amountResponseList.stream()
                .filter(o -> o.stock() != 0)
                .filter(o -> ((double) o.value() / o.stock()) * 100 <= 10)
                .map(o -> CategoryCode.getByTitle(o.category())).toList();
        if (categoryCodeList.isEmpty()) return;

        Date servingAt = Date.valueOf(LocalDate.now()); // 배식일 = 오늘
        List<AmountNotificationId> ids = categoryCodeList.stream().map(o -> AmountNotificationId.builder()
                .servingAt(servingAt)
                .categoryCode(o)
                .build()).toList();

        // DB에 등록되어 있음 = 알림을 이미 송신함
        List<CategoryCode> saved = amountNotificationRepository.findAllById(ids).stream().map(o -> (CategoryCode) o.getCategoryCode()).toList();
        List<AmountNotification> todoNotification = categoryCodeList.stream()
                .filter(o -> !saved.contains(o))
                .map(o -> AmountNotification.builder()
                        .servingAt(servingAt)
                        .categoryCode(o)
                        .build())
                .toList();

        if (!todoNotification.isEmpty()) {
            amountNotificationRepository.saveAll(todoNotification);
            sendAmount(todoNotification);
        }
    }

    @Transactional
    protected void deleteInvalidAppTokens(List<String> list) {
        notificationRepository.deleteAllById(list.stream().map(String::getBytes).toList());
    }
}
