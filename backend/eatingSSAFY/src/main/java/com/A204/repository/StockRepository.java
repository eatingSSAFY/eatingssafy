package com.A204.repository;

import com.A204.domain.ICntValueOfCategory;
import com.A204.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Integer> {
    @Query("select stock.categoryCode as categoryCode, stock.cnt as value from Stock as stock where stock.servingAt = :servingAt")
    List<ICntValueOfCategory> findStockOfDay(@Param("servingAt") Date servingAt);
}
