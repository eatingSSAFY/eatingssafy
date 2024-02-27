package com.A204.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "allergy_code")
public class AllergyCode {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "ingredient", nullable = false, length = 100)
    private String ingredient;
}
