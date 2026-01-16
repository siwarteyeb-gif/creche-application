package com.example.crecheapplication.repository;

import com.example.crecheapplication.model.Bebe;
import com.example.crecheapplication.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BebeRepository extends JpaRepository<Bebe,Long> {
    Optional<Bebe> findById(Long id);

}
