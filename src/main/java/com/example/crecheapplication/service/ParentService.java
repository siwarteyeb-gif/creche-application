package com.example.crecheapplication.service;

import com.example.crecheapplication.exception.BadRequestException;
import com.example.crecheapplication.model.Activitebebe;
import com.example.crecheapplication.model.Bebe;
import com.example.crecheapplication.model.Parent;
import com.example.crecheapplication.repository.ActivitesRepository;
import com.example.crecheapplication.repository.BebeRepository;
import com.example.crecheapplication.repository.ParentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ParentService implements InterfaceParentService{
    private final ParentRepository parentRepository;
    private final BebeRepository bebeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ActivitesRepository activitesRepository;
    public ParentService(ParentRepository parentRepository,
                         BebeRepository bebeRepository,
                         PasswordEncoder passwordEncoder,
                         JwtService jwtService,ActivitesRepository activitesRepository) {
        this.parentRepository = parentRepository;
        this.bebeRepository = bebeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.activitesRepository=activitesRepository;
    }

    public Parent inscrire(String nom, String prenom, String email, String telephone, String password) {

        if (parentRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("Un compte avec cet email existe déjà !");
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


    public Parent modifierParent(String token, Parent parentDetails) {
        Parent parent = jwtService.getParentFromToken(token);
        Optional<Parent> verifEmail = parentRepository.findByEmail(parentDetails.getEmail());
        if (verifEmail.isPresent() && !verifEmail.get().getId().equals(parent.getId())) {
            throw new BadRequestException("Un autre parent avec le même email existe déjà !)"
            );
        }
        parent.setNom(parentDetails.getNom());
        parent.setPrenom(parentDetails.getPrenom());
        parent.setEmail(parentDetails.getEmail());
        parent.setTelephone(parentDetails.getTelephone());
        if (parentDetails.getPassword() != null && !parentDetails.getPassword().isEmpty()) {
            parent.setPassword(passwordEncoder.encode(parentDetails.getPassword()));
        }        parent.setModifiedAt(LocalDateTime.now());
        return parentRepository.save(parent);
    }

    public Map<String, String> login(String email, String password) {
        Parent parent = parentRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(password, parent.getPassword())) {
            throw new BadRequestException("Email ou mot de passe incorrect");
        }

        String token = jwtService.generateToken(parent);

        return Map.of(
                "token", token,
                "role", parent.getRole()
        );
    }
    public Activitebebe getLastActivite(String token, Long idBebe) {
        Parent parent = jwtService.getParentFromToken(token);

        Pageable topOne = PageRequest.of(0, 1);
        List<Activitebebe> activites =
                activitesRepository.findLastActivite(idBebe, parent.getId(), topOne);

        return activites.isEmpty() ? null : activites.get(0);
    }
    public List<Activitebebe> getActivitesAujourdhui(String token, Long idBebe) {
        Parent parent = jwtService.getParentFromToken(token);
        LocalDate today = LocalDate.now();

        return activitesRepository.findActivitesDuJour(
                idBebe,
                parent.getId(),
                today
        );
    }

    public List<Bebe> getBebes(String token) {
        Parent parent = jwtService.getParentFromToken(token);
        return Optional.ofNullable(parent.getBebes())
                .orElse(new ArrayList<>());
    }
    public Parent afficherParentParEmail(String email) {
        return parentRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Parent avec email " + email + " introuvable"));
    }

}
