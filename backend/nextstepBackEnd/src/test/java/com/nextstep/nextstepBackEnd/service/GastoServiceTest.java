package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Categoria;
import com.nextstep.nextstepBackEnd.model.Gasto;
import com.nextstep.nextstepBackEnd.model.GastoDTO;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.CategoriaRepository;
import com.nextstep.nextstepBackEnd.repository.GastoRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class GastoServiceTest {

    @Mock
    private GastoRepository gastoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GastoService gastoService;

    private Usuario mockUser;
    private Categoria mockCategoria;
    private Gasto mockGasto;
    private GastoDTO mockGastoDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Crear usuario simulado
        mockUser = new Usuario();
        mockUser.setId(1);
        mockUser.setUsername("testuser");

        // Crear categoría simulada
        mockCategoria = new Categoria();
        mockCategoria.setId(1);
        mockCategoria.setNombre("Alimentación");

        // Crear gasto simulado
        mockGasto = new Gasto();
        mockGasto.setId(1);
        mockGasto.setNombre("Compra");
        mockGasto.setMonto(BigDecimal.valueOf(50.00));
        mockGasto.setFecha(LocalDate.now());
        mockGasto.setUsuario(mockUser);
        mockGasto.setCategoria(mockCategoria);

        // Crear GastoDTO simulado
        mockGastoDTO = new GastoDTO(
                mockGasto.getId(),
                mockGasto.getNombre(),
                mockGasto.getMonto(),
                mockGasto.getFecha(),
                mockCategoria.getId()
        );

        // Configurar los mocks
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(mockCategoria));
        when(gastoRepository.findById(1)).thenReturn(Optional.of(mockGasto));
        when(gastoRepository.findByUsuario(mockUser)).thenReturn(List.of(mockGasto));
    }

    @Test
    public void testCreateGasto() {
        when(gastoRepository.save(any(Gasto.class))).thenAnswer(invocation -> {
            Gasto gasto = invocation.getArgument(0);
            gasto.setId(1);
            return gasto;
        });

        GastoDTO result = gastoService.createGasto(1, 1, mockGastoDTO);

        assertNotNull(result, "El gasto creado no debería ser nulo");
        assertEquals(1, result.getId(), "El ID del gasto debería ser 1");
        assertEquals("Compra", result.getNombre(), "El nombre del gasto debería ser Compra");
        assertEquals(BigDecimal.valueOf(50.00), result.getMonto(), "El monto del gasto debería ser 50.00");

        verify(gastoRepository, times(1)).save(any(Gasto.class));
    }

    @Test
    public void testGetGastosByUsuarioId() {
        List<GastoDTO> result = gastoService.getGastosByUsuarioId(1);

        assertNotNull(result, "La lista de gastos no debería ser nula");
        assertEquals(1, result.size(), "La lista de gastos debería contener un elemento");
        assertEquals("Compra", result.get(0).getNombre(), "El nombre del gasto debería ser Compra");

        verify(gastoRepository, times(1)).findByUsuario(mockUser);
    }


    @Test
    public void testUpdateGasto() {
        GastoDTO updatedGastoDTO = new GastoDTO(
                1,
                "Compra Modificada",
                BigDecimal.valueOf(100.00),
                LocalDate.now(),
                mockCategoria.getId()
        );

        when(gastoRepository.save(any(Gasto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GastoDTO result = gastoService.updateGasto(1, updatedGastoDTO);

        assertEquals("Compra Modificada", result.getNombre(), "El nombre del gasto debería haber sido modificado");
        assertEquals(BigDecimal.valueOf(100.00), result.getMonto(), "El monto del gasto debería haber sido modificado");

        verify(gastoRepository, times(1)).save(any(Gasto.class));
    }

    @Test
    public void testDeleteGasto() {
        doNothing().when(gastoRepository).deleteById(1);

        gastoService.deleteGasto(1);

        verify(gastoRepository, times(1)).deleteById(1);
    }
}
