package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.PagoDTO;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.PagoRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final UserRepository userRepository;

    public PagoService(PagoRepository pagoRepository, UserRepository userRepository) {
        this.pagoRepository = pagoRepository;
        this.userRepository = userRepository;
    }

    // Obtener todos los pagos de un usuario por ID
    public List<PagoDTO> getPagosByUsuarioId(Integer usuarioId) {
        Usuario usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        List<Pago> pagos = pagoRepository.findByUsuarioId(usuario.getId());

        return pagos.stream()
                .map(this::convertirAPagoDTO)
                .collect(Collectors.toList());
    }

    // Crear un nuevo pago
    @Transactional
    public PagoDTO createPago(Integer usuarioId, PagoDTO pagoDTO) {
        validarFrecuenciaRecurrente(pagoDTO);

        Usuario usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        Pago pago = new Pago();

        pago.setUsuario(usuario);
        pago.setNombre(pagoDTO.getNombre());
        pago.setMonto(pagoDTO.getMonto());
        pago.setFecha(pagoDTO.getFecha());
        pago.setRecurrente(pagoDTO.getRecurrente());
        pago.setFrecuencia(pagoDTO.getRecurrente() ? pagoDTO.getFrecuencia() : null);

        Pago savedPago = pagoRepository.save(pago);

        return convertirAPagoDTO(savedPago);
    }

    // Actualizar un pago existente
    @Transactional
    public PagoDTO updatePago(Integer pagoId, PagoDTO pagoDTO) {
        return pagoRepository.findById(pagoId).map(existingPago -> {
            existingPago.setNombre(pagoDTO.getNombre());
            existingPago.setMonto(pagoDTO.getMonto());
            existingPago.setFecha(pagoDTO.getFecha());
            existingPago.setRecurrente(pagoDTO.getRecurrente());

            if (pagoDTO.getRecurrente() && pagoDTO.getFrecuencia() != null) {
                existingPago.setFrecuencia(pagoDTO.getFrecuencia());
            } else {
                existingPago.setFrecuencia(null);
            }

            Pago updatedPago = pagoRepository.save(existingPago);
            return convertirAPagoDTO(updatedPago);
        }).orElseThrow(() -> new IllegalArgumentException("Pago no encontrado."));
    }

    // Eliminar un pago
    @Transactional
    public void deletePago(Integer pagoId) {
        if (pagoRepository.existsById(pagoId)) {
            pagoRepository.deleteById(pagoId);
        } else {
            throw new IllegalArgumentException("Pago no encontrado.");
        }
    }

    // Obtener todos los pagos recurrentes de un usuario
    public List<PagoDTO> getPagosRecurrentesByUsuarioId(Integer usuarioId) {
        Usuario usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        List<Pago> pagos = pagoRepository.findByUsuarioIdAndRecurrenteTrue(usuario.getId());

        return pagos.stream()
                .map(this::convertirAPagoDTO)
                .collect(Collectors.toList());
    }

    // Obtener todos los pagos con sus recurrencias incluidas
    public List<PagoDTO> getPagosConRecurrencia(Integer usuarioId) {
        Usuario usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        List<Pago> pagos = pagoRepository.findByUsuarioId(usuario.getId());
        List<PagoDTO> pagosConRecurrencia = new ArrayList<>();

        for (Pago pago : pagos) {
            if (pago.getRecurrente() && pago.getFrecuencia() != null) {
                pagosConRecurrencia.addAll(generarRecurrencias(pago, pago.getFrecuencia()));
            } else {
                pagosConRecurrencia.add(convertirAPagoDTO(pago));
            }
        }

        return pagosConRecurrencia;
    }

    // Generar las ocurrencias de pagos recurrentes
    private List<PagoDTO> generarRecurrencias(Pago pago, Pago.Frecuencia frecuencia) {
        List<PagoDTO> recurrencias = new ArrayList<>();
        LocalDate fechaActual = pago.getFecha();

        for (int i = 0; i < 12; i++) { // Generar 12 ocurrencias
            recurrencias.add(new PagoDTO(
                    pago.getId(),
                    pago.getNombre(),
                    pago.getMonto(),
                    fechaActual,
                    true,
                    pago.getFrecuencia()
            ));
            fechaActual = avanzarFecha(fechaActual, frecuencia);
        }

        return recurrencias;
    }

    // Avanzar la fecha según la frecuencia
    LocalDate avanzarFecha(LocalDate fecha, Pago.Frecuencia frecuencia) {
        switch (frecuencia) {
            case DIARIA:
                return fecha.plusDays(1);
            case SEMANAL:
                return fecha.plusWeeks(1);
            case MENSUAL:
                return fecha.plusMonths(1);
            case ANUAL:
                return fecha.plusYears(1);
            default:
                throw new IllegalArgumentException("Frecuencia no soportada: " + frecuencia);
        }
    }

    // Convertir Pago a PagoDTO
    private PagoDTO convertirAPagoDTO(Pago pago) {
        return new PagoDTO(
                pago.getId(),
                pago.getNombre(),
                pago.getMonto(),
                pago.getFecha(),
                pago.getRecurrente(),
                pago.getFrecuencia() != null ? Pago.Frecuencia.valueOf(pago.getFrecuencia().name()) : null
        );
    }


    // Validar frecuencia solo para pagos recurrentes
    private void validarFrecuenciaRecurrente(PagoDTO pagoDTO) {
        if (!pagoDTO.getRecurrente() && pagoDTO.getFrecuencia() != null) {
            throw new IllegalArgumentException("No se puede asignar una frecuencia a un pago no recurrente.");
        }
    }
}