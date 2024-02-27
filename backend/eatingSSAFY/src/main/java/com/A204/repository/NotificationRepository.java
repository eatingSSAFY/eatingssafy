package com.A204.repository;

import com.A204.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, byte[]> {

}
