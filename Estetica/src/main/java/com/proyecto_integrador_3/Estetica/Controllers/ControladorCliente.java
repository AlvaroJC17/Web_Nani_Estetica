package com.proyecto_integrador_3.Estetica.Controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCliente;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

import jakarta.persistence.EntityManager;

@Controller
//@RequestMapping(")
public class ControladorCliente {

	@Autowired
	public RepositorioCliente repositorioCliente;
	
	@Autowired
	public ServicioCliente servicioCliente;
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	
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
	
	//Devuelve la pagina homeCLiente con los datos del usuario que le pasemos por mmail
	@GetMapping("/homeCliente")
	public String homeCliente(String email, Model model) {
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosCliente", datosCliente);
		return "/pagina_cliente/homeCliente";	
		
	}
	
	//Muestra todos los datos personales de la persona en la pagina misdatosCliente
	@GetMapping("misdatosCliente")
	public String misdatosCliente(@RequestParam("email") String email, Model model) { // el valor del parametro email viene del html homeCliente
		List <Usuario> datosPersonaUsuario = servicioUsuario.buscarPorEmail(email);
		List <Usuario> dastosEnMayuscula = new ArrayList();
	
		
		model.addAttribute("datosCliente", datosPersonaUsuario);
	return "/pagina_cliente/misdatosCliente";	
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
