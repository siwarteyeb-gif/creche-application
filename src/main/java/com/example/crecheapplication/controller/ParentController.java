package com.example.crecheapplication.controller;

import com.example.crecheapplication.model.Activitebebe;
import com.example.crecheapplication.model.Bebe;
import com.example.crecheapplication.model.Parent;
import com.example.crecheapplication.service.ParentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
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
    @PutMapping("/modifier")
    public Parent modifierParent(@RequestHeader("Authorization") String authHeader,@RequestBody Parent parentDetails) {
        String token = authHeader.replace("Bearer ", "");
        return parentService.modifierParent(token, parentDetails);}

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        Map<String, String> result = parentService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(result);
    }
    @GetMapping("/bebes")
    public List<Bebe> getBebes(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return parentService.getBebes(token);
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
        return parentService.getActiviteMaintenant(token, idBebe);
    }


}
