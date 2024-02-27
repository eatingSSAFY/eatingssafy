package com.A204.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "food")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "content")
    private String content;

    @OneToMany(
            mappedBy = "food",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<FoodAllergyCode> foodAllergyCodeList = new ArrayList<>();

    public void setAllergyCodeList(Stream<AllergyCode> allergyCodeList) {
        this.foodAllergyCodeList = allergyCodeList.map(o -> FoodAllergyCode.builder()
                        .allergyCode(o)
                        .food(this)
                        .build())
                .toList();
    }
}
