package com.example.crecheapplication.repository;

import com.example.crecheapplication.model.Activitebebe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivitesRepository extends JpaRepository<Activitebebe,Long> {
}
