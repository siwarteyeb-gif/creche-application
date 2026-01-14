package com.example.crecheapplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bebe")
public class Bebe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    private LocalDate dateNais;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "bebe",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Activitebebe> activites;
}