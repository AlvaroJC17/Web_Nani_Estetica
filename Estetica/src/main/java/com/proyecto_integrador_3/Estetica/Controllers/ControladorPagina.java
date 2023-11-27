package com.proyecto_integrador_3.Estetica.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

@Controller
public class ControladorPagina {
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	@GetMapping("registrarse")
	public String monstrarRegistrarse() {
		return "registrarse";
	}
	
	@GetMapping("index")
	public String monstrarIndex() {
		return "index";
	}
	
	@GetMapping("login")
	public String login() {
	return "login";	
	}
	
	@GetMapping("olvidoContrasena")
	public String olvidoContrasena() {
	return "olvidocontrasena";	
	}
	
	@GetMapping("cambiarContrasena")
	public String cambiarContrasena() {
	return "cambiarContrasena";	
	}
	
	 @PostMapping("/registro")
	    public String registroUsuario(@RequestParam(name = "email") String email, @RequestParam(name = "contrasena") String password,
	           @RequestParam(name = "contrasena2") String password2, ModelMap modelo) throws MiExcepcion {

		
	        try {

	        	servicioUsuario.guardarUsuario(email, password, password2);
	        
	            return "index.html";
	        } catch (MiExcepcion e) {
	            System.out.println(e.getMessage());
	            modelo.put("error", e.getMessage());
	            modelo.put("email", email);
	            return "registrarse";
	        }

	    }
	 
		@PostMapping("/loginUsuario")
		 public String loginUsuario(
		         @RequestParam(name = "email") String email,
		         @RequestParam(name = "password") String contrasena,
		         ModelMap model, Model modelo) throws MiExcepcion {

			 String password = null;
		     Optional<Usuario> usuarioOptional = servicioUsuario.buscarPorEmailOptional(email);

		     if (usuarioOptional.isPresent()) {
		    	 
		         Usuario usuario = usuarioOptional.get();
		         String emailUsuario = usuario.getEmail();
		         password = usuario.getContrasena();
		         Boolean activo = usuario.getActivo();
		         Boolean validarForm = usuario.getValidacionForm();
		         Rol rol = usuario.getRol();
		         
		         try {
		        	  if (servicioUsuario.verificarPassword(contrasena, password) && activo) {
		        		  				        	 
				             if ("ADMIN".equals(rol.toString())) {
				       
				            	 if (validarForm) {
				            		 modelo.addAttribute("datosAdmin", usuarioOptional);
				            		 return "pagina_admin/homeAdmin";
				            	 }else if(!validarForm) {
				            		 modelo.addAttribute("emailUsuario", emailUsuario);
				            		 return "completarDatos";
				            	 }
									
				            	 
				             } else if ("CLIENTE".equals(rol.toString())) {
				            	
				            	 if (validarForm) {
				            		 modelo.addAttribute("datosCliente", usuarioOptional);
				            		 return "pagina_cliente/homeCliente";
				            	 }else if(!validarForm) {
				            		 modelo.addAttribute("emailUsuario", emailUsuario);
				            		 return "completarDatos";
				            	 }
				            	 
				             } else if ("PROFESIONAL".equals(rol.toString())) {
				            	 
				            	 if (validarForm) {
				            		 modelo.addAttribute("datosProfesional", usuarioOptional);
				            		 return "pagina_profesional/homeProfesional";
				            	 }else if(!validarForm) {
				            		 modelo.addAttribute("emailUsuario", emailUsuario);
				            		 return "pagina_profesional/completarDatosProfesional";
				            	 }
				             }
		        	  }
		         } catch (Exception e) {
		        	 model.put("Error", "Usuario o contraseña incorrecta");
		         }
		     }else {
		    	 model.put("Error", "Usuario o contraseña incorrecta");
		     }
		     return "login";
		}
				            	
				                
		       
		    	


}