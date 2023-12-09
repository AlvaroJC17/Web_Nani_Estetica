package com.proyecto_integrador_3.Estetica.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioProfesional;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

@Controller
public class ControladorProfesional {
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	@Autowired
	public ServicioProfesional servicioProfesional;

	
	@GetMapping("cambiarContrasenaProfesional")
	public String cambiarContrasenaProfesional() {
	return "/pagina_admin/cambiarContrasenaProfesional";	
	}
	
	@GetMapping("/homeProfesional")
	public String homeProfesional(@RequestParam(name = "email") String email, Model model) {
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosProfesional", datosProfesional);
		return "/pagina_profesional/homeProfesional";	
	}

	@GetMapping("/misdatosProfesional")
	public String misdatosProfesional(@RequestParam(name = "email") String email, ModelMap model) {
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosProfesional", datosProfesional);
		return "/pagina_profesional/misdatosProfesional";	
	}
	
	@PostMapping("/guardarDatosProfesional")
	public String guardarDatosProfesional(
			@RequestParam(name = "nombre") String nombre,
			@RequestParam(name = "apellido") String apellido,
			@RequestParam(name = "numeroDoc") String dni,
			@RequestParam(name = "matricula") String matricula,
			@RequestParam(name = "especialidad") String especialidad,
			@RequestParam(name = "sexo") String sexo,
			@RequestParam(name = "nacimiento") String fechaNacimiento,
			@RequestParam(name = "telefono", required = false) Integer telefono,
			@RequestParam(name = "direccion") String direccion,
			@RequestParam(name = "emailUsuario") String emailUsuario, //Esta valor viene del input oculto de la hoja completarDatos, que a su vez viene del meotodo Login en ControladorPagina
			ModelMap model
			) throws MiExcepcion {
		
		try {
			
			//Guardamos los datos del formulario que lleno el nuevo cliente
			servicioProfesional.registrarProfesional(emailUsuario, matricula, especialidad, dni, nombre, apellido, direccion, telefono, fechaNacimiento, sexo);
				
		} catch (MiExcepcion e) {
			System.out.println(e.getMessage());
			model.put("error", e.getMessage());
			return "pagina_profesional/completarDatosProfesional";
		}
		return "redirect:/homeProfesional?email=" + emailUsuario; //redirecionamos al metodo homeProfesional enviando la varibale mail
	}

}
