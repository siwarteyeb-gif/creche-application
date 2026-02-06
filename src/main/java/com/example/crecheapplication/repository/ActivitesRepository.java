package com.example.crecheapplication.repository;

import com.example.crecheapplication.model.Activitebebe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ActivitesRepository extends JpaRepository<Activitebebe,Long> {

    @Query("""
        SELECT a
        FROM Activitebebe a
        WHERE a.bebe.id = :bebeId
        AND a.bebe.parent.id = :parentId
        AND a.date = :date
        ORDER BY a.temps ASC
    """)
    List<Activitebebe> findActivitesDuJour(
            @Param("bebeId") Long bebeId,
            @Param("parentId") Long parentId,
            @Param("date") LocalDate date);
    @Query("""
    SELECT a
    FROM Activitebebe a
    WHERE a.bebe.id = :bebeId
      AND a.bebe.parent.id = :parentId
    ORDER BY a.date DESC, a.temps DESC
""")
    List<Activitebebe> findLastActivite(
            @Param("bebeId") Long bebeId,
            @Param("parentId") Long parentId,
            Pageable pageable
    );
    @Query("""
    SELECT a
    FROM Activitebebe a
    WHERE a.bebe.id = :bebeId
    AND a.date = :date
    ORDER BY a.temps ASC
""")
    List<Activitebebe> findActivitesDuJourAdmin(
            @Param("bebeId") Long bebeId,
            @Param("date") LocalDate date
    );
}


