package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.Categoria;
import com.nextstep.nextstepBackEnd.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UserRepository userRepository;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        // Crear y guardar un usuario para las pruebas
        usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEmail("testuser@example.com");
        usuario.setPassword("password123");
        usuario = userRepository.save(usuario);
    }

    @Test
    public void testFindByNombre() {
        // Crear y guardar una categoría
        Categoria categoria = new Categoria();
        categoria.setNombre("Alimentación");
        categoria.setUsuario(usuario);
        categoriaRepository.save(categoria);

        // Buscar la categoría por nombre
        Optional<Categoria> result = categoriaRepository.findByNombre("Alimentación");

        assertTrue(result.isPresent());
        assertEquals("Alimentación", result.get().getNombre());
    }

    @Test
    public void testFindByUsuario() {
        // Crear y guardar una categoría asociada al usuario
        Categoria categoria = new Categoria();
        categoria.setNombre("Ocio");
        categoria.setUsuario(usuario);
        categoriaRepository.save(categoria);

        // Recuperar las categorías por usuario
        List<Categoria> categorias = categoriaRepository.findByUsuario(usuario);

        assertNotNull(categorias);
        assertFalse(categorias.isEmpty());
        assertEquals(1, categorias.size());
        assertEquals("Ocio", categorias.get(0).getNombre());
    }
}
