package com.example.crecheapplication.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="parent")
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    @Email(message = "email doit etre valide")
    @Column(unique = true,nullable = false)
    private String email;
    private String telephone;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL)
    private List<Bebe> bebes;


}
