package com.example.crecheapplication.service;

import com.example.crecheapplication.model.Parent;
import com.example.crecheapplication.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Date;

public class ParentService {


    @Autowired private ParentRepository parentRepository;
    public Parent inscrire(String nom, String prenom, String email, String telephone, String password)
    {
        if (parentRepository.findByNomAndPrenom(nom, prenom).isPresent())
    { throw new RuntimeException("Un compte avec ce nom, prénom existe déjà !");
    } Parent parent = new Parent();
        parent.setNom(nom);
        parent.setPrenom(prenom);
        parent.setEmail(email);
        parent.setTelephone(telephone);
        parent.setPassword(password);
        parent.setModifiedAt(LocalDateTime.now());
        parent.setCreatedAt(LocalDateTime.now());
        return parentRepository.save(parent); }
}