package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.PagoDTO;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.PagoRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
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
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PagoService pagoService;

    private Usuario usuario;
    private Pago pago;
    private PagoDTO pagoDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("testuser");

        pago = new Pago();
        pago.setId(1);
        pago.setNombre("Internet");
        pago.setMonto(BigDecimal.valueOf(50.00));
        pago.setFecha(LocalDate.now().plusDays(5));
        pago.setUsuario(usuario);
        pago.setRecurrente(true);
        pago.setFrecuencia(Pago.Frecuencia.MENSUAL);

        pagoDTO = new PagoDTO(
                1,
                "Internet",
                BigDecimal.valueOf(50.00),
                LocalDate.now().plusDays(5),
                true,
                Pago.Frecuencia.MENSUAL
        );
    }

    @Test
    void getPagosByUsuarioId() {
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(pagoRepository.findByUsuarioId(1)).thenReturn(List.of(pago));

        List<PagoDTO> result = pagoService.getPagosByUsuarioId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Internet", result.get(0).getNombre());

        verify(userRepository, times(1)).findById(1);
        verify(pagoRepository, times(1)).findByUsuarioId(1);
    }

    @Test
    void createPago() {
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> {
            Pago pago = invocation.getArgument(0);
            pago.setId(1);
            return pago;
        });

        PagoDTO result = pagoService.createPago(1, pagoDTO);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Internet", result.getNombre());

        verify(userRepository, times(1)).findById(1);
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void updatePago() {
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PagoDTO updatedPagoDTO = new PagoDTO(1, "Electricidad", BigDecimal.valueOf(75.00), LocalDate.now().plusDays(10), false, null);
        PagoDTO result = pagoService.updatePago(1, updatedPagoDTO);

        assertNotNull(result);
        assertEquals("Electricidad", result.getNombre());
        assertEquals(BigDecimal.valueOf(75.00), result.getMonto());
        assertEquals(LocalDate.now().plusDays(10), result.getFecha());
        assertFalse(result.getRecurrente());

        verify(pagoRepository, times(1)).findById(1);
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void deletePago() {
        when(pagoRepository.existsById(1)).thenReturn(true);

        pagoService.deletePago(1);

        verify(pagoRepository, times(1)).existsById(1);
        verify(pagoRepository, times(1)).deleteById(1);
    }

    @Test
    void getPagosRecurrentesByUsuarioId() {
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(pagoRepository.findByUsuarioIdAndRecurrenteTrue(1)).thenReturn(List.of(pago));

        List<PagoDTO> result = pagoService.getPagosRecurrentesByUsuarioId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Internet", result.get(0).getNombre());
        assertTrue(result.get(0).getRecurrente());
        assertEquals(Pago.Frecuencia.MENSUAL, result.get(0).getFrecuencia());

        verify(userRepository, times(1)).findById(1);
        verify(pagoRepository, times(1)).findByUsuarioIdAndRecurrenteTrue(1);
    }

    @Test
    void getPagosProximosByUsuarioId() {
        LocalDate now = LocalDate.now();
        LocalDate limit = now.plusDays(15);

        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(pagoRepository.findPagosFuturosByUsuarioIdWithinDays(1, limit)).thenReturn(List.of(pago));

        List<PagoDTO> result = pagoService.getPagosProximosByUsuarioId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Internet", result.get(0).getNombre());
        assertEquals(BigDecimal.valueOf(50.00), result.get(0).getMonto());
        assertEquals(LocalDate.now().plusDays(5), result.get(0).getFecha());

        verify(userRepository, times(1)).findById(1);
        verify(pagoRepository, times(1)).findPagosFuturosByUsuarioIdWithinDays(1, limit);
    }

    @Test
    void createPago_ShouldThrowExceptionWhenFrecuenciaIsSetForNonRecurrentPayment() {
        PagoDTO nonRecurrentPagoDTO = new PagoDTO(
                null,
                "Pago Único",
                BigDecimal.valueOf(100.00),
                LocalDate.now(),
                false,
                Pago.Frecuencia.MENSUAL
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pagoService.createPago(1, nonRecurrentPagoDTO);
        });

        assertEquals("No se puede asignar una frecuencia a un pago no recurrente.", exception.getMessage());
        verifyNoInteractions(userRepository, pagoRepository);
    }

    @Test
    void getPagosByUsuarioIdThrowsExceptionWhenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> pagoService.getPagosByUsuarioId(1));

        verify(userRepository, times(1)).findById(1);
        verifyNoInteractions(pagoRepository);
    }

    @Test
    void getPagosProximosByUsuarioIdThrowsExceptionWhenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> pagoService.getPagosProximosByUsuarioId(1));

        verify(userRepository, times(1)).findById(1);
        verifyNoInteractions(pagoRepository);
    }
}
