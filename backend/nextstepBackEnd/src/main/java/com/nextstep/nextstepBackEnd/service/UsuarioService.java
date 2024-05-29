package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UserRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword()) // Asegúrate de que este es el getter correcto
                .roles(usuario.getRol().name())
                .build();
    }

    public void registrarUsuario(Usuario usuario) {
        // Registrar usuario sin passwordEncoder.encode
        usuarioRepository.save(usuario);
    }
}
