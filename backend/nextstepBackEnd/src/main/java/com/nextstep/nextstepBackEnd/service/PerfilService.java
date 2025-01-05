package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.jwt.JwtService;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public boolean updatePassword(int userId, String newPassword) {
        // Buscar al usuario por ID
        Usuario usuario = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Codificar la nueva contraseña
        usuario.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(usuario);

        // Generar un nuevo token de autenticación, si es necesario
        User userDetails = new User(usuario.getUsername(), usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("normal")));
        jwtService.generateToken(userDetails);

        return true; // Cambiado a booleano, indicando que la actualización fue exitosa
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


    // Metodo para actualizar el username de un usuario
    public void updateUsername(Integer usuarioId, String newUsername) {
        Optional<Usuario> optionalUsuario = userRepository.findById(usuarioId);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            usuario.setUsername(newUsername);
            userRepository.save(usuario);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }

}

