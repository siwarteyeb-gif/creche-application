package com.example.crecheapplication.repository;

import com.example.crecheapplication.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent,Long> {
    Optional<Parent> findByNomAndPrenom(String nom, String prenom);
}
