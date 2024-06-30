package com.proyecto_integrador_3.Estetica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling //activa tareas programadas en la aplicacion
public class EsteticaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EsteticaApplication.class, args);
	}

}
