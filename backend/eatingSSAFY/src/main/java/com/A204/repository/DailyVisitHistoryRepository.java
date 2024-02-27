package com.A204.repository;

import com.A204.domain.DailyVisitHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.Optional;

public interface DailyVisitHistoryRepository extends JpaRepository<DailyVisitHistory, Long> {
    @Query(value = "select * " +
            "from daily_visit_history as dvh\n" +
            "where dvh.user_id = :userId and dvh.visited_at = :today",
            nativeQuery = true)
    Optional<DailyVisitHistory> findDailyVisitHistory(@Param("userId") Long userId, @Param("today") Date today);

    @Query("select dvh.cnt from DailyVisitHistory as dvh where dvh.userId = :userId and dvh.visitedAt = :today")
    Integer findCntOfUser(@Param("userId") Long userId, @Param("today") Date today);
}
