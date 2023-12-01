package com.proyecto_integrador_3.Estetica.Controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioPersona;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

@Controller
public class ControladorPersona {

	@Autowired
	public ServicioPersona servicioPersona;
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	@Autowired
	public RepositorioUsuario repositorioUsuario;
	
	@GetMapping("completarDatos")
	public String completarDatos() {
		return "completarDatos";
	}
	
	@PostMapping("guardarDatosPersona")
	public String guardarDatosPersona(
			@RequestParam(name = "nombre") String nombre,
			@RequestParam(name = "apellido") String apellido,
			@RequestParam(name = "numeroDoc") String dni,
			@RequestParam(name = "sexo") String sexo,
			@RequestParam(name = "nacimiento") String fechaNacimiento,
			@RequestParam(name = "telefono", required = false) Integer telefono,
			@RequestParam(name = "direccion") String direccion,
			@RequestParam(name = "ocupacion") String ocupacion,
			@RequestParam(name = "emailUsuario") String emailUsuario, //Esta valor viene del input oculto de la hoja completarDatos, que a su vez viene del meotodo Login en ControladorPagina
			ModelMap model
			) throws MiExcepcion {
		
		 System.out.println("nombre: " + nombre);
		 System.out.println("apellido: " + apellido);
		 System.out.println("dni: " + dni);
		 System.out.println("sexo: " + sexo);
		 System.out.println("FechaNacimiento: " + fechaNacimiento);
		 System.out.println("telefono: " + telefono);
		 System.out.println("direccion: " + direccion);
		 System.out.println("ocupacion: " + ocupacion);
		 System.out.println("Email: " + emailUsuario);
		
		String idPersona = null;
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailUsuario);
		
		try {
			//Con este metodo obtenemos el id de la persona que se registro
			idPersona = servicioPersona.registrarPersona(dni, emailUsuario, nombre, apellido, ocupacion, direccion, telefono, fechaNacimiento, sexo);
					

			// Con este metodo buscamos al usuario que se logue a traves de email
			//Y los datos que lleno del form a traves de id
			//jpa crea un usario con el mismo dni de la persona que llena el form
			//Con este metodo asiganamos los valos del usario que se logue al mismo id
			// de la persona que lleno el form y borramos el usuario repetido
			servicioPersona.validarForm(emailUsuario, idPersona);
			
		
		} catch (MiExcepcion e) {
			System.out.println(e.getMessage());
			model.put("error", e.getMessage());
			return "completarDatos";
		}
		return "redirect:/homeCliente?email=" + emailUsuario; //redirecionamos al metodo homeCliente enviando la varibale mail
	}
}
		
		
	
	
	
	
