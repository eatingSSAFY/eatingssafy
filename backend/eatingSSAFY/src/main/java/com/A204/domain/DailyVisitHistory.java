package com.A204.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "daily_visit_history")
public class DailyVisitHistory {
    @Id
    @Column(name = "user_id", unique = true)
    Long userId;

    @Column(name = "cnt")
    Integer cnt;

    @Column(name = "visited_at")
    Date visitedAt;
}
