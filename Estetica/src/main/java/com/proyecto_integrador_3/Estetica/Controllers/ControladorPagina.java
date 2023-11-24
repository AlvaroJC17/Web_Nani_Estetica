package com.proyecto_integrador_3.Estetica.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ControladorPagina {

	@GetMapping("index")
	public String monstrarIndex() {
		return "index";
	}
	
	@GetMapping("homeCliente")
	public String homeCliente() {
	return "/pagina_cliente/homeCliente";	
	}
	
	@GetMapping("login")
	public String login() {
	return "login";	
	}
	
	@GetMapping("homeAdmin")
	public String homeAdmin() {
	return "/pagina_admin/homeAdmin";	
	}
	
	@GetMapping("portalAdmin")
	public String portalAdmin() {
	return "/pagina_admin/portalAdmin";	
	}
	
	
}
