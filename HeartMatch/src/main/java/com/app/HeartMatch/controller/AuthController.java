package com.app.HeartMatch.controller;

import com.app.HeartMatch.dto.*;
import com.app.HeartMatch.dto.*;
import com.app.HeartMatch.model.*;
import com.app.HeartMatch.service.*;
import com.app.HeartMatch.service.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return new AuthResponse(jwtService.generateToken(request.getEmail(), request.getPassword()));
    }
}