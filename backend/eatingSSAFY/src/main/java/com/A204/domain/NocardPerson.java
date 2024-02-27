package com.A204.domain;

import com.A204.dto.request.AddNocardPersonRequest;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "nocard_person")
public class NocardPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    @Column(name = "id", updatable = false, unique = true)
    private Integer id;

    @Column(name = "person_name", nullable = false, length = 20)
    private String personName;

    @Column(name = "person_id", nullable = false, length = 50)
    private String personId;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "exported_cnt", nullable = false)
    private int exportedCnt;

    @Column(name = "exported_at", nullable = true)
    private Timestamp exportedAt;

    public static NocardPerson convert(AddNocardPersonRequest request) {
        return NocardPerson.builder()
                .personName(request.personName())
                .personId(request.personId())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }
}
