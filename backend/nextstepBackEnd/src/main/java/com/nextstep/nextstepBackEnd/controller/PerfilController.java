package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.service.PerfilService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/perfil")
public class PerfilController {

    private final PerfilService perfilService;

    // Constructor
    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
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
        String newToken = perfilService.updatePassword(usuarioId, newPassword);

        // Retornar el nuevo token en la respuesta
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password updated successfully.");
        response.put("token", newToken); // Incluye el nuevo token en la respuesta
        return ResponseEntity.ok(response);
    }


    // Eliminar la cuenta del usuario
    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Integer usuarioId) {
        perfilService.deleteAccount(usuarioId);
        return ResponseEntity.ok("Account deleted successfully.");
    }

}

