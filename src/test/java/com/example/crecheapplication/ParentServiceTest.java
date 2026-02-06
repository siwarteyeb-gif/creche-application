package com.example.crecheapplication.service;
import com.example.crecheapplication.model.Bebe;
import com.example.crecheapplication.model.Parent;
import com.example.crecheapplication.repository.BebeRepository;
import com.example.crecheapplication.repository.ParentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
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
        parent = new Parent();
        parent.setId(1L);
        parent.setNom("Ali");
        parent.setEmail("ali@test.com");
        parent.setRole("ROLE_PARENT");

        Bebe bebe = new Bebe();
        bebe.setId(1L);
        bebe.setParent(parent);}

    @Test
    void testInscrire() {
        when(parentRepository.findByEmail("ali@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(parentRepository.save(any(Parent.class))).thenAnswer(i -> i.getArguments()[0]);
        Parent result = parentService.inscrire("Ali", "Prenom", "ali@test.com", "12345678", "password");
        assertEquals("Ali", result.getNom());
        assertEquals("Prenom", result.getPrenom());
        assertEquals("ali@test.com", result.getEmail());
        assertEquals("12345678", result.getTelephone());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals("ROLE_PARENT", result.getRole());
    }

    @Test
    void testInscrireEmailExists() {
        when(parentRepository.findByEmail("ali@test.com")).thenReturn(Optional.of(parent));

        Exception exception = assertThrows(RuntimeException.class, () ->
                parentService.inscrire("Ali", "Prenom", "ali@test.com", "12345678", "password")
        );

        assertTrue(exception.getMessage().contains("Un compte avec cet email existe déjà"));
    }

    @Test
    void testLoginSuccess() {
        parent.setPassword("encodedPassword");
        when(parentRepository.findByEmail("ali@test.com")).thenReturn(Optional.of(parent));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtService.generateToken(parent)).thenReturn("token123");

        Map<String, String> result = parentService.login("ali@test.com", "password");

        assertEquals("token123", result.get("token"));
        assertEquals("ROLE_PARENT", result.get("role"));
    }

    @Test
    void testLoginFail() {
        when(parentRepository.findByEmail("ali@test.com")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                parentService.login("ali@test.com", "password")
        );
        assertEquals(401, exception.getStatusCode().value());
    }

    @Test
    void testModifierParent() {
        Parent details = new Parent();
        details.setNom("Ali2");
        details.setPrenom("Prenom2");
        details.setEmail("ali2@test.com");
        details.setTelephone("87654321");
        details.setPassword("newPass");
        when(jwtService.getParentFromToken("token123")).thenReturn(parent);
        when(parentRepository.findByEmail("ali2@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");
        when(parentRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        Parent result = parentService.modifierParent("token123", details);
        assertEquals("Ali2", result.getNom());
        assertEquals("Prenom2", result.getPrenom());
        assertEquals("ali2@test.com", result.getEmail());
        assertEquals("87654321", result.getTelephone());
        assertEquals("encodedNewPass", result.getPassword());
    }
}