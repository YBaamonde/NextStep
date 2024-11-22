package com.nextstep.nextstepBackEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Habilitar la programación de tareas para el envío de notificaciones
public class NextstepBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(NextstepBackEndApplication.class, args);
	}

}
