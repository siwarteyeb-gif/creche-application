package com.example.crecheapplication.controller;

import com.example.crecheapplication.model.Activitebebe;
import com.example.crecheapplication.model.Bebe;
import com.example.crecheapplication.model.Parent;
import com.example.crecheapplication.service.ParentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")

public class ParentController {
    private  final ParentService parentService;
    public ParentController( ParentService parentService){
        this.parentService=parentService;
    }
    @PostMapping("/inscrire")
    public Parent inscrire(@RequestBody Parent parent) {
        return parentService.inscrire(
                parent.getNom(),
                parent.getPrenom(),
                parent.getEmail(),
                parent.getTelephone(),
                parent.getPassword()
        );
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = parentService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(token);
    }
    @GetMapping("/bebes")
    public List<Bebe> getBebes(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Parent parent = parentService.getParentFromToken(token); // لازم تعمل method تستخرج Parent من JWT
        return Optional.ofNullable(parent.getBebes())
                .orElse(new ArrayList<>());
    }

    @DeleteMapping("/{id}")
    public void deleteParent(@PathVariable Long id) {
        parentService.supprimerParent(id);
    }
    @PutMapping("/{id}")
    public Parent updateParent(@PathVariable Long id, @RequestBody Parent parent) {
        return parentService.modifierParent(id, parent);
    }
    @GetMapping("/{id}")
    public Parent getParentById(@PathVariable Long id) {
        return parentService.afficherParentParId(id);
    }
    @GetMapping("/parents")
    public List<Parent> getAllParents() {
        return parentService.afficherParent();
    }
    @GetMapping("/bebes/{idBebe}/activites")
    public List<Activitebebe> getActivitesAujourdhui(@PathVariable Long idBebe) {
        return parentService.getActivitesAujourdhui(idBebe);
    }
    @GetMapping("/bebes/{idBebe}/activite-maintenant")
    public Activitebebe getActiviteMaintenant(@PathVariable Long idBebe) {
        return parentService.getActiviteMaintenant(idBebe);
    }


}
