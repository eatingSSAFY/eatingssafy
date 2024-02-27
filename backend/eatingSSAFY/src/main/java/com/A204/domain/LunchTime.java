package com.A204.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lunch_time")
public class LunchTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "lunch_time")
    private Time lunchTime;

    @Column(name = "period_start")
    private Date periodStart;

    @Column(name = "period_end")
    private Date periodEnd;
}
