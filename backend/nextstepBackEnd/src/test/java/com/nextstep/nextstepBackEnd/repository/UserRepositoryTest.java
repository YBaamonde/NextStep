package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.Rol;
import com.nextstep.nextstepBackEnd.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {

        Usuario user = new Usuario();
        user.setUsername("testUser");
        user.setEmail("email@simulado.com");
        user.setPassword("password");
        user.setRol(Rol.normal);
        userRepository.save(user);

        Optional<Usuario> foundUser = userRepository.findByUsername("testUser");

        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
    }

    @Test
    public void testFindByEmail() {

        Usuario user = new Usuario();
        user.setUsername("testUser");
        user.setEmail("email@simulado.com");
        user.setPassword("password");
        user.setRol(Rol.normal);
        userRepository.save(user);

        Optional<Usuario> foundUser = userRepository.findByEmail("email@simulado.com");

        assertTrue(foundUser.isPresent());
        assertEquals("email@simulado.com", foundUser.get().getEmail());
    }
}
