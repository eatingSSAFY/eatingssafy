package com.A204.repository;

import com.A204.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    @Query("select menu from Menu as menu where menu.servingAt between :start and :end order by menu.servingAt, menu.categoryCode")
    List<Menu> findMenuOfWeek(@Param("start") Date start, @Param("end") Date end);
}
