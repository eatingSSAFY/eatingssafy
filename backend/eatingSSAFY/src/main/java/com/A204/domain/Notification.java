package com.A204.domain;

import com.A204.dto.request.AppTokenRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notification")
public class Notification {
    @Id
    @Column(name = "app_token")
    private byte[] appToken;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "preference")
    private Boolean preference;

    @Column(name = "amount")
    private Boolean amount;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public static Notification generate(AppTokenRequest request, Optional<Notification> saved) {
        if (saved.isPresent()) {
            Notification notification = saved.get();
            notification.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            if (request.userId() != null) notification.setUserId(request.userId());
            if (request.preferenceNotification() != null) notification.setPreference(request.preferenceNotification());
            if (request.amountNotification() != null) notification.setAmount(request.amountNotification());
            return notification;
        }

        return Notification.builder()
                .appToken(request.appToken().getBytes())
                .userId(request.userId())
                .preference(request.preferenceNotification() != null && request.preferenceNotification())
                .amount(request.amountNotification() != null && request.amountNotification())
                .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }
}
