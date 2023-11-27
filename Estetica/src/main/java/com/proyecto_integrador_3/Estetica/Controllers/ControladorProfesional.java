package com.proyecto_integrador_3.Estetica.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorProfesional {

	@GetMapping("homeProfesional")
	public String homeProfesional() {
	return "/pagina_admin/homeProfesional";	
	}
	
	@GetMapping("misdatosProfesional")
	public String misdatosProfesional() {
	return "/pagina_admin/misdatosProfesional";	
	}
	
	@GetMapping("cambiarContrasenaProfesional")
	public String cambiarContrasenaProfesional() {
	return "/pagina_admin/cambiarContrasenaProfesional";	
	}
}
