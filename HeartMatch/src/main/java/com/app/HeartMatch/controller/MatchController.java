package com.app.HeartMatch.controller;

import com.app.HeartMatch.model.*;
import com.app.HeartMatch.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/{userId}")
    public List<User> getMatches(@PathVariable Long userId) {
        return matchService.findMatches(userId);
    }
}