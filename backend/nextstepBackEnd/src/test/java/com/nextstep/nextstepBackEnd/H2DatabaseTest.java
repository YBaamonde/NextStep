package com.nextstep.nextstepBackEnd;

import com.nextstep.nextstepBackEnd.repository.UserRepository;
import com.nextstep.nextstepBackEnd.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class H2DatabaseTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void testDatabaseConnection() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setPassword("testpassword");

        Usuario savedUser = userRepository.save(usuario);
        assertNotNull(savedUser.getId(), "User ID should not be null after saving");

        Usuario foundUser = userRepository.findByUsername("testuser").orElse(null);
        assertNotNull(foundUser, "User should be found by username");
        assertTrue("testuser".equals(foundUser.getUsername()), "Username should match");
    }
}


