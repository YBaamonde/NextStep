package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.jwt.JwtService;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import com.nextstep.nextstepBackEnd.service.PerfilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/perfil")
public class PerfilController {

    private final PerfilService perfilService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    // Constructor con inyección de dependencias para perfilService, userRepository, y jwtService
    public PerfilController(PerfilService perfilService, UserRepository userRepository, JwtService jwtService) {
        this.perfilService = perfilService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    // Obtener el perfil del usuario autenticado
    @GetMapping("/{usuarioId}")
    public ResponseEntity<Usuario> getProfile(@PathVariable Integer usuarioId) {
        Usuario usuario = perfilService.getProfile(usuarioId);
        return ResponseEntity.ok(usuario);
    }

    // Actualizar la contraseña del usuario
    @PostMapping("/{usuarioId}/password")
    public ResponseEntity<Map<String, String>> updatePassword(@PathVariable Integer usuarioId,
                                                              @RequestBody Map<String, String> request) {
        String newPassword = request.get("newPassword");

        // Llamar a updatePassword en el servicio y recibir el booleano indicando éxito
        boolean isUpdated = perfilService.updatePassword(usuarioId, newPassword);

        // Crear la respuesta de éxito
        Map<String, String> response = new HashMap<>();
        if (isUpdated) {
            // Generar un nuevo token si la actualización fue exitosa
            Usuario usuario = userRepository.findById(usuarioId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

            // Generar UserDetails
            User userDetails = new User(usuario.getUsername(), usuario.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

            String newToken = jwtService.generateToken(userDetails);
            response.put("message", "Password updated successfully.");
            response.put("token", newToken); // Incluye el nuevo token en la respuesta
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to update password.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Eliminar la cuenta del usuario
    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Integer usuarioId) {
        perfilService.deleteAccount(usuarioId);
        return ResponseEntity.ok("Account deleted successfully.");
    }

    // Endpoint para actualizar el nombre de usuario
    @PutMapping("/{usuarioId}/username")
    public ResponseEntity<Map<String, String>> updateUsername(@PathVariable Integer usuarioId,
                                                              @RequestBody Map<String, String> request) {
        String newUsername = request.get("newUsername");
        perfilService.updateUsername(usuarioId, newUsername);

        // Generar nuevo token con el nuevo nombre de usuario
        Usuario usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Crear UserDetails con el nuevo username
        UserDetails userDetails = new User(usuario.getUsername(), usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        // Generar nuevo token
        String newToken = jwtService.generateToken(userDetails);

        // Responder con un mensaje de éxito y el nuevo token
        Map<String, String> response = new HashMap<>();
        response.put("message", "Nombre de usuario actualizado correctamente");
        response.put("token", newToken);  // Devuelve el nuevo token

        return ResponseEntity.ok(response);
    }

}
