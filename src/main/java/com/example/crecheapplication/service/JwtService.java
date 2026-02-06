package com.example.crecheapplication.service;

import com.example.crecheapplication.model.Parent;
import com.example.crecheapplication.repository.ParentRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.Date;
@Service
public class JwtService {

    private static final String SECRET_KEY = "my_super_secret_key_123456789_my_super_secret_key";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;
    private final ParentRepository parentRepository;
    public JwtService(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(Parent parent) {
        return Jwts.builder()
                .setSubject(parent.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public Parent getParentFromToken(String token) {
        String email = extractEmail(token);
        return parentRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Parent introuvable depuis token"
                ));
    }

}
