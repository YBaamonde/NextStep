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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        categoria.setNombre("Casa");

        gasto = new Gasto();
        gasto.setId(1);
        gasto.setNombre("Alquiler");
        gasto.setMonto(BigDecimal.valueOf(500));
        gasto.setFecha(LocalDate.of(2024, 1, 1));
        gasto.setUsuario(usuario);
        gasto.setCategoria(categoria);

        gastoDTO = new GastoDTO(
                1,
                "Alquiler",
                BigDecimal.valueOf(500),
                LocalDate.of(2024, 1, 1),
                1
        );
    }

    @Test
    void getGastosByUsuarioId() {
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(gastoRepository.findByUsuario(usuario)).thenReturn(List.of(gasto));

        List<GastoDTO> result = gastoService.getGastosByUsuarioId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Alquiler", result.get(0).getNombre());

        verify(userRepository, times(1)).findById(1);
        verify(gastoRepository, times(1)).findByUsuario(usuario);
    }

    @Test
    void createGasto() {
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(gastoRepository.save(any(Gasto.class))).thenAnswer(invocation -> {
            Gasto gasto = invocation.getArgument(0);
            gasto.setId(1);
            return gasto;
        });

        GastoDTO result = gastoService.createGasto(1, 1, gastoDTO);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Alquiler", result.getNombre());

        verify(userRepository, times(1)).findById(1);
        verify(categoriaRepository, times(1)).findById(1);
        verify(gastoRepository, times(1)).save(any(Gasto.class));
    }

    @Test
    void updateGasto() {
        when(gastoRepository.findById(1)).thenReturn(Optional.of(gasto));
        when(gastoRepository.save(any(Gasto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GastoDTO updatedGastoDTO = new GastoDTO(1, "Internet", BigDecimal.valueOf(300), LocalDate.of(2024, 2, 1), 1);
        GastoDTO result = gastoService.updateGasto(1, updatedGastoDTO);

        assertNotNull(result);
        assertEquals("Internet", result.getNombre());

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
    void getGastosByCategoriaConLimite() {
        when(gastoRepository.findTopGastosByCategoria(eq(1), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(gasto)));

        List<GastoDTO> result = gastoService.getGastosByCategoriaConLimite(1, 5);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Alquiler", result.get(0).getNombre());

        verify(gastoRepository, times(1)).findTopGastosByCategoria(eq(1), any(Pageable.class));
    }

    @Test
    void getGastosPorTrimestre() {
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(new Object[]{1, BigDecimal.valueOf(200)});
        when(gastoRepository.findGastosByTrimestre(1)).thenReturn(mockResult);

        Map<Integer, BigDecimal> result = gastoService.getGastosPorTrimestre(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(BigDecimal.valueOf(200), result.get(1));

        verify(gastoRepository, times(1)).findGastosByTrimestre(1);
    }

    @Test
    void getGastosPorCategoria() {
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(new Object[]{"Casa", BigDecimal.valueOf(1000)});
        when(gastoRepository.findGastosGroupedByCategoria(1)).thenReturn(mockResult);

        Map<String, BigDecimal> result = gastoService.getGastosPorCategoria(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(BigDecimal.valueOf(1000), result.get("Casa"));

        verify(gastoRepository, times(1)).findGastosGroupedByCategoria(1);
    }

    @Test
    void createGastoThrowsExceptionWhenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gastoService.createGasto(1, 1, gastoDTO));

        verify(userRepository, times(1)).findById(1);
        verifyNoInteractions(categoriaRepository, gastoRepository);
    }

    @Test
    void createGastoThrowsExceptionWhenCategoryNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(categoriaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gastoService.createGasto(1, 1, gastoDTO));

        verify(userRepository, times(1)).findById(1);
        verify(categoriaRepository, times(1)).findById(1);
        verifyNoInteractions(gastoRepository);
    }

    @Test
    void updateGastoThrowsExceptionWhenGastoNotFound() {
        when(gastoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gastoService.updateGasto(1, gastoDTO));

        verify(gastoRepository, times(1)).findById(1);
        verifyNoMoreInteractions(gastoRepository);
    }
}
