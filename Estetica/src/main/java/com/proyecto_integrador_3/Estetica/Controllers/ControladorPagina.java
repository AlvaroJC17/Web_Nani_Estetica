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
	
	
	// Registro un usuario en la base de datos
	 @PostMapping("/registro")
	    public String registroUsuario(@RequestParam(name = "email") String email, @RequestParam(name = "contrasena") String password,
	           @RequestParam(name = "contrasena2") String password2, ModelMap modelo) throws MiExcepcion {

		
	        try {

	        	servicioUsuario.guardarUsuario(email, password, password2);
	        	return "index.html";
	        
	        } catch (MiExcepcion e) {
	            System.out.println(e.getMessage());
	            modelo.put("error", e.getMessage());
	            return "registrarse";
	        }

	    }
	 
	 	//Login de un usuario, dependiendo de su rol lo redireccion a un home y si es logueo
	 	// por primera vez lo redirecciona a una formulario de datos
		@PostMapping("/loginUsuario")
		 public String loginUsuario(
		         @RequestParam(name = "email") String email,
		         @RequestParam(name = "password") String contrasena,
		         ModelMap model, Model modelo) throws MiExcepcion {

			 String password = null;
		     Optional<Usuario> usuarioOptional = servicioUsuario.buscarPorEmailOptional(email);

		     if (usuarioOptional.isPresent()) {
		    	 
		         Usuario usuario = usuarioOptional.get(); //con el metodo get, asociamos al usuario encontrado a la variable usuario y asi poder acceder a sus atributos
		         String emailUsuario = usuario.getEmail();
		         password = usuario.getContrasena();
		         Boolean activo = usuario.getActivo();
		         Boolean validarForm = usuario.getValidacionForm();
		         Rol rol = usuario.getRol();
		         
		         if (!activo) {
						model.addAttribute("Error", "Usuario no se encuentra activo");
						return "login";
					}
			         
		      
		         try {
		        	 //verificamos que los password esten correcto y sean iguales ademas de que el usuario este activo
		        	  if (servicioUsuario.verificarPassword(contrasena, password)) {
		        		  
		        		  //Buscamos los datos pertenecientes a ese email en la table de persona y los pasamos a homeAdmin, en este caso solo usamos el nombre
		        		  List <Usuario> datosPersonaUsuario = servicioUsuario.buscarPorEmail(emailUsuario);
		        		  
				             if ("ADMIN".equals(rol.toString())) {
				       
				            	 if (validarForm) {
				            		 modelo.addAttribute("datosAdmin", datosPersonaUsuario);
				            		 return "pagina_admin/homeAdmin";
				            	 }else if(!validarForm) {
				            		 modelo.addAttribute("emailUsuario", emailUsuario);
				            		 return "completarDatos";
				            	 }
									
				            	 
				             } else if ("CLIENTE".equals(rol.toString())) {
				            	
				            	 if (validarForm) {
				            		 modelo.addAttribute("datosCliente", datosPersonaUsuario);
				            		 return "pagina_cliente/homeCliente";
				            	 }else if(!validarForm) {
				            		 modelo.addAttribute("emailUsuario", emailUsuario); //Esta variable envia el email en un input oculto hacia el metodo guardarDatosPersona
				            		 return "completarDatos";
				            	 }
				            	 
				             } else if ("PROFESIONAL".equals(rol.toString())) {
				            	 
				            	 if (validarForm) {
				            		 modelo.addAttribute("datosProfesional", datosPersonaUsuario);
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
		    	 model.put("Error", "Usuario no se encuentra registrado");
		     }
		     return "login";
		}
				            	
				                
		       
		    	


}