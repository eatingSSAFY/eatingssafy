package com.A204.repository;

import com.A204.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query("select count(*) from Tag where createdAt between :start and :end")
    Integer findTagCntOfDay(@Param("start") Timestamp start, @Param("end") Timestamp end);
}
