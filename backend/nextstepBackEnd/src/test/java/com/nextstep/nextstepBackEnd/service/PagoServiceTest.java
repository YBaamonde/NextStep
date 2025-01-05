package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.PagoDTO;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.PagoRepository;
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
    void updatePago_ShouldUpdateExistingPago() {
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
    void updatePago_ShouldThrowExceptionWhenPagoNotFound() {
        when(pagoRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pagoService.updatePago(1, pagoDTO);
        });

        assertEquals("Pago no encontrado.", exception.getMessage());
        verify(pagoRepository, times(1)).findById(1);
        verifyNoMoreInteractions(pagoRepository);
    }

    @Test
    void deletePago_ShouldDeleteExistingPago() {
        when(pagoRepository.existsById(1)).thenReturn(true);

        pagoService.deletePago(1);

        verify(pagoRepository, times(1)).existsById(1);
        verify(pagoRepository, times(1)).deleteById(1);
    }

    @Test
    void deletePago_ShouldThrowExceptionWhenPagoNotFound() {
        when(pagoRepository.existsById(1)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pagoService.deletePago(1);
        });

        assertEquals("Pago no encontrado.", exception.getMessage());
        verify(pagoRepository, times(1)).existsById(1);
        verifyNoMoreInteractions(pagoRepository);
    }

    @Test
    void getPagosRecurrentesByUsuarioId_ShouldReturnRecurrentPayments() {
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
    void getPagosRecurrentesByUsuarioId_ShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pagoService.getPagosRecurrentesByUsuarioId(1);
        });

        assertEquals("Usuario no encontrado.", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
        verifyNoInteractions(pagoRepository);
    }

    @Test
    void getPagosConRecurrencia_ShouldHandleRecurrentAndNonRecurrentPagos() {
        Pago nonRecurrentPago = new Pago();
        nonRecurrentPago.setId(2);
        nonRecurrentPago.setNombre("Gym");
        nonRecurrentPago.setMonto(BigDecimal.valueOf(30.00));
        nonRecurrentPago.setFecha(LocalDate.now().plusDays(3));
        nonRecurrentPago.setUsuario(usuario);
        nonRecurrentPago.setRecurrente(false);

        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(pagoRepository.findByUsuarioId(1)).thenReturn(List.of(pago, nonRecurrentPago));

        List<PagoDTO> result = pagoService.getPagosConRecurrencia(1);

        assertNotNull(result);
        assertTrue(result.size() > 2); // Recurrent payments generate additional entries
        assertTrue(result.stream().anyMatch(dto -> dto.getNombre().equals("Gym") && !dto.getRecurrente()));

        verify(userRepository, times(1)).findById(1);
        verify(pagoRepository, times(1)).findByUsuarioId(1);
    }

    @Test
    void avanzarFecha_ShouldGenerateCorrectDatesForFrequencies() {
        LocalDate initialDate = LocalDate.of(2024, 1, 1);

        assertEquals(initialDate.plusDays(1), pagoService.avanzarFecha(initialDate, Pago.Frecuencia.DIARIA));
        assertEquals(initialDate.plusWeeks(1), pagoService.avanzarFecha(initialDate, Pago.Frecuencia.SEMANAL));
        assertEquals(initialDate.plusMonths(1), pagoService.avanzarFecha(initialDate, Pago.Frecuencia.MENSUAL));
        assertEquals(initialDate.plusYears(1), pagoService.avanzarFecha(initialDate, Pago.Frecuencia.ANUAL));
    }

    @Test
    void convertirAPagoDTO_ShouldMapPagoToPagoDTOCorrectly() {
        // Configurar usuario
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));

        // Configurar pago recurrente
        Pago testPago = new Pago();
        testPago.setId(10);
        testPago.setNombre("Test Pago");
        testPago.setMonto(BigDecimal.valueOf(100.00));
        testPago.setFecha(LocalDate.of(2024, 1, 15));
        testPago.setRecurrente(true);
        testPago.setFrecuencia(Pago.Frecuencia.MENSUAL);
        testPago.setUsuario(usuario);

        when(pagoRepository.findByUsuarioId(1)).thenReturn(List.of(testPago));

        // Ejecutar el metodo
        List<PagoDTO> result = pagoService.getPagosConRecurrencia(1);

        // Validar que se generaron 12 ocurrencias
        assertNotNull(result);
        assertEquals(12, result.size()); // Verifica las 12 ocurrencias generadas

        // Validar los datos del primer pago
        PagoDTO pagoDTO = result.get(0);
        assertEquals(testPago.getId(), pagoDTO.getId());
        assertEquals(testPago.getNombre(), pagoDTO.getNombre());
        assertEquals(testPago.getMonto(), pagoDTO.getMonto());
        assertEquals(testPago.getFecha(), pagoDTO.getFecha());
        assertEquals(testPago.getRecurrente(), pagoDTO.getRecurrente());
        assertEquals(testPago.getFrecuencia(), pagoDTO.getFrecuencia());

        // Validar que las fechas de los pagos son correctas
        for (int i = 0; i < 12; i++) {
            PagoDTO ocurrencia = result.get(i);
            LocalDate expectedDate = testPago.getFecha().plusMonths(i);
            assertEquals(expectedDate, ocurrencia.getFecha()); // Verifica las fechas generadas
        }

        // Verificar interacciones con los mocks
        verify(userRepository, times(1)).findById(1);
        verify(pagoRepository, times(1)).findByUsuarioId(1);
    }

}
