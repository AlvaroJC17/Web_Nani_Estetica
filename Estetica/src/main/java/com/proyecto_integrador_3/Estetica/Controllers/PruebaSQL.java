package com.proyecto_integrador_3.Estetica.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;

@Component
public class PruebaSQL implements CommandLineRunner {

	@Autowired
	RepositorioUsuario repositorioUsuario;
	@Override
	public void run(String... args) throws Exception {
		
		
		List <Usuario> prueba1 = repositorioUsuario.buscarPorNombre("cliente");
		prueba1.forEach(System.out::println);
		
		
	}

}
