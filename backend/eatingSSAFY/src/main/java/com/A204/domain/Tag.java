package com.A204.domain;

import com.A204.dto.request.AddTagRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //auto_increment
    @Column(name = "id", updatable = false, unique = true)
    private Integer id;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    public static Tag convert(AddTagRequest request) {
        return Tag.builder()
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }
}
