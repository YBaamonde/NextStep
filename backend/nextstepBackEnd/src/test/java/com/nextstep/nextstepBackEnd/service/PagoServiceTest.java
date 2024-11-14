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

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        pago.setFecha(LocalDate.now());
        pago.setUsuario(usuario);
        pago.setRecurrente(true);
        pago.setFrecuencia(Pago.Frecuencia.MENSUAL);

        pagoDTO = new PagoDTO(
                1,
                "Internet",
                BigDecimal.valueOf(50.00),
                LocalDate.now(),
                true,
                Pago.Frecuencia.MENSUAL
        );
    }

    @Test
    void getPagosByUsuarioId() {
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(pagoRepository.findByUsuarioId(1)).thenReturn(List.of(pago));

        List<PagoDTO> pagos = pagoService.getPagosByUsuarioId(1);

        Assertions.assertNotNull(pagos);
        assertEquals(1, pagos.size());
        assertEquals("Internet", pagos.get(0).getNombre());
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

        Assertions.assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Internet", result.getNombre());
        verify(userRepository, times(1)).findById(1);
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void updatePago() {
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PagoDTO updatedPagoDTO = new PagoDTO(1, "Gas", BigDecimal.valueOf(75.00), LocalDate.now(), false, null);
        PagoDTO result = pagoService.updatePago(1, updatedPagoDTO);

        Assertions.assertNotNull(result);
        assertEquals("Gas", result.getNombre());
        Assertions.assertFalse(result.getRecurrente());
        Assertions.assertNull(result.getFrecuencia());
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

        List<PagoDTO> pagosRecurrentes = pagoService.getPagosRecurrentesByUsuarioId(1);

        Assertions.assertNotNull(pagosRecurrentes);
        assertEquals(1, pagosRecurrentes.size());
        Assertions.assertTrue(pagosRecurrentes.get(0).getRecurrente());
        assertEquals(Pago.Frecuencia.MENSUAL, pagosRecurrentes.get(0).getFrecuencia());

        verify(userRepository, times(1)).findById(1);
        verify(pagoRepository, times(1)).findByUsuarioIdAndRecurrenteTrue(1);
    }

    @Test
    void createPago_ShouldThrowExceptionWhenFrecuenciaIsSetForNonRecurrentPayment() {
        PagoDTO nonRecurrentPagoDTO = new PagoDTO(
                1,
                "Pago Único",
                BigDecimal.valueOf(100.00),
                LocalDate.now(),
                false,
                Pago.Frecuencia.MENSUAL // Frecuencia está definida aunque recurrente es false
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pagoService.createPago(1, nonRecurrentPagoDTO); // Llamada a createPago que invoca la validación
        });

        assertEquals("No se puede asignar una frecuencia a un pago no recurrente.", exception.getMessage());

        // Verifica que no hay interacción con userRepository ni con pagoRepository
        verifyNoInteractions(userRepository);
        verifyNoInteractions(pagoRepository);
    }


}

