package com.app.CabBooking.controller;

import com.app.CabBooking.model.CabDriver;
import com.app.CabBooking.model.User;
import com.app.CabBooking.repository.CabDriverRepository;
import com.app.CabBooking.repository.UserRepository;
import com.app.CabBooking.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private CabDriverRepository driverRepository;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/register/user")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/register/driver")
    public ResponseEntity<String> registerDriver(@RequestBody CabDriver driver) {
        driver.setPassword(passwordEncoder.encode(driver.getPassword()));
        driverRepository.save(driver);
        return ResponseEntity.ok("Driver registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        return userRepository.findByEmail(username)
            .filter(user -> passwordEncoder.matches(password, user.getPassword()))
            .map(user -> ResponseEntity.ok(jwtService.generateToken(username)))
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"));
    }
}