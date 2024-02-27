package com.A204.domain;

import com.A204.code.CategoryCode;
import com.A204.converter.CategoryCodeConverter;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "menu",
        uniqueConstraints = {
                @UniqueConstraint(name = "UniqueServingAtAndCategoryCode", columnNames = {
                        "serving_at", "category_code"
                })
        })
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    @Column(name = "id", updatable = false, unique = true)
    private Integer id;

    @Column(name = "serving_at", nullable = false)
    private Date servingAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Convert(converter = CategoryCodeConverter.class)
    @Column(name = "category_code", nullable = false)
    private Enum<CategoryCode> categoryCode;

    @OneToMany(
            mappedBy = "menu",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<MenuFood> menuFoodList = new ArrayList<>();

    public void addFood(Food food) {
        MenuFood menuFood = MenuFood.builder()
                .menu(this)
                .food(food)
                .build();
        this.menuFoodList.add(menuFood);
    }
}