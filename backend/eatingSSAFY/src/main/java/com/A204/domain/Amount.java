package com.A204.domain;

import com.A204.code.CategoryCode;
import com.A204.converter.CategoryCodeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "amount")
public class Amount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    @Column(name = "id", updatable = false, unique = true)
    private Integer id;

    @Column(name = "cnt", nullable = false)
    private Integer cnt;

    @Column(name = "camera_id", nullable = false)
    private Integer cameraId;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "serving_at", nullable = false)
    private Date servingAt;

    @Convert(converter = CategoryCodeConverter.class)
    @Column(name = "category_code", nullable = false, length = 100)
    private Enum<CategoryCode> categoryCode;
}
