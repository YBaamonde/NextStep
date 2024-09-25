package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Usuario, Long> {

    // Método para buscar un usuario por su nombre de usuario (correo)
    Optional<Usuario>findByUsername(String correo);

}