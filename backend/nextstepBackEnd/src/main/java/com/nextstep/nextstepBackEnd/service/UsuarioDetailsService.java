package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new User(usuario.getEmail(), usuario.getContraseña(),
                Collections.singletonList(new SimpleGrantedAuthority(usuario.getRol().name())));
    }
}
