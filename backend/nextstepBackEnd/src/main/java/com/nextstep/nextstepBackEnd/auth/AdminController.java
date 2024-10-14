package com.nextstep.nextstepBackEnd.auth;

import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('admin')")  // Solo accesible para administradores
public class AdminController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @Autowired
    public AdminController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    // Endpoint para que los administradores puedan crear usuarios con un rol específico
    @PostMapping("/create-user")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> createUser(@RequestBody AdminRegisterRequest adminRegisterRequest) {
        authService.registerAdmin(adminRegisterRequest);
        return ResponseEntity.ok("Usuario creado exitosamente");
    }

    // Listar todos los usuarios
    @GetMapping("/users")
    public ResponseEntity<List<Usuario>> getAllUsers() {
        List<Usuario> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // Eliminar un usuario
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok("Usuario eliminado exitosamente");
    }
}


