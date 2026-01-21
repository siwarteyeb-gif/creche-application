package com.example.crecheapplication.service;

import com.example.crecheapplication.model.Activitebebe;
import com.example.crecheapplication.model.Bebe;
import com.example.crecheapplication.model.Parent;
import com.example.crecheapplication.repository.BebeRepository;
import com.example.crecheapplication.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ParentService {

    private final ParentRepository parentRepository;
    private final BebeRepository bebeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ParentService(ParentRepository parentRepository,
                         BebeRepository bebeRepository,
                         PasswordEncoder passwordEncoder,
                         JwtService jwtService) {
        this.parentRepository = parentRepository;
        this.bebeRepository = bebeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Parent inscrire(String nom, String prenom, String email, String telephone, String password) {

        if (parentRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Un compte avec cet email existe déjà !");
        }
        Parent parent = new Parent();
        parent.setNom(nom);
        parent.setPrenom(prenom);
        parent.setEmail(email);
        parent.setTelephone(telephone);
        parent.setPassword(passwordEncoder.encode(password));
        parent.setModifiedAt(LocalDateTime.now());
        parent.setCreatedAt(LocalDateTime.now());
        parent.setRole("ROLE_PARENT");
        return parentRepository.save(parent);
    }


    public Parent modifierParent(Long id, Parent parentDetails) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parent non trouvé avec l'id: " + id));

        Optional<Parent> verifNomPren = parentRepository.findByNomAndPrenom(parentDetails.getNom(), parentDetails.getPrenom());
        if (verifNomPren.isPresent() && !verifNomPren.get().getId().equals(id)) {
            throw new RuntimeException("Un autre parent avec le même nom et prénom existe déjà !");
        }

        parent.setNom(parentDetails.getNom());
        parent.setPrenom(parentDetails.getPrenom());
        parent.setEmail(parentDetails.getEmail());
        parent.setTelephone(parentDetails.getTelephone());
        parent.setPassword(passwordEncoder.encode(parentDetails.getPassword()));        parent.setModifiedAt(LocalDateTime.now());

        return parentRepository.save(parent);
    }
    public Parent afficherParentParId(Long id) {
        return parentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parent avec l'id " + id + " n'existe pas !"));
    }

    public String login(String email, String password) {
        Parent parent = parentRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email incorrect"));

        if (!passwordEncoder.matches(password, parent.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Mot de passe incorrect");
        }

        return jwtService.generateToken(parent);
    }
    public Activitebebe getActiviteMaintenant(String token, Long idBebe) {
        Parent parent = getParentFromToken(token);
        Bebe bebe = bebeRepository.findById(idBebe)
                .orElseThrow(() -> new RuntimeException("Bebe introuvable"));
        if (!bebe.getParent().getId().equals(parent.getId())) {
            throw new RuntimeException("Accès refusé : bébé non autorisé");
        }
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        return Optional.ofNullable(bebe.getActivites())
                .orElse(new ArrayList<>())
                .stream()
                .filter(a -> a.getDate().equals(today))
                .filter(a -> !a.getTemps().isAfter(now))
                .max(Comparator.comparing(Activitebebe::getTemps))
                .orElse(null);
    }
    public List<Activitebebe> getActivitesAujourdhui(String token, Long idBebe) {
        Parent parent = getParentFromToken(token);
        Bebe bebe = bebeRepository.findById(idBebe)
                .orElseThrow(() -> new RuntimeException("Bebe introuvable"));
        if (!bebe.getParent().getId().equals(parent.getId())) {
            throw new RuntimeException("Accès refusé : bébé non autorisé");
        }
        LocalDate today = LocalDate.now();
        return Optional.ofNullable(bebe.getActivites())
                .orElse(new ArrayList<>())
                .stream()
                .filter(a -> a.getDate().equals(today))
                .sorted(Comparator.comparing(Activitebebe::getTemps))
                .toList();
    }
    public Parent getParentFromToken(String token) {
        String email = jwtService.extractEmail(token);
        return parentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Parent introuvable depuis token"));
    }
    public List<Bebe> getBebesFromToken(String token) {
        Parent parent = getParentFromToken(token);
        return Optional.ofNullable(parent.getBebes())
                .orElse(new ArrayList<>());
    }
    public Parent afficherParentParEmail(String email) {
        return parentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Parent avec email " + email + " introuvable"));
    }

}
