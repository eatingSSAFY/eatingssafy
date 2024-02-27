package com.A204.domain;

import com.A204.code.CategoryCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "amount_notification")
@IdClass(AmountNotificationId.class)
public class AmountNotification {
    @Id
    @Column
    private Date servingAt;

    @Id
    @Column
    private Enum<CategoryCode> categoryCode;
}
