package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.jwt.JwtService;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Obtener el perfil del usuario por ID
    public Usuario getProfile(Integer usuarioId) {
        return userRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
    }

    // Actualizar la contraseña y devolver un nuevo token
    public String updatePassword(Integer usuarioId, String newPassword) {
        Usuario usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Codificar la nueva contraseña y guardarla
        usuario.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(usuario);

        // Generar y devolver un nuevo token JWT para el usuario
        return jwtService.generateToken(usuario);
    }

    // Eliminar la cuenta del usuario
    public void deleteAccount(Integer usuarioId) {
        //System.out.println("Intentando eliminar usuario con ID: " + usuarioId); // Debug
        if (!userRepository.existsById(usuarioId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        userRepository.deleteById(usuarioId);
        //System.out.println("Usuario eliminado correctamente."); // Debug
    }

}

