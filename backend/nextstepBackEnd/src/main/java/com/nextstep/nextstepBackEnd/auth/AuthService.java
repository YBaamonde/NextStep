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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .username(request.getUsername()) // change this line
                .password(request.getPassword())
                .rol(Rol.NORMAL)
                .build();

        userRepository.save(usuario);

        return AuthResponse.builder()
                .token(jwtService.getToken(usuario))
                .build();
    }
}
