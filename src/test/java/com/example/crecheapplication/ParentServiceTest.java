package com.example.crecheapplication.service;

import com.example.crecheapplication.model.Activitebebe;
import com.example.crecheapplication.model.Bebe;
import com.example.crecheapplication.model.Parent;
import com.example.crecheapplication.repository.BebeRepository;
import com.example.crecheapplication.repository.ParentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ParentServiceTest {

    @Mock
    private ParentRepository parentRepository;

    @Mock
    private BebeRepository bebeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private ParentService parentService;

    private Parent parent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        parent = new Parent();
        parent.setId(1L);
        parent.setNom("Ali");
        parent.setPrenom("Ben Ali");
        parent.setEmail("ali@test.com");
        parent.setTelephone("12345678");
        parent.setPassword("hashedpassword");
        parent.setCreatedAt(LocalDateTime.now());
        parent.setModifiedAt(LocalDateTime.now());
        parent.setRole("ROLE_PARENT");
    }

    @Test
    void testInscrireSuccess() {
        when(parentRepository.findByEmail(parent.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("hashedpassword");
        when(parentRepository.save(any(Parent.class))).thenReturn(parent);

        Parent result = parentService.inscrire("Ali", "Ben Ali", "ali@test.com", "12345678", "password");

        assertNotNull(result);
        assertEquals("Ali", result.getNom());
        assertEquals("ali@test.com", result.getEmail());
    }

    @Test
    void testInscrireEmailExist() {
        when(parentRepository.findByEmail(parent.getEmail())).thenReturn(Optional.of(parent));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            parentService.inscrire("Ali", "Ben Ali", "ali@test.com", "12345678", "password");
        });

        assertEquals("Un compte avec cet email existe déjà !", exception.getMessage());
    }

    @Test
    void testModifierParentSuccess() {
        Parent updatedParent = new Parent();
        updatedParent.setNom("Ali2");
        updatedParent.setPrenom("Ben Ali2");
        updatedParent.setEmail("ali2@test.com");
        updatedParent.setTelephone("87654321");
        updatedParent.setPassword("newpassword");

        when(parentRepository.findById(1L)).thenReturn(Optional.of(parent));
        when(parentRepository.findByNomAndPrenom("Ali2", "Ben Ali2")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("newpassword")).thenReturn("hashedpassword2");
        when(parentRepository.save(any(Parent.class))).thenReturn(parent);

        Parent result = parentService.modifierParent(1L, updatedParent);

        assertNotNull(result);
        assertEquals("Ali2", result.getNom());
    }

    @Test
    void testAfficherParentParIdSuccess() {
        when(parentRepository.findById(1L)).thenReturn(Optional.of(parent));

        Parent result = parentService.afficherParentParId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testAfficherParentParIdNotFound() {
        when(parentRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            parentService.afficherParentParId(2L);
        });

        assertEquals("Parent avec l'id 2 n'existe pas !", exception.getMessage());
    }

    @Test
    void testGetBebesFromToken() {
        Bebe bebe = new Bebe();
        bebe.setId(1L);
        List<Bebe> bebeList = Collections.singletonList(bebe);
        parent.setBebes(bebeList);

        when(jwtService.extractEmail("token")).thenReturn("ali@test.com");
        when(parentRepository.findByEmail("ali@test.com")).thenReturn(Optional.of(parent));

        List<Bebe> result = parentService.getBebesFromToken("token");

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

}
