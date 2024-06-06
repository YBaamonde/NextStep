package com.nextstep.nextstepBackEnd.auth;

import com.nextstep.nextstepBackEnd.jwt.JwtService;
import com.nextstep.nextstepBackEnd.model.Rol;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder; // Inyecta el PasswordEncoder

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        logger.info("Registering user: {}", request.getUsername());

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())) // Cifra la contraseña antes de guardarla
                .rol(Rol.valueOf(request.getRol().toUpperCase()))
                .build();

        userRepository.save(usuario);
        logger.info("User registered: {}", usuario.getUsername());

        return AuthResponse.builder()
                .token(jwtService.getToken(usuario))
                .build();
    }
}
