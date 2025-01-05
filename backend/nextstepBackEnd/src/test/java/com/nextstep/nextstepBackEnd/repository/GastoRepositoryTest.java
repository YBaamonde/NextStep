package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.Categoria;
import com.nextstep.nextstepBackEnd.model.Gasto;
import com.nextstep.nextstepBackEnd.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class GastoRepositoryTest {

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UserRepository userRepository;

    private Usuario usuario;
    private Categoria categoria;

    @BeforeEach
    public void setUp() {
        // Crear y guardar un usuario para las pruebas
        usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEmail("testuser@example.com");
        usuario.setPassword("password123");
        usuario = userRepository.save(usuario);

        // Crear y guardar una categoría para las pruebas
        categoria = new Categoria();
        categoria.setNombre("Transporte");
        categoria.setUsuario(usuario);
        categoria = categoriaRepository.save(categoria);
    }

    @Test
    public void testFindByUsuario() {
        // Crear y guardar un gasto asociado al usuario
        Gasto gasto = new Gasto();
        gasto.setUsuario(usuario);
        gasto.setNombre("Bus");
        gasto.setMonto(BigDecimal.valueOf(2.50));
        gasto.setFecha(LocalDate.now());
        gasto.setCategoria(categoria);
        gastoRepository.save(gasto);

        // Recuperar los gastos por usuario
        List<Gasto> gastos = gastoRepository.findByUsuario(usuario);

        assertNotNull(gastos);
        assertFalse(gastos.isEmpty());
        assertEquals(1, gastos.size());
        assertEquals("Bus", gastos.get(0).getNombre());
    }

    @Test
    public void testFindByCategoria() {
        // Crear y guardar un gasto asociado a la categoría
        Gasto gasto = new Gasto();
        gasto.setUsuario(usuario);
        gasto.setNombre("Taxi");
        gasto.setMonto(BigDecimal.valueOf(10.00));
        gasto.setFecha(LocalDate.now());
        gasto.setCategoria(categoria);
        gastoRepository.save(gasto);

        // Recuperar los gastos por categoría
        List<Gasto> gastos = gastoRepository.findByCategoria(categoria);

        assertNotNull(gastos);
        assertFalse(gastos.isEmpty());
        assertEquals(1, gastos.size());
        assertEquals("Taxi", gastos.get(0).getNombre());
    }

    @Test
    public void testFindByUsuarioId() {
        // Crear y guardar un gasto asociado al usuario
        Gasto gasto = new Gasto();
        gasto.setUsuario(usuario);
        gasto.setNombre("Bus");
        gasto.setMonto(BigDecimal.valueOf(2.50));
        gasto.setFecha(LocalDate.now());
        gasto.setCategoria(categoria);
        gastoRepository.save(gasto);

        // Recuperar los gastos por usuario ID
        List<Gasto> gastos = gastoRepository.findByUsuarioId(usuario.getId());

        assertNotNull(gastos);
        assertFalse(gastos.isEmpty());
        assertEquals(1, gastos.size());
        assertEquals("Bus", gastos.get(0).getNombre());
    }

    @Test
    public void testFindByCategoriaId() {
        // Crear y guardar un gasto asociado a la categoría
        Gasto gasto = new Gasto();
        gasto.setUsuario(usuario);
        gasto.setNombre("Taxi");
        gasto.setMonto(BigDecimal.valueOf(10.00));
        gasto.setFecha(LocalDate.now());
        gasto.setCategoria(categoria);
        gastoRepository.save(gasto);

        // Recuperar los gastos por categoría ID
        List<Gasto> gastos = gastoRepository.findByCategoriaId(categoria.getId());

        assertNotNull(gastos);
        assertFalse(gastos.isEmpty());
        assertEquals(1, gastos.size());
        assertEquals("Taxi", gastos.get(0).getNombre());
    }
}
