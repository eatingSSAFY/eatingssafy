package com.A204.repository;

import com.A204.domain.LunchTime;
import com.A204.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface LunchTimeRepository extends JpaRepository<LunchTime, Integer> {
    @Query(value = "select * " +
            "from lunch_time as lt\n" +
            "where :today between lt.period_start and lt.period_end",
            nativeQuery = true)
    List<LunchTime> findLunchTimeOfWeek(@Param("today") Date today);
}
