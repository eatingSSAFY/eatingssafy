package com.A204.repository;

import com.A204.domain.Amount;
import com.A204.domain.ICntValueOfCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface AmountRepository extends JpaRepository<Amount, Integer> {
    @Query("select categoryCode as categoryCode, sum(cnt) as value from Amount group by categoryCode, servingAt having servingAt = :today")
    List<ICntValueOfCategory> findAmountOfCategory(@Param("today") Date today);
}
