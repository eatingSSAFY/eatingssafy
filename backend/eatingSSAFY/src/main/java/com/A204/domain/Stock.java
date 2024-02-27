package com.A204.domain;

import com.A204.code.CategoryCode;
import com.A204.converter.CategoryCodeConverter;
import jakarta.persistence.*;
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
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true)
    private Integer id;

    @Convert(converter = CategoryCodeConverter.class)
    @Column(name = "category_code", nullable = false, length = 100)
    private Enum<CategoryCode> categoryCode;

    @Column(name = "serving_at", nullable = false)
    private Date servingAt;

    @Column(name = "cnt", nullable = false)
    private Integer cnt;
}
