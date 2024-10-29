package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.auth.AuthResponse;
import com.nextstep.nextstepBackEnd.auth.LoginRequest;
import com.nextstep.nextstepBackEnd.auth.RegisterRequest;
import com.nextstep.nextstepBackEnd.exception.InvalidCredentialsException;
import com.nextstep.nextstepBackEnd.exception.UserAlreadyExistsException;
import com.nextstep.nextstepBackEnd.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Login
    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse authResponse = authService.login(request);
            return ResponseEntity.ok(authResponse);
        } catch (InvalidCredentialsException e) {
            // Retorna un error 401 cuando las credenciales son inválidas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            // Devuelve un error 500 en caso de excepción no controlada
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Registro
    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse authResponse = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse); // Código 201 para creación exitosa
        } catch (UserAlreadyExistsException e) {
            // Retorna un error 409 cuando el usuario ya existe
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (Exception e) {
            // Devuelve un error 500 en caso de excepción no controlada
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /*
    // Obtener el ID del usuario actual
    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> userId = Map.of("userId", authentication.getPrincipal());
        return ResponseEntity.ok(userId);
    }
     */
}
