package com.proyecto_integrador_3.Estetica.Controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCodigoDeVerificacion;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

@Controller
public class ControladorPagina {
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	@Autowired
	public ServicioCodigoDeVerificacion servicioCodigoDeVerificacion;
	
	
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
	
	@GetMapping("/validadorDeCodigo")
	public String validadorDeCodigo() {
		return "validadorDeCodigo";
	}
	
	
	//Validar el codigo y  guardar a los usuarios en la tabla
	@PostMapping("/validarCodigoYRegistrar")
	public String validarCodigoYRegistrar(
			@RequestParam(name="idUsuarioNoValidado") String idUsuarioNoValidado,
			@RequestParam(name="codigoUsuario") String CodigoUsuario,
			@RequestParam(name="email") String email,
			@RequestParam(name="contrasena") String password,
			@RequestParam(name="contrasena2") String password2,
			@RequestParam(name="fechaNacimiento") String fechaNacimiento,
			Model model) {
		
		//evitamos que el usuario deje espacios en blanco al inicio o final del string
		String codigoUsuarioSinEspacios = CodigoUsuario.trim();
		
		//Usamos un opcional y el id del usuario para buscar el mismo usuario pero en forma de objeto
		Optional <Usuario> usuarioNoValidado = servicioUsuario.buscarPorIdOptional(idUsuarioNoValidado);
		if (usuarioNoValidado.isPresent()) {
			Usuario usuarioSinValidar = usuarioNoValidado.get();
			usuarioSinValidar.getId();
			
			//Llamamos al metodo para verificar el codigo y guardamos el resultado booleano en una variable
		Boolean isValido = servicioCodigoDeVerificacion.validarCodigoIngresado(codigoUsuarioSinEspacios, usuarioSinValidar);
		
		//Si el codigo es valido, llamamos al metodo que guarda a los usuarios en la tabla
		if (isValido) {
			
			try {
				servicioUsuario.guardarUsuario(email, password, password2, fechaNacimiento, idUsuarioNoValidado);
				String exito = "<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
						 +"<span class='fs-6'>El correo eléctronico ha sido validado exitosamente."
						 + " Por favor inicie sesion en su cuenta para poder disfrutar de nuestros servicios.</span>";
	        	model.addAttribute("exito", exito);
	        	model.addAttribute("showModalExito", true);
	        	return "login";
			} catch (MiExcepcion e) {
				
				String error = e.getMessage();
				System.out.println("Error al registrar el usuario");
	            model.addAttribute("error", error);
	        	model.addAttribute("showModalError", true);
	            return "registrarse";
			}
        	
			//Sino es valido lanzamos un error
		}else {			
			String error = "<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
					 +"<span class='fs-6'>El codigo ingresado no es valido."
					 + " Por favor intente nuevamente o genere un nuevo codigo.</span>";
			Boolean mostrarEnlace = true;
			model.addAttribute("mostrarEnlace",mostrarEnlace);
			model.addAttribute("idUsuarioNoValidado", idUsuarioNoValidado);
			model.addAttribute("email", email);
			model.addAttribute("contrasena", password);
			model.addAttribute("contrasena2", password2);
			model.addAttribute("fechaNacimiento", fechaNacimiento);
			model.addAttribute("error", error);
        	model.addAttribute("showModalError", true);
			return "validadorDeCodigo";
		}
		
		}else {
			System.out.println("Usuario sin validar no encontrado");
			return "registrarse";
		}
		
	}
	
	
	//Valida los datos del usuario al registrarse, genera un codigo y lo envia por email
	@PostMapping("/validarEmail")
	public String validarEmail(
			@RequestParam(name="email")String email,
			@RequestParam(name="fechaNacimiento")String fechaNacimiento,
			@RequestParam(name="contrasena")String password,
			@RequestParam(name="contrasena2")String password2,
			@RequestParam(required = false)String accion,
			Model modelo) throws MiExcepcion {	
		
		System.out.println("valor de accion: " + accion);
		try {
			//Validamos que los datos ingresados sean correctos, sino tiramos una excepcion que sale por el catch
			servicioUsuario.validarDatosDelUsuario(email, password, password2, fechaNacimiento);
			
			//Buscamos la fecha actual para registrar cuando se creo el usuario
			//esto sirve para despues limpiar registros imcompletos viejos
			LocalDateTime fechaActual = LocalDateTime.now();
			
			//Creamos una instancia de Usuario para pasar su id al metodo, en este punto el id
			//es el unico valor disponible en la entidad usuario por que se crea de forma dinamica
			//al instanciarla
			Usuario usuarioNoValidado = new Usuario();
			usuarioNoValidado.setFechaCreacion(fechaActual);
			
			//Llamamos al metodo que genera el codigo y lo manda por email
			servicioCodigoDeVerificacion.generarGuardarYEnviarCodigo(usuarioNoValidado, email);
			
			//Retornamos la plantilla donde se va a ingresar el codigo
			
			modelo.addAttribute("email",email);
			modelo.addAttribute("contrasena",password);
			modelo.addAttribute("contrasena2",password2);
			modelo.addAttribute("fechaNacimiento",fechaNacimiento);
			modelo.addAttribute("idUsuarioNoValidado", usuarioNoValidado.getId());
			if (accion!=null && accion.equals("reenviarCodigo")) {
				String exito = "El código ha sido enviado exitosamente a su correo eléctronico";
				modelo.addAttribute("exito", exito);
			}
			return "validadorDeCodigo";
			
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
		         String error1 = "<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
    					 +"<span class='fs-6'>Tu cuenta no se encuentra activa para utilizar nuestros servicios."
    					 + " Por favor, ponte en contacto con nuestro soporte técnico para resolver este problema.</span><br><br>"
    					 + "Gracias.";
		         if (!activo) {
						model.addAttribute("error", error1);
						modelo.addAttribute("showModalError", true);
						modelo.addAttribute("email", email);
						return "login";
					}
			     
		     // Si usuario no existe en la base de datos, tiramos el error y el mensaje    
		     }else {
		    	 String error3 = "<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
    					 +"<span class='fs-6'>La contraseña o el nombre de usuario que has ingresado son incorrectos."
    					 + "  Por favor, verifica tus datos e intenta nuevamente.</span><br><br>"
    					 + "Gracias.";
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