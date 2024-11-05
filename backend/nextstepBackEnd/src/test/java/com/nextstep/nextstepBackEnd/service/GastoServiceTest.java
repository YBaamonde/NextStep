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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GastoServiceTest {

    @Mock
    private GastoRepository gastoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GastoService gastoService;

    private Usuario usuario;
    private Categoria categoria;
    private Gasto gasto;
    private GastoDTO gastoDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("testuser");

        categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Alimentación");

        gasto = new Gasto();
        gasto.setId(1);
        gasto.setNombre("Compra");
        gasto.setMonto(BigDecimal.valueOf(100.0));
        gasto.setFecha(LocalDate.of(2022, 1, 1));
        gasto.setUsuario(usuario);
        gasto.setCategoria(categoria);

        gastoDTO = new GastoDTO(
                1,
                "Compra",
                BigDecimal.valueOf(100.0),
                LocalDate.of(2022, 1, 1),
                1
        );
    }

    @Test
    void getGastosByUsuarioId() {
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(gastoRepository.findByUsuario(usuario)).thenReturn(List.of(gasto));

        List<GastoDTO> gastos = gastoService.getGastosByUsuarioId(1);

        assertNotNull(gastos);
        assertEquals(1, gastos.size());
        assertEquals("Compra", gastos.get(0).getNombre());
        assertEquals(BigDecimal.valueOf(100.0), gastos.get(0).getMonto());
        assertEquals(LocalDate.of(2022, 1, 1), gastos.get(0).getFecha());
        assertEquals(1, gastos.get(0).getCategoriaId());

        verify(userRepository, times(1)).findById(1);
        verify(gastoRepository, times(1)).findByUsuario(usuario);
    }

    @Test
    void createGasto() {
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(gastoRepository.save(any(Gasto.class))).thenAnswer(invocation -> {
            Gasto gasto = invocation.getArgument(0);
            gasto.setId(1); // Assign a mock ID
            return gasto;
        });

        GastoDTO newGastoDTO = new GastoDTO(null, "Alquiler", BigDecimal.valueOf(500), LocalDate.now(), 1);
        GastoDTO result = gastoService.createGasto(1, 1, newGastoDTO);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Alquiler", result.getNombre());
        assertEquals(BigDecimal.valueOf(500), result.getMonto());

        verify(userRepository, times(1)).findById(1);
        verify(categoriaRepository, times(1)).findById(1);
        verify(gastoRepository, times(1)).save(any(Gasto.class));
    }

    @Test
    void updateGasto() {
        when(gastoRepository.findById(1)).thenReturn(Optional.of(gasto));
        when(gastoRepository.save(any(Gasto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GastoDTO updatedGastoDTO = new GastoDTO(1, "Transporte Modificado", BigDecimal.valueOf(150), LocalDate.now(), 1);
        GastoDTO result = gastoService.updateGasto(1, updatedGastoDTO);

        assertNotNull(result);
        assertEquals("Transporte Modificado", result.getNombre());
        assertEquals(BigDecimal.valueOf(150), result.getMonto());

        verify(gastoRepository, times(1)).findById(1);
        verify(gastoRepository, times(1)).save(any(Gasto.class));
    }

    @Test
    void deleteGasto() {
        when(gastoRepository.existsById(1)).thenReturn(true);

        gastoService.deleteGasto(1);

        verify(gastoRepository, times(1)).existsById(1);
        verify(gastoRepository, times(1)).deleteById(1);
    }

    @Test
    void createGastoThrowsExceptionWhenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gastoService.createGasto(1, 1, gastoDTO));

        verify(userRepository, times(1)).findById(1);
        verifyNoMoreInteractions(categoriaRepository, gastoRepository);
    }

    @Test
    void createGastoThrowsExceptionWhenCategoryNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(categoriaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gastoService.createGasto(1, 1, gastoDTO));

        verify(userRepository, times(1)).findById(1);
        verify(categoriaRepository, times(1)).findById(1);
        verifyNoMoreInteractions(gastoRepository);
    }

    @Test
    void updateGastoThrowsExceptionWhenGastoNotFound() {
        when(gastoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gastoService.updateGasto(1, gastoDTO));

        verify(gastoRepository, times(1)).findById(1);
        verifyNoMoreInteractions(gastoRepository);
    }
}
