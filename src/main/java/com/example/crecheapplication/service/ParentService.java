package com.example.crecheapplication.service;

import com.example.crecheapplication.model.Activitebebe;
import com.example.crecheapplication.model.Bebe;
import com.example.crecheapplication.model.Parent;
import com.example.crecheapplication.repository.BebeRepository;
import com.example.crecheapplication.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class ParentService {


    @Autowired
    private ParentRepository parentRepository;

    public Parent inscrire(String nom, String prenom, String email, String telephone, String password) {
        if (parentRepository.findByNomAndPrenom(nom, prenom).isPresent()) {
            throw new RuntimeException("Un compte avec ce nom, prénom existe déjà !");
        }
        Parent parent = new Parent();
        parent.setNom(nom);
        parent.setPrenom(prenom);
        parent.setEmail(email);
        parent.setTelephone(telephone);
        parent.setPassword(password);
        parent.setModifiedAt(LocalDateTime.now());
        parent.setCreatedAt(LocalDateTime.now());
        return parentRepository.save(parent);
    }

    public Parent ajouterParent(Parent parent) {
        Optional<Parent> existingParent = parentRepository.findByNomAndPrenom(parent.getNom(), parent.getPrenom());
        if (existingParent.isPresent()) {
            throw new RuntimeException("Parent avec le même nom et prénom existe déjà !");
        }

        parent.setCreatedAt(LocalDateTime.now());
        parent.setModifiedAt(LocalDateTime.now());
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
        parent.setPassword(parentDetails.getPassword());
        parent.setModifiedAt(LocalDateTime.now());

        return parentRepository.save(parent);
    }
    public void supprimerParent(Long id) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parent non trouvé avec l'id: " + id));

        parentRepository.delete(parent);
    }
    public List<Parent> afficherParent() {
        return parentRepository.findAll();
    }
    public Parent afficherParentParId(Long id) {
        return parentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parent avec l'id " + id + " n'existe pas !"));
    }
    public List<Bebe> getBebe(String email, String password) {
        Parent parent = parentRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect !"));
        return parent.getBebes();
    }
    public String login(String email, String password) {
        Parent parent = parentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email incorrect"));

        if(!parent.getPassword().equals(password)) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        return jwtService.generateToken(parent);
    }
    public Activitebebe getActiviteMaintenant(Long idBebe) {

        Bebe bebe = BebeRepository.findById(idBebe).get();

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        return bebe.getActivites().stream()
                .filter(a -> a.getDate().equals(today))
                .filter(a -> !a.getTemps().isAfter(now))
                .max(Comparator.comparing(Activitebebe::getTemps))
                .orElse(null);
    }

}
