package com.example.crecheapplication.service;

import com.example.crecheapplication.model.Activitebebe;
import com.example.crecheapplication.model.Bebe;
import com.example.crecheapplication.model.Parent;
import com.example.crecheapplication.repository.ActivitesRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private ParentRepository parentRepository;

    @Mock
    private BebeRepository bebeRepository;

    @Mock
    private ActivitesRepository activitesRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    private Parent parent;
    private Bebe bebe;
    private Activitebebe activite;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Parent
        parent = new Parent();
        parent.setId(1L);
        parent.setNom("Ali");
        parent.setEmail("ali@test.com");
        parent.setRole("ROLE_PARENT");

        // Bebe
        bebe = new Bebe();
        bebe.setId(1L);
        bebe.setParent(parent);

        // Activité
        activite = new Activitebebe();
        activite.setId(1L);
        activite.setBebe(bebe);
    }

    @Test
    void testGetAllParents() {
        when(parentRepository.findByRole("ROLE_PARENT")).thenReturn(Collections.singletonList(parent));
        List<Parent> result = adminService.getAllParents();
        assertEquals(1, result.size());
        assertEquals("Ali", result.get(0).getNom());
    }

    @Test
    void testAjouterBebe() {
        when(parentRepository.findById(1L)).thenReturn(Optional.of(parent));
        when(bebeRepository.save(any(Bebe.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bebe result = adminService.ajouterBebe(1L, "Bebe1", "Prenom1", LocalDate.now());

        assertEquals("Bebe1", result.getNom());
        assertEquals(parent, result.getParent());
    }

    @Test
    void testAjouterActivite() {
        when(bebeRepository.findById(1L)).thenReturn(Optional.of(bebe));
        when(activitesRepository.save(any(Activitebebe.class))).thenReturn(activite);

        Activitebebe result = adminService.ajouterActivite(1L, new Activitebebe());
        assertEquals(bebe, result.getBebe());
        assertEquals(1L, result.getId());
    }

    @Test
    void testDeleteParent() {
        when(parentRepository.findById(1L)).thenReturn(Optional.of(parent));
        doNothing().when(parentRepository).delete(parent);

        assertDoesNotThrow(() -> adminService.deleteParent(1L));
        verify(parentRepository, times(1)).delete(parent);
    }

    @Test
    void testSupprimerActivite() {
        when(activitesRepository.findById(1L)).thenReturn(Optional.of(activite));
        doNothing().when(activitesRepository).delete(activite);

        assertDoesNotThrow(() -> adminService.supprimerActivite(1L));
        verify(activitesRepository, times(1)).delete(activite);
    }

    @Test
    void testDeleteParentNotFound() {
        when(parentRepository.findById(2L)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                adminService.deleteParent(2L)
        );
        assertTrue(exception.getMessage().contains("Parent introuvable"));
    }

    @Test
    void testSupprimerActiviteNotFound() {
        when(activitesRepository.findById(2L)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                adminService.supprimerActivite(2L)
        );
        assertTrue(exception.getMessage().contains("Activité introuvable"));
    }
}