package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Categoria;
import com.nextstep.nextstepBackEnd.model.Gasto;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.CategoriaRepository;
import com.nextstep.nextstepBackEnd.repository.GastoRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GastoService {
    private final GastoRepository gastoRepository;
    private final UserRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    public List<Gasto> getGastosByUsuarioId(Integer userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return gastoRepository.findByUsuario(usuario);
    }

    public Gasto createGasto(Integer userId, Integer categoriaId, String nombre, BigDecimal monto, LocalDate fecha) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Gasto gasto = Gasto.builder()
                .usuario(usuario)
                .categoria(categoria)
                .nombre(nombre)
                .monto(monto)
                .fecha(fecha)
                .build();
        return gastoRepository.save(gasto);
    }
}

