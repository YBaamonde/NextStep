package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Usuario usuario) {
        if (userRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("El correo ya está en uso");
        }
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        userRepository.save(usuario);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    @GetMapping("/login")
    public ResponseEntity<?> login() {
        return ResponseEntity.ok("Login successful");
    }
}
