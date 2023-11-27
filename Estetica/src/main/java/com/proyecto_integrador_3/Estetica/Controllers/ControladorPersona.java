package com.proyecto_integrador_3.Estetica.Controllers;

import java.util.Date;
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
			@RequestParam(name = "telefono") Integer telefono,
			@RequestParam(name = "direccion") String direccion,
			@RequestParam(name = "ocupacion") String ocupacion,
			@RequestParam(name = "emailUsuario") String emailUsuario,
			ModelMap model
			) {
		
		System.out.println("NAME: " + nombre + "\n" + "APELLIDO: " + apellido + "\n" 
		+ "NUMERODOC: " + dni + "\n" + "SEXO: " + sexo + "\n" + "FECHA: " + fechaNacimiento
		+ "\n" + "TELEFONO: " + telefono + "\n" + "DIRECCION: " + direccion + "\n" 
		+ "OCUPACION: " + ocupacion + "\n" + "EMAILUSUARIO: " + emailUsuario);
		
		String idPersona = null;
		
		try {
			idPersona = servicioPersona.registrarPersona(dni, direccion, nombre, apellido, ocupacion, ocupacion, telefono, fechaNacimiento, sexo);
			System.out.println("IDPERSONA: " + idPersona);
			servicioPersona.validarForm(emailUsuario, idPersona);
			model.put("exito", "GUARDADO CORRECTAMENTE");
		
		} catch (MiExcepcion e) {
			System.out.println("NO SE REGISTRO");
			model.put("error", "NO SE REGISTRO");
			e.printStackTrace();
		}
		
		
		return"completarDatos";
	}
	
	
	
	
}
