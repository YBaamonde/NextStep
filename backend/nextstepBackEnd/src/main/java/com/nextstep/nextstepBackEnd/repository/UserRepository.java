package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    // Nuevo metodo para buscar por nombre de usuario o email
    Optional<Usuario> findByUsernameOrEmail(String username, String email);
}
