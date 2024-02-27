package com.A204.repository;

import com.A204.domain.NocardPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface NocardPersonRepository extends JpaRepository<NocardPerson, Integer> {
    @Query("select nocardPerson from NocardPerson as nocardPerson where nocardPerson.createdAt between :start and :end")
    List<NocardPerson> findNocardPersonOfDay(@Param("start") Timestamp start, @Param("end") Timestamp end);

    @Query("select count(*) from NocardPerson as nocardPerson where nocardPerson.createdAt between :start and :end")
    Integer findNocardPersonCntOfDay(@Param("start") Timestamp start, @Param("end") Timestamp end);

    @Query("select count(*) from NocardPerson as nocardPerson where nocardPerson.createdAt between :start and :end\n"
            + "and nocardPerson.personName = :personName and nocardPerson.personId = :personId")
    Integer findDuplicatedNocardPerson(@Param("start") Timestamp start, @Param("end") Timestamp end, @Param("personName") String personName, @Param("personId") String personId);

}
