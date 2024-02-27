package com.A204.repository;

import com.A204.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Integer> {
    List<Food> findByContentIn(List<String> contentList);
}
