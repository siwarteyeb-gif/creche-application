package com.example.crecheapplication.controller;

import com.example.crecheapplication.model.Activitebebe;
import com.example.crecheapplication.model.Bebe;
import com.example.crecheapplication.model.Parent;
import com.example.crecheapplication.service.ParentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
        return parentService.getBebesFromToken(token);
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
    public List<Activitebebe> getActivitesAujourdhui(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long idBebe) {

        String token = authHeader.replace("Bearer ", "");
        return parentService.getActivitesAujourdhui(token, idBebe);
    }

    @GetMapping("/bebes/{idBebe}/activite-maintenant")
    public Activitebebe getActiviteMaintenant(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long idBebe) {

        String token = authHeader.replace("Bearer ", "");
        return parentService.getActiviteMaintenan(token, idBebe);
    }


}
