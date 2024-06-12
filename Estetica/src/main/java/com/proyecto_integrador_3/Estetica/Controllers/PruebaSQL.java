package com.proyecto_integrador_3.Estetica.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioProfesional;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioPersona;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioProfesional;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

//@Component
public class PruebaSQL implements CommandLineRunner {

	@Autowired
	RepositorioUsuario repositorioUsuario;
	
	@Autowired
	RepositorioPersona repositorioPersona;
	
	@Autowired
	RepositorioProfesional repositorioProfesional;
	
	@Autowired
	RepositorioCliente repositorioCliente;
	
	@Autowired
	ServicioUsuario servicioUsuario;
	
	@Autowired
	ServicioPersona servicioPersona;
	
	@Autowired
	ServicioProfesional servicioProfesional;
	

	
	
	@Override
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
		
		System.out.println("De aqui comienzan las pruebas");
		
		List<Persona> prueba4 = servicioProfesional.buscarPacienteByRolAndDni("63565373", Rol.CLIENTE);
		
		for(Persona persona: prueba4) {
			System.out.println("DNI ROL :" + persona.getNombre());
		}
		List<Persona> prueba5 = servicioProfesional.buscarPacienteByRolAndNombre("Deyannys", Rol.CLIENTE);
		
		for(Persona persona: prueba5) {
			System.out.println("NOMBRE ROL :" + persona.getDni());
		}
		
		List<Persona> prueba6 = servicioProfesional.buscarPacienteByRolAndEmail2("comida@comida.com", Rol.CLIENTE);
		
		for(Persona persona: prueba6) {
			System.out.println("EMAIL ROL :" + persona.getNombre());
		}
		
		Optional<Cliente> datosCliente = repositorioCliente.obtenerDatosClienteUsuarioPorIdOptional("18156700-d56c-4196-95f1-6e2010ad8974");
		
		if (datosCliente.isPresent()) {
			Cliente nuevoCliente = datosCliente.get();
			System.out.println(nuevoCliente.getFuma());
			System.out.println(nuevoCliente.getAmamantando());
			System.out.println(nuevoCliente.getCuidado_de_piel());
		}
		
		
		
	}

}
