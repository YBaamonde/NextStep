package com.nextstep.nextstepBackEnd.config;

import com.nextstep.nextstepBackEnd.model.Rol;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminConfig adminConfig;

    @Autowired
    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, AdminConfig adminConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminConfig = adminConfig;
    }

    @PostConstruct
    public void initAdminUser() {
        // Verifica si el usuario admin ya existe
        if (userRepository.findByEmail(adminConfig.getAdminEmail()).isEmpty()) {
            // Crea el nuevo usuario admin
            Usuario admin = new Usuario();
            admin.setUsername(adminConfig.getAdminUsername());
            admin.setEmail(adminConfig.getAdminEmail());
            admin.setPassword(passwordEncoder.encode(adminConfig.getAdminPassword()));
            admin.setRol(Rol.admin);
            userRepository.save(admin);  // Guarda el usuario en la base de datos
            System.out.println("Usuario admin creado con éxito");
        } else {
            System.out.println("El usuario admin ya existe");
        }
    }
}
