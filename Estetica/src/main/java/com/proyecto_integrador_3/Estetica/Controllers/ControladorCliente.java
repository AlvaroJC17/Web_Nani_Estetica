package com.proyecto_integrador_3.Estetica.Controllers;

import java.sql.SQLException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCliente;

import jakarta.persistence.EntityManager;

@Controller
//@RequestMapping(")
public class ControladorCliente {

	@Autowired
	public RepositorioCliente repositorioCliente;
	@Autowired
	public ServicioCliente servicioCliente;
	
	
	@GetMapping("homeCliente")
	public String homeCliente() {
	return "/pagina_cliente/homeCliente";	
	}
	
	@GetMapping("misdatosCliente")
	public String misdatosCliente() {
	return "/pagina_cliente/misdatosCliente";	
	}
	
	@GetMapping("reservaDeTurnoCliente")
	public String reservaDeTurnoCliente() {
	return "/pagina_cliente/reservaDeTurnoCliente";	
	}
	
	@GetMapping("misturnos")
	public String misturnos() {
	return "/pagina_cliente/misturnos";	
	}
	
	@GetMapping("misconsultas")
	public String misconsultas() {
	return "/pagina_cliente/misconsultas";	
	}
	
	@GetMapping("cambiarContrasenaCliente")
	public String cambiarContrasenaCliente() {
	return "/pagina_cliente/cambiarContrasenaCliente";	
	}
	
	
	/*
	@PostMapping("/ingresarCliente")
	public String ingresarCliente( @RequestBody Cliente nuevo_cliente) throws SQLException {
		
		
			try {
				servicioCliente.registrarCliente(nuevo_cliente);
				System.out.println("Agregado con exito");
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("no se agrego");
			}
		
			return "login";
		
	}
	
	@PostMapping("/modificar")
	public String actualizarCliente(@RequestBody Cliente cliente) {
		
		try {
			servicioCliente.modificarCliente(cliente);
			System.out.println("Actualizado con exito!!!");
		} catch (Exception e) {
			System.out.println("No se actualizo....");
			e.printStackTrace();
		}
		
		return "index";
	}
	
	@PostMapping("/eliminar")
	public String eliminarCliente(@RequestBody Cliente cliente) {
		
		try {
			servicioCliente.borrarCliente(cliente);
			System.out.println("Actualizado con exito!!!");
		} catch (Exception e) {
			System.out.println("No se actualizo....");
			e.printStackTrace();
		}
		
		return "index";
	}
	
	@PostMapping("/baja")
	public String bajaCliente(@RequestBody Cliente cliente) {
		
		try {
			servicioCliente.bajaCliente(cliente);
			System.out.println("baja con exito!!!");
		} catch (Exception e) {
			System.out.println("No se actualizo....");
			e.printStackTrace();
		}
		
		return "index";
	}
	
	@PostMapping("/alta")
	public String altaCliente(@RequestBody Cliente cliente) {
		
		try {
			servicioCliente.altaCliente(cliente);
			System.out.println("alta con exito!!!");
		} catch (Exception e) {
			System.out.println("No se actualizo....");
			e.printStackTrace();
		}
		
		return "index.html";
	}
	
	*/
		
		
		
}
