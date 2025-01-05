package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.auth.AdminRegisterRequest;
import com.nextstep.nextstepBackEnd.auth.AuthResponse;
import com.nextstep.nextstepBackEnd.auth.LoginRequest;
import com.nextstep.nextstepBackEnd.auth.RegisterRequest;
import com.nextstep.nextstepBackEnd.exception.InvalidCredentialsException;
import com.nextstep.nextstepBackEnd.jwt.JwtService;
import com.nextstep.nextstepBackEnd.model.Rol;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final JwtService jwtService;  // Asegúrate de tener esta referencia al servicio de JWT
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        // Busca al usuario por nombre de usuario o email en el repositorio
        Usuario usuario = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        // Determina si se ingresó un email o un nombre de usuario
        String loginIdentifier = request.getUsername();
        if (loginIdentifier.contains("@")) {
            loginIdentifier = usuario.getUsername(); // Si es un email, usa el nombre de usuario para la autenticación
        }


        // Autentica al usuario usando las credenciales proporcionadas
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginIdentifier, request.getPassword())
        );

        // Genera el token JWT después de la autenticación
        String token = jwtService.generateToken(usuario);

        // Obtiene el ID del usuario directamente desde la entidad User
        Integer userId = usuario.getId();

        // Devuelve la respuesta con el token y el userId
        return AuthResponse.builder()
                .token(token)
                .userId(userId) // Agrega el userId a la respuesta
                .build();
    }





    @Transactional
    public AuthResponse register(RegisterRequest request) {
        logger.info("Registrando usuario: {}", request.getUsername());

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya existe");
        } else if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya existe");
        }

        // Crear usuario con rol "normal", ya que solo un admin puede crear admins
        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))  // Encripta la contraseña
                .rol(Rol.normal)  // Todos los usuarios que se registren tendrán el rol 'normal'
                .build();

        userRepository.save(usuario);
        return AuthResponse.builder()
                .token(jwtService.generateToken(usuario))
                .build();
    }

    // Metodo que usarán los admins para registrar usuarios
    @Transactional
    public AuthResponse registerAdmin(AdminRegisterRequest request) {
        logger.info("Admin registrando usuario: {}", request.getUsername());

        // Validar si el rol es válido
        Rol rol;
        try {
            rol = Rol.valueOf(request.getRol());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol inválido: " + request.getRol());
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya existe");
        } else if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya existe");
        }

        // Crear usuario con el rol proporcionado
        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))  // Encripta la contraseña
                .rol(rol)  // El administrador elige el rol
                .build();

        userRepository.save(usuario);
        return AuthResponse.builder()
                .token(jwtService.generateToken(usuario))
                .build();
    }


}
