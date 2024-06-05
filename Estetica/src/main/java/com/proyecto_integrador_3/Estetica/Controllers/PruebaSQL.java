package com.proyecto_integrador_3.Estetica.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioPersona;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

//@Component
public class PruebaSQL implements CommandLineRunner {

	@Autowired
	RepositorioUsuario repositorioUsuario;
	
	@Autowired
	RepositorioPersona repositorioPersona;
	
	@Autowired
	ServicioUsuario servicioUsuario;
	
	@Autowired
	ServicioPersona servicioPersona;
	

	
	
	//@Override
	public void run(String... args) throws Exception {
		
		
		List <Usuario> prueba1 = repositorioUsuario.buscarPorNombre("cliente");
		prueba1.forEach(System.out::println);
		
		System.out.println("HOLA mUNDO");
		
		List <Usuario> prueba2 = servicioUsuario.buscarRol(Rol.PROFESIONAL);
		prueba2.forEach(System.out::println);
		
		
		
		List<Persona> prueba3 = servicioPersona.buscarNombreApellidoPorRol(Rol.PROFESIONAL);
		prueba3.forEach(System.out::println);
		
		for(Persona persona: prueba3) {
			System.out.println("Nombre :" + persona.getNombre());
		}
		
		
		
		
	}

}
