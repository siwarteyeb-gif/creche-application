package com.example.crecheapplication.service;

import com.example.crecheapplication.model.Activitebebe;
import com.example.crecheapplication.model.Bebe;
import com.example.crecheapplication.model.Parent;
import com.example.crecheapplication.repository.ActivitesRepository;
import com.example.crecheapplication.repository.BebeRepository;
import com.example.crecheapplication.repository.ParentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AdminService {

    private final ParentRepository parentRepository;
    private final BebeRepository bebeRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivitesRepository activitesRepository;

    public AdminService(ParentRepository parentRepository, BebeRepository bebeRepository,PasswordEncoder passwordEncoder,ActivitesRepository activitesRepository
) {
        this.parentRepository = parentRepository;
        this.bebeRepository = bebeRepository;
        this.passwordEncoder=passwordEncoder;
        this.activitesRepository=activitesRepository;
    }

    // 1️⃣ afficher tous les parents
    public List<Parent> getAllParents() {
        return parentRepository.findByRole("ROLE_PARENT");
    }

    // 2️⃣ afficher tous les bébés
    public List<Bebe> getAllBebes() {
        return bebeRepository.findAll();
    }
    public List<Activitebebe> getAllActivites() {
        return activitesRepository.findAll();
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
        Parent parent = parentRepository.findById(idParent)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Parent introuvable"
                        )
                );

        parentRepository.delete(parent);
    }   public Parent createParent(String nom,String prenom,String email,String telephone,String password,String role) {
        if (parentRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email déjà utilisé");
        }
        if (!"ROLE_ADMIN".equals(role) && !"ROLE_PARENT".equals(role)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Role invalide, uniquement ROLE_ADMIN ou ROLE_PARENT");
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
    public Bebe updateBebe(Long id, Bebe bebeDetails) {
        Bebe bebe = bebeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bébé introuvable"));

        bebe.setNom(bebeDetails.getNom());
        bebe.setPrenom(bebeDetails.getPrenom());
        bebe.setDateNais(bebeDetails.getDateNais());
        bebe.setModifiedAt(LocalDateTime.now());
        if (bebeDetails.getParent() != null) {
            Parent parent = parentRepository.findById(bebeDetails.getParent().getId())
                    .orElseThrow(() -> new RuntimeException("Parent introuvable"));
            bebe.setParent(parent);
        }
        return bebeRepository.save(bebe);
    }

    public void deleteBebe(Long id) {
        Bebe bebe = bebeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bébé introuvable"));
        bebeRepository.delete(bebe);
    }
    public List<Activitebebe> getActivitesDuJour(Long idBebe) {

        Bebe bebe = bebeRepository.findById(idBebe)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Bébé introuvable"));

        LocalDate today = LocalDate.now();

        return bebe.getActivites().stream()
                .filter(a -> a.getDate().equals(today))
                .toList();
    }
    public Activitebebe ajouterActivite(Long idBebe, Activitebebe activiteBebe) {
        Bebe bebe = bebeRepository.findById(idBebe)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Bébé introuvable"));
        activiteBebe.setBebe(bebe);
        activiteBebe.setDate(LocalDate.now());
        activiteBebe.setTemps(LocalTime.now());
        LocalDateTime now = LocalDateTime.now();
        activiteBebe.setCreatedAt(now);
        activiteBebe.setModifiedAt(now);
        return activitesRepository.save(activiteBebe);
    }
    public Activitebebe modifierActivite(Long idActivite, Activitebebe activiteDetails) {

        Activitebebe activite = activitesRepository.findById(idActivite)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Activité introuvable"));
        activite.setTypeActivite(activiteDetails.getTypeActivite());
        activite.setNotes(activiteDetails.getNotes());
        activite.setModifiedAt(LocalDateTime.now());
        return activitesRepository.save(activite);
    }
    import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

    public void supprimerActivite(Long idActivite) {
        Activitebebe activite = activitesRepository.findById(idActivite)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Activité introuvable"));

        activitesRepository.delete(activite);
    }

}


