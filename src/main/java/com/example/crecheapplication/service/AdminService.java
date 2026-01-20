package com.example.crecheapplication.service;

import com.example.crecheapplication.model.Activitebebe;
import com.example.crecheapplication.model.Bebe;
import com.example.crecheapplication.model.Parent;
import com.example.crecheapplication.repository.BebeRepository;
import com.example.crecheapplication.repository.ParentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {

    private final ParentRepository parentRepository;
    private final BebeRepository bebeRepository;
    private final PasswordEncoder passwordEncoder;


    public AdminService(ParentRepository parentRepository, BebeRepository bebeRepository,PasswordEncoder passwordEncoder
) {
        this.parentRepository = parentRepository;
        this.bebeRepository = bebeRepository;
        this.passwordEncoder=passwordEncoder;
    }

    // 1️⃣ afficher tous les parents
    public List<Parent> getAllParents() {
        return parentRepository.findByRole("ROLE_PARENT");
    }

    // 2️⃣ afficher tous les bébés
    public List<Bebe> getAllBebes() {
        return bebeRepository.findAll();
    }

    // 3️⃣ parent d’un bébé
    public Parent getParentOfBebe(Long idBebe) {
        Bebe bebe = bebeRepository.findById(idBebe)
                .orElseThrow(() -> new RuntimeException("Bebe introuvable"));
        return bebe.getParent();
    }

    // 4️⃣ activités d’un bébé
    public List<Activitebebe> getActivitesOfBebe(Long idBebe) {
        Bebe bebe = bebeRepository.findById(idBebe)
                .orElseThrow(() -> new RuntimeException("Bebe introuvable"));
        return bebe.getActivites();
    }

    // 5️⃣ changer rôle (ROLE_PARENT / ROLE_ADMIN)
    public Parent updateRole(Long idParent, String role) {
        Parent parent = parentRepository.findById(idParent)
                .orElseThrow(() -> new RuntimeException("Parent introuvable"));
        parent.setRole(role);
        return parentRepository.save(parent);
    }

    public void deleteParent(Long idParent) {
        parentRepository.deleteById(idParent);
    }
    public Parent createParent(String nom, String prenom, String email, String telephone, String password, String role) {
        if (parentRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }
        Parent parent = new Parent();
        parent.setNom(nom);
        parent.setPrenom(prenom);
        parent.setEmail(email);
        parent.setTelephone(telephone);
        parent.setPassword(passwordEncoder.encode(password));
        parent.setRole(role);
        LocalDateTime now = LocalDateTime.now();
        parent.setCreatedAt(now);
        parent.setModifiedAt(now);

        return parentRepository.save(parent);
    }
    public Bebe ajouterBebe(Long parentId, String nom, String prenom, LocalDate dateNais) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent introuvable"));

        Bebe bebe = new Bebe();
        bebe.setNom(nom);
        bebe.setPrenom(prenom);
        bebe.setDateNais(dateNais);
        bebe.setParent(parent);

        LocalDateTime now = LocalDateTime.now();
        bebe.setCreatedAt(now);
        bebe.setModifiedAt(now);

        return bebeRepository.save(bebe);
    }
}


