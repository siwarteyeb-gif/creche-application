package com.example.crecheapplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "activitesBebes")
public class Activitebebe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String typeActivite;
    private LocalDate date;
    private LocalTime temps;
    private String notes;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "bebe_id", nullable = false)
    private Bebe bebe;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
