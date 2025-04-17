package com.app.HeartMatch.service;

import com.app.HeartMatch.model.*;
import com.app.HeartMatch.repository.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    private final UserRepository userRepository;
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(String email, String rawPassword) {
        User user = userRepository.findByEmail(email).orElseThrow();
        if (!new UserService(userRepository).isValidPassword(user, rawPassword)) {
            throw new RuntimeException("Invalid credentials");
        }
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }
}