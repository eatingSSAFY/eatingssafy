package com.A204.repository;

import com.A204.domain.AmountNotification;
import com.A204.domain.AmountNotificationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmountNotificationRepository extends JpaRepository<AmountNotification, AmountNotificationId> {
}
