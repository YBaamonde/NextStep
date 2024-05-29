package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario>findByCorreo(String correo);

}