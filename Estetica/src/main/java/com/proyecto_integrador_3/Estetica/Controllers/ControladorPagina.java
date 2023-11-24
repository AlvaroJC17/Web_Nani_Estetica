package com.proyecto_integrador_3.Estetica.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

@Controller
public class ControladorPagina {
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	 @PostMapping("/registro")
	    public String registroUsuario(@RequestParam(name = "email") String email, @RequestParam(name = "contrasena") String password,
	           @RequestParam(name = "contrasena2") String password2, ModelMap modelo) throws MiExcepcion {

		 System.out.println("EMAIL: " + email + "\n" + "PASS: " + password + "\n" + "PASS2: " + password2);
	        try {

	        	servicioUsuario.guardarUsuario(email, password, password2);

	            modelo.put("exito", "Usuario registrado correctamente!");

	            return "index.html";
	        } catch (MiExcepcion e) {
	            System.out.println(e.getMessage());
	            modelo.put("error", e.getMessage());
	            modelo.put("email", email);
	            return "index.html";
	        }

	    }
	 @GetMapping("registrarse")
		public String monstrarRegistrarse() {
			return "registrarse";
		}

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
