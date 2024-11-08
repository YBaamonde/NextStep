package com.nextstep.nextstepBackEnd.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debug")
public class DebugController {

    private final BCryptPasswordEncoder passwordEncoder;

    public DebugController(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/test-password")
    public ResponseEntity<String> testPassword() {
        String rawPassword = "nuevaContraseña123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

        return ResponseEntity.ok("Encoded password: " + encodedPassword + " | Matches: " + matches);
    }
}

