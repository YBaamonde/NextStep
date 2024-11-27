package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.model.notif.NotificacionConfig;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Transactional
public class NotificacionConfigRepositoryTest {

    @Autowired
    private NotificacionConfigRepository notificacionConfigRepository;

    @Autowired
    private UserRepository userRepository; // Asegúrate de tener este repositorio

    @Test
    public void testFindByUsuarioId_ReturnsConfig_WhenExists() {
        // Crear un usuario
        Usuario usuario = new Usuario();
        usuario.setUsername("testUser");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password");
        usuario = userRepository.save(usuario);

        // Crear una configuración de notificación asociada al usuario
        NotificacionConfig config = NotificacionConfig.builder()
                .usuario(usuario)
                .emailActivas(true)
                .emailDiasAntes(2)
                .inAppActivas(true)
                .inAppDiasAntes(1)
                .build();
        notificacionConfigRepository.save(config);

        // Verificar que se puede recuperar la configuración
        Optional<NotificacionConfig> result = notificacionConfigRepository.findByUsuarioId(usuario.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getUsuario().getId()).isEqualTo(usuario.getId());
        assertThat(result.get().isEmailActivas()).isTrue();
    }
}