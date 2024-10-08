package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Usuario, Long> {

    // Metodo para buscar un usuario por su nombre de usuario
    Optional<Usuario>findByUsername(String username);
    // Metodo para buscar un usuario por su email
    Optional<Usuario>findByEmail(String email);
}