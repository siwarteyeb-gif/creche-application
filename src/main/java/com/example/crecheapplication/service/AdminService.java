package com.example.crecheapplication.service;

import com.example.crecheapplication.exception.BadRequestException;
import com.example.crecheapplication.model.Activitebebe;
import com.example.crecheapplication.model.Bebe;
import com.example.crecheapplication.model.Parent;
import com.example.crecheapplication.repository.ActivitesRepository;
import com.example.crecheapplication.repository.BebeRepository;
import com.example.crecheapplication.repository.ParentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService implements InterfaceAdminService{

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
        Bebe bebe = bebeRepository.findById(idBebe).get();
        return bebe.getParent();
    }

    // 4️⃣ activités d’un bébé
    public List<Activitebebe> getActivitesOfBebe(Long idBebe) {
        Bebe bebe = bebeRepository.findById(idBebe)
                .orElseThrow(() -> new BadRequestException("Bébé introuvable"));
        return bebe.getActivites();
    }

    public void deleteParent(Long idParent) {
        Parent parent = parentRepository.findById(idParent)
                .orElseThrow(() ->new BadRequestException(
                        "Parent introuvable")
                );
        parentRepository.delete(parent);
    }   public Parent createParent(String nom, String prenom, String email, String telephone, String password) {
        if (parentRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("Email déjà utilisé");}
        Parent parent = new Parent();
        parent.setNom(nom);
        parent.setPrenom(prenom);
        parent.setEmail(email);
        parent.setTelephone(telephone);
        parent.setPassword(passwordEncoder.encode(password));
        parent.setRole("ROLE_PARENT");
        LocalDateTime now = LocalDateTime.now();
        parent.setCreatedAt(now);
        parent.setModifiedAt(now);
        return parentRepository.save(parent);
    }
    public Bebe ajouterBebe(Long parentId, String nom, String prenom, LocalDate dateNais) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() ->new BadRequestException("Parent introuvable"));

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
    public Parent inscrireadmin(String nom, String prenom, String email, String telephone, String password) {

        if (parentRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("Un compte avec cet email existe déjà !"); }
        Parent parent = new Parent();
        parent.setNom(nom);
        parent.setPrenom(prenom);
        parent.setEmail(email);
        parent.setTelephone(telephone);
        parent.setPassword(passwordEncoder.encode(password));
        parent.setModifiedAt(LocalDateTime.now());
        parent.setCreatedAt(LocalDateTime.now());
        parent.setRole("ROLE_ADMIN");
        return parentRepository.save(parent);
    }
    public Parent updateParent(Long id,String nom, String prenom,String email, String telephone,String password,String role) {
        Parent parent = parentRepository.findById(id).orElseThrow(() ->new BadRequestException ("Parent introuvable"));
        Optional<Parent> parentByEmail = parentRepository.findByEmail(email);
        if (parentByEmail.isPresent() && !parentByEmail.get().getId().equals(id)) {
            throw new BadRequestException("Email déjà utilisé")
            ;}
        if (!"ROLE_ADMIN".equals(role) && !"ROLE_PARENT".equals(role)) {
            throw new BadRequestException("Role invalide, uniquement ROLE_ADMIN ou ROLE_PARENT"
            );
        }
        parent.setNom(nom);
        parent.setPrenom(prenom);
        parent.setEmail(email);
        parent.setTelephone(telephone);
        if (password != null && !password.isEmpty()) {
            parent.setPassword(passwordEncoder.encode(password));
        }
        parent.setRole(role);
        parent.setModifiedAt(LocalDateTime.now());
        return parentRepository.save(parent);
    }
    public Bebe updateBebe(Long id, Bebe bebeDetails) {
        Bebe bebe = bebeRepository.findById(id).orElseThrow(() ->new BadRequestException
                ("bébé introuvable"));
        bebe.setNom(bebeDetails.getNom());
        bebe.setPrenom(bebeDetails.getPrenom());
        bebe.setDateNais(bebeDetails.getDateNais());
        bebe.setModifiedAt(LocalDateTime.now());
        if (bebeDetails.getParent() != null) {
            Parent parent = parentRepository.findById(bebeDetails.getParent().getId())
                    .orElseThrow(() -> new BadRequestException("Parent introuvable"));
            bebe.setParent(parent);
        }
        return bebeRepository.save(bebe);
    }

    public void deleteBebe(Long id) {
        Bebe bebe = bebeRepository.findById(id).orElseThrow(() -> new BadRequestException("bébé introuvable"));
        bebeRepository.delete(bebe);
    }
    public List<Activitebebe> getActivitesAujourdhui(Long idBebe) {
        bebeRepository.findById(idBebe)
                .orElseThrow(() ->new BadRequestException("Bébé introuvable"));

        return activitesRepository.findActivitesDuJourAdmin(
                idBebe,
                LocalDate.now()
        );
    }
    public Activitebebe ajouterActivite(Long idBebe, Activitebebe activiteBebe) {
        Bebe bebe = bebeRepository.findById(idBebe) .orElseThrow(() -> new BadRequestException("Bébé introuvable"
        ));
        activiteBebe.setBebe(bebe);
        activiteBebe.setDate(LocalDate.now());
        activiteBebe.setTemps(LocalTime.now());
        LocalDateTime now = LocalDateTime.now();
        activiteBebe.setCreatedAt(now);
        activiteBebe.setModifiedAt(now);
        return activitesRepository.save(activiteBebe);
    }
    public Activitebebe modifierActivite(Long idActivite, Activitebebe activiteDetails) {

        Activitebebe activite = activitesRepository.findById(idActivite) .orElseThrow(() ->new BadRequestException("Activité introuvable"
        ));
        activite.setTypeActivite(activiteDetails.getTypeActivite());
        activite.setNotes(activiteDetails.getNotes());
        activite.setModifiedAt(LocalDateTime.now());
        return activitesRepository.save(activite);
    }

    public void supprimerActivite(Long idActivite) {
        Activitebebe activite = activitesRepository.findById(idActivite).orElseThrow(() -> new BadRequestException("Activité introuvable"
        ));
        activitesRepository.delete(activite);
    }
    public List<Parent> getAllAdmins() {
        return parentRepository.findByRole("ROLE_ADMIN");
    }
    public void deleteAdmin(Long adminId, String emailFromToken) {

        Parent adminToDelete = parentRepository.findById(adminId)
                .orElseThrow(() ->new BadRequestException("Admin introuvable"
                ));
        if (adminToDelete.getEmail().equals(emailFromToken)) {
            throw new BadRequestException("impossible de supprimer votre propre compte");}

        parentRepository.delete(adminToDelete);
    }
    public List<Activitebebe> ActivitesAujourdhuiAdmin(Long idBebe) {
        bebeRepository.findById(idBebe)
                .orElseThrow(() ->new BadRequestException("Bébé introuvable"
                ));

        return activitesRepository.findActivitesDuJourAdmin(
                idBebe,
                LocalDate.now()
        );
    }
    public Parent getParentById(Long id) {
        return parentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Parent introuvable"
                ));
    }

}


