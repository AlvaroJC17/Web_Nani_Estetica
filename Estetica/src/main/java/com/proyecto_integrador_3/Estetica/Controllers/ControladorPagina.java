package com.proyecto_integrador_3.Estetica.Controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
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
import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

@Controller
public class ControladorPagina {
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/index")
	public String monstrarIndex() {
		return "index";
	}
	
	@GetMapping("/registrarse")
	public String registrarse() {
		return "registrarse";
	}
	
	@GetMapping("/olvidoContrasena")
	public String olvidoContrasena() {
	return "olvidocontrasena";	
	}
	
	@GetMapping("/cambiarContrasena")
	public String cambiarContrasena() {
	return "cambiarContrasena";	
	}
	
	
	// Registro un usuario en la base de datos
	 @PostMapping("/registro")
	    public String registroUsuario(
	    		@RequestParam(name = "email") String email,
	    		@RequestParam(name = "fechaNacimiento", required = false) String fechaNacimiento, // no lo colocamos requerido para poder manejar la excepcion
	    		@RequestParam(name = "contrasena") String password,
	    		@RequestParam(name = "contrasena2") String password2,
	    		ModelMap modelo) throws MiExcepcion {

		 try {
	        	servicioUsuario.guardarUsuario(email, password, password2, fechaNacimiento );
	        	String exito = "¡Gracias por registrarte!, ahora inicia sesión y completa tus datos personales para poder usar tu cuenta";
	        	modelo.addAttribute("exito", exito);
	        	modelo.addAttribute("showModalExito", true);
	        	return "registrarse";
	        } catch (MiExcepcion e) {
	        	String error = e.getMessage();
	        	modelo.addAttribute("email", email);
	            modelo.addAttribute("error", error);
	        	modelo.addAttribute("showModalError", true);
	            return "registrarse";
	        }
	 }
	
	 	//Login de un usuario, dependiendo de su rol lo redirecciona a un home determinado y si es logueo
	 	// por primera vez lo redirecciona a una formulario para completar los datos
		@PostMapping("/loginUsuario")
		 public String loginUsuario(
		         @RequestParam(name = "email") String email, //El valor de estas variables proviene del form login
		         @RequestParam(name = "password") String contrasena,
		         ModelMap model, Model modelo) throws MiExcepcion {
			
			
			//VAlidamos que la casilla de mail no esta vacia
			try {
				servicioUsuario.validarEmail(email);
			} catch (MiExcepcion e) {
				String error = e.getMessage();
				model.addAttribute("error", error);
				modelo.addAttribute("showModalError", true);
				return "login";
			}
				
			//Validamos con el mail ingresado si el usuario existe en la base de datos
			// Si existe entonces le buscamos todos los valores de las variables mostradas abajo
			Usuario usuario;
			String emailUsuario = null;
			String password = null;
			Boolean activo;
			Boolean validarForm = null;
			Rol rol = null;
		     Optional<Usuario> usuarioOptional = servicioUsuario.buscarPorEmailOptional(email);
		     if (usuarioOptional.isPresent()) {		    	 
		         usuario = usuarioOptional.get(); //con el metodo get, asociamos al usuario encontrado a la variable usuario y asi poder acceder a sus atributos
		         emailUsuario = usuario.getEmail();
		         password = usuario.getContrasena();
		         activo = usuario.getActivo();
		         validarForm = usuario.getValidacionForm();
		         rol = usuario.getRol();
		         
		         //Verificamos que el usuario este activo
		         String error1 = "Usuario no se encuentra activo";
		         if (!activo) {
						model.addAttribute("error", error1);
						modelo.addAttribute("showModalError", true);
						modelo.addAttribute("email", email);
						return "login";
					}
			     
		     // Si usuario no existe en la base de datos, tiramos el error y el mensaje    
		     }else {
		    	 String error3 = "Usuario o contraseña incorrectos";
		    	 modelo.addAttribute("error", error3);
		    	 modelo.addAttribute("email", email);
		    	 modelo.addAttribute("showModalError", true);
		     }

		     
		         try {
		        	// validamos que las contraseña ingresada coincida con la registrada en la base de datos
		        	  if (servicioUsuario.verificarPassword(contrasena, password)) {
		        		  
		        		  //Buscamos los datos pertenecientes a ese email en la tabla de persona y los pasamos a homeAdmin
		        		  //Dependiendo si es admin, profesional o cliente lo direccionamos a su respectivo home
		        		  List <Usuario> datosPersonaUsuario = servicioUsuario.buscarPorEmail(emailUsuario);
				             if ("ADMIN".equals(rol.toString())) {
				            	 if (validarForm) {
				            		 modelo.addAttribute("datosAdmin", datosPersonaUsuario);
				            		 return "pagina_admin/homeAdmin";
				            	 }else if(!validarForm) {
				            		 modelo.addAttribute("emailUsuario", emailUsuario);
				            		 return "pagina_admin/completarDatosAdmin";
				            	 }
				             } else if ("CLIENTE".equals(rol.toString())) {
				            	 if (validarForm) {
				            		 modelo.addAttribute("datosCliente", datosPersonaUsuario);
				            		 return "pagina_cliente/homeCliente";
				            	 }else if(!validarForm) {
				            		 modelo.addAttribute("emailUsuario", emailUsuario); //Esta variable envia el email en un input oculto hacia el metodo guardarDatosPersona
				            		 return "pagina_cliente/completarDatosCliente";
				            	 }
				             } else if ("PROFESIONAL".equals(rol.toString())) {				            	 
				            	 if (validarForm) {
				            		 modelo.addAttribute("datosProfesional", datosPersonaUsuario);
				            		 return "pagina_profesional/homeProfesional";
				            	 }else if(!validarForm) {
				            		 modelo.addAttribute("emailUsuario", emailUsuario);
				            		 modelo.addAttribute("provincias", Provincias.values());
				            		 return "pagina_profesional/completarDatosProfesional";
				            	 }
				             }
		        	  }
		        	  //En caso que la contraseña ingresada no sea igual a la guardada, tiramos exta excepcion con un mensaje
		         } catch (Exception e) {
		        	 String error2 = "Usuario o contraseña incorrecta";
		        	 modelo.addAttribute("error", error2);
		        	 modelo.addAttribute("email", email);
			         modelo.addAttribute("showModalError", true);
			         return "login";
		         }
		     return "login";
		}
				            	
				                
		       
		    	


}