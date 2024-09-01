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
        // Verifica si el usuario existe en la base de datos antes de intentar autenticar
        UserDetails user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // Autentica al usuario usando las credenciales proporcionadas
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Genera el token JWT
        String token = jwtService.getToken(user);

        // Devuelve la respuesta con el token
        return AuthResponse.builder()
                .token(token)
                .build();
    }


    @Transactional
    public AuthResponse register(RegisterRequest request) {
        logger.info("Registering user: {}", request.getUsername());

        // Verifica si el nombre de usuario ya existe
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            logger.warn("Username already taken: {}", request.getUsername());
            throw new RuntimeException("Username already taken");
        }

        // Crea el usuario si el nombre de usuario es único
        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())) // Cifra la contraseña antes de guardarla
                .rol(Rol.valueOf(request.getRol()))
                .build();

        userRepository.save(usuario);
        logger.info("User registered: {}", usuario.getUsername());

        return AuthResponse.builder()
                .token(jwtService.getToken(usuario))
                .build();
    }

}
