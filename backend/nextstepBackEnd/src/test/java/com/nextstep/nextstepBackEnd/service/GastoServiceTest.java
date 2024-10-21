package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Categoria;
import com.nextstep.nextstepBackEnd.model.Gasto;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.CategoriaRepository;
import com.nextstep.nextstepBackEnd.repository.GastoRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class GastoServiceTest {

    @Mock
    private GastoRepository gastoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GastoService gastoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateGasto() {
        // Crear un mock de Usuario
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("testuser");

        // Crear un mock de Categoria
        Categoria categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Alimentación");

        // Crear una instancia de Gasto
        Gasto newGasto = new Gasto();
        newGasto.setNombre("Alquiler");
        newGasto.setMonto(BigDecimal.valueOf(500));
        newGasto.setFecha(LocalDate.now());

        // Simular el comportamiento de userRepository y categoriaRepository
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));

        // Simular el comportamiento del gastoRepository
        when(gastoRepository.save(any(Gasto.class))).thenAnswer(invocation -> {
            Gasto gasto = invocation.getArgument(0);
            gasto.setId(1); // Asignar un ID simulado al gasto
            return gasto;
        });

        // Ejecutar el metodo createGasto con el ID de usuario, ID de categoría y el objeto gasto
        Gasto result = gastoService.createGasto(1, 1, newGasto);

        // Verificar que el resultado no sea nulo y que el ID esté asignado
        assertNotNull(result, "El gasto creado no debería ser nulo");
        assertEquals(1, result.getId(), "El ID del gasto debería ser 1");
        assertEquals("Alquiler", result.getNombre(), "El nombre del gasto debería ser Alquiler");
        assertEquals(BigDecimal.valueOf(500), result.getMonto(), "El monto del gasto debería ser 500");

        // Verificar que se llamó al metodo save del repositorio
        verify(gastoRepository, times(1)).save(any(Gasto.class));
    }



    @Test
    public void testGetGastosByUsuarioId() {
        // Crear un mock de Usuario y de gastos
        Usuario usuario = new Usuario();
        usuario.setId(1);
        Gasto gasto = new Gasto();
        gasto.setId(1);
        gasto.setNombre("Comida");
        gasto.setMonto(BigDecimal.valueOf(50));
        gasto.setFecha(LocalDate.now());
        gasto.setUsuario(usuario);

        // Simular el comportamiento del userRepository y gastoRepository
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(usuario));
        when(gastoRepository.findByUsuario(usuario)).thenReturn(List.of(gasto));

        // Ejecutar el metodo getGastosByUsuarioId
        List<Gasto> result = gastoService.getGastosByUsuarioId(1);

        // Verificar que el resultado no sea nulo y que contenga el gasto simulado
        assertNotNull(result, "La lista de gastos no debería ser nula");
        assertEquals(1, result.size(), "La lista de gastos debería contener un elemento");
        assertEquals("Comida", result.get(0).getNombre(), "El nombre del gasto debería ser Comida");

        // Verificar que se llamó al metodo findByUsuario del repositorio
        verify(gastoRepository, times(1)).findByUsuario(usuario);
    }

    @Test
    public void testUpdateGasto() {
        // Crear un mock de Usuario y un gasto existente
        Usuario usuario = new Usuario();
        usuario.setId(1);

        Gasto existingGasto = new Gasto();
        existingGasto.setId(1);
        existingGasto.setNombre("Transporte");
        existingGasto.setMonto(BigDecimal.valueOf(100));
        existingGasto.setFecha(LocalDate.now());
        existingGasto.setUsuario(usuario);

        // Simular el comportamiento del gastoRepository y userRepository
        when(gastoRepository.findById(1)).thenReturn(Optional.of(existingGasto));
        when(gastoRepository.save(any(Gasto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Crear un gasto actualizado
        Gasto updatedGasto = new Gasto();
        updatedGasto.setNombre("Transporte Modificado");
        updatedGasto.setMonto(BigDecimal.valueOf(150));

        // Ejecutar el metodo updateGasto
        Gasto result = gastoService.updateGasto(1, updatedGasto);

        // Verificar que los campos se actualizaron correctamente
        assertEquals("Transporte Modificado", result.getNombre(), "El nombre del gasto debería haber sido modificado");
        assertEquals(BigDecimal.valueOf(150), result.getMonto(), "El monto del gasto debería haber sido modificado");

        // Verificar que se llamó al metodo save del repositorio
        verify(gastoRepository, times(1)).save(any(Gasto.class));
    }

    @Test
    public void testDeleteGasto() {
        // Crear una instancia de Gasto
        Gasto gasto = new Gasto();
        gasto.setId(1);
        gasto.setNombre("Alquiler");
        gasto.setMonto(BigDecimal.valueOf(500));
        gasto.setFecha(LocalDate.now());

        // Simular el comportamiento del gastoRepository
        when(gastoRepository.existsById(1)).thenReturn(true);
        when(gastoRepository.findById(1)).thenReturn(Optional.of(gasto));

        // Ejecutar el metodo deleteGasto con el ID del gasto
        gastoService.deleteGasto(1);

        // Verificar que se llamó al metodo deleteById del gastoRepository con el ID correcto
        verify(gastoRepository, times(1)).deleteById(1);
    }


}
