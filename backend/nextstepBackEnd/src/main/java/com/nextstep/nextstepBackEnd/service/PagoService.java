package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.PagoDTO;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.PagoRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

        // Convertir cada Pago a PagoDTO
        return pagos.stream()
                .map(pago -> new PagoDTO(
                        pago.getId(),
                        pago.getNombre(),
                        pago.getMonto(),
                        pago.getFecha(),
                        pago.getRecurrente(),
                        pago.getFrecuencia()
                ))
                .collect(Collectors.toList());
    }

    // Crear un nuevo pago
    @Transactional
    public PagoDTO createPago(Integer usuarioId, PagoDTO pagoDTO) {
        Usuario usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        Pago pago = new Pago();
        pago.setNombre(pagoDTO.getNombre());
        pago.setMonto(pagoDTO.getMonto());
        pago.setFecha(pagoDTO.getFecha());
        pago.setRecurrente(pagoDTO.getRecurrente());
        pago.setFrecuencia(pagoDTO.getFrecuencia());
        pago.setUsuario(usuario);

        Pago savedPago = pagoRepository.save(pago);

        return new PagoDTO(
                savedPago.getId(),
                savedPago.getNombre(),
                savedPago.getMonto(),
                savedPago.getFecha(),
                savedPago.getRecurrente(),
                savedPago.getFrecuencia()
        );
    }

    // Actualizar un pago existente
    @Transactional
    public PagoDTO updatePago(Integer pagoId, PagoDTO pagoDTO) {
        return pagoRepository.findById(pagoId).map(existingPago -> {
            existingPago.setNombre(pagoDTO.getNombre());
            existingPago.setMonto(pagoDTO.getMonto());
            existingPago.setFecha(pagoDTO.getFecha());
            existingPago.setRecurrente(pagoDTO.getRecurrente());
            existingPago.setFrecuencia(pagoDTO.getFrecuencia());
            Pago updatedPago = pagoRepository.save(existingPago);

            return new PagoDTO(
                    updatedPago.getId(),
                    updatedPago.getNombre(),
                    updatedPago.getMonto(),
                    updatedPago.getFecha(),
                    updatedPago.getRecurrente(),
                    updatedPago.getFrecuencia()
            );
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
                .map(pago -> new PagoDTO(
                        pago.getId(),
                        pago.getNombre(),
                        pago.getMonto(),
                        pago.getFecha(),
                        pago.getRecurrente(),
                        pago.getFrecuencia()
                ))
                .collect(Collectors.toList());
    }
}

