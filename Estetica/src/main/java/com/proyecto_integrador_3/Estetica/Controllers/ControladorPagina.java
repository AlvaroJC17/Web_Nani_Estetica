package com.proyecto_integrador_3.Estetica.Controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Entidades.CodigoDeVerificacion;
import com.proyecto_integrador_3.Estetica.Entidades.EmailUsuarios;
import com.proyecto_integrador_3.Estetica.Entidades.TokenUsuario;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.DiasDeLaSemana;
import com.proyecto_integrador_3.Estetica.Enums.Especialidad;
import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.TipoDeEspecialidad;
import com.proyecto_integrador_3.Estetica.Enums.TratamientoEnum;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCodigoDeVerificacion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioToken;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCodigoDeVerificacion;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioEmail;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioHorario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioToken;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

@Controller
public class ControladorPagina {
	
	@Autowired
	public RepositorioUsuario repositorioUsuario;
	
	@Autowired
	public RepositorioToken repositorioToken;
	
	@Autowired
	public RepositorioCodigoDeVerificacion repositorioCodigoDeVerificacion;
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	@Autowired
	public ServicioCodigoDeVerificacion servicioCodigoDeVerificacion;
	
	@Autowired
	public ServicioEmail servicioEmail;
	
	@Autowired
	ServicioToken servicioToken;
	
	@Autowired
	ServicioHorario servicioHorario;
	
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/index")
	public String monstrarIndex() {
		return "index";
	}
	
	@GetMapping("/registrarse")
	public String registrarse(
			@RequestParam(required = false) String error,
			Model model) {
		
		if (error != null) {
			String error2 = "<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><p class='fs-6 text-center'>Ha superado el número máximo de intentos, por favor espere unos minutos y registrese nuevamente para "
					 + "generar un nuevo código de validación.</p>"; 
			model.addAttribute("error", error2);
			model.addAttribute("showModalError", true);	
		}
		
		return "registrarse";
	}
	
	@GetMapping("/olvidoContrasena")
	public String olvidoContrasena() {
	return "olvidocontrasena";	
	}
	
	@GetMapping("/validadorDeCodigo")
	public String validadorDeCodigo() {
		return "validadorDeCodigo";
	}
	
	//actualiza la contraseña del usuario
	@PostMapping("actualizarContrasenaUsuario")
	public String actualizarContrasenaCliente(
			@RequestParam String emailCliente, //Esta variable viene de un input oculto de la pag cambiarContrasenaCliente
			@RequestParam String idCliente, //Esta variable viene de un input oculto de la pag de la pag cambiarContrasenaCliente
			@RequestParam String oldPass, //A partir de estas viene del formulario
			@RequestParam String newPass,
			@RequestParam String repeatNewPass,
			ModelMap model) throws MiExcepcion {
		
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailCliente);
		
		try {
			servicioUsuario.modificarContrasena(idCliente,oldPass, newPass, repeatNewPass);
			String exito = "<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
					 +"<span class='fs-6'>Hemos actualizado correctamente tu contraseña. Ahora serás redireccionado a la página principal para que inicies"
					 + " sesión con tus nuevas credenciales.</span>";;
			model.addAttribute("datosCliente", datosCliente);
			model.addAttribute("showModalExito", true);
			model.addAttribute("exito", exito);
			return "cambiarContrasena";
		} catch (Exception e) {
			String error = e.getMessage();
	        model.addAttribute("datosCliente", datosCliente);
			model.addAttribute("showModalError", true);
			model.addAttribute("error", error);
			return "cambiarContrasena";
		}
		
	}
	
	//enviar el link para el cambio de contraseña al email del usuario
	@PostMapping("/enviarLinkCambioContrasena")
	public String enviarLinkCambioContrasena(
			@RequestParam String emailUsuario,
			Model model) throws MiExcepcion {
		
		Optional<Usuario> buscarUsuario;
		try {
			buscarUsuario = servicioUsuario.buscarPorEmailOptional(emailUsuario);
			if (buscarUsuario.isPresent()) {
			
				EmailUsuarios linkRestablecerContrasena = new EmailUsuarios();
				linkRestablecerContrasena.setAsunto("Nani estética - RESTABLECER CONTRASEÑA");
				linkRestablecerContrasena.setDestinatario("alvarocortesia@gmail.com");
				
				servicioEmail.enviarEmailUsuarioOlvidoContrasena(linkRestablecerContrasena, emailUsuario);
				
				String exito = "Se ha enviado un mensaje a su correo eléctronico para poder restablecer su contraseña";
				model.addAttribute("exito", exito);
				model.addAttribute("showModalExito", true);
				return "olvidocontrasena";
			}else {
				String error = "<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
						 +"<span class='fs-6'>El correo eléctronico no se encuentra registrado."
						 + " Por favor verifique e intente nuevamente.</span>";
				model.addAttribute("error", error);
				model.addAttribute("showModalError", true);
				return "olvidocontrasena";
			}
		} catch (MiExcepcion e) {
			
			String error = "<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
					 +"<span class='fs-6'>Hubo un error al enviar el correo eléctronico, por favor intente más tarde";
			model.addAttribute("error", error);
			model.addAttribute("showModalError", true);
			return "olvidocontrasena";
		}
	}
	
	//Cuando ingresa al link de cambio de contraseña, valida si el token esta activo y redirecciona a la pagina para poder cambiar la contrasena
	@GetMapping("/cambiarContrasena")
	public String cambiarContrasena(
			@RequestParam(required = false) String token,
			@RequestParam(required = false) String exito,
			@RequestParam(required = false) String error,
			ModelMap model) {
		
		//valida que el token este activo
		 if (servicioToken.validarToken(token)) {
			 
			 Optional <TokenUsuario> obetnerValoresDelToken = repositorioToken.findByToken(token);
			 String emailAsociadoAlToken = null;
			 if (obetnerValoresDelToken.isPresent()) {
				TokenUsuario datosDelToken = obetnerValoresDelToken.get();
				emailAsociadoAlToken = datosDelToken.getEmailUsuario();
			}
	            // Token válido, mostrar formulario de restablecimiento
			 List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailAsociadoAlToken);
	            model.addAttribute("datosCliente", datosCliente);
	    		model.addAttribute("exito", exito);
	    		return "cambiarContrasena";
	            
	        } else {
	            error = "El enlace ha expirado, por favor generar otro";
	            model.addAttribute("error", error);
	            model.addAttribute("showModalError", true);
	            return "olvidocontrasena";
	        }
	}
			
	//Validar el codigo y  guardar a los usuarios en la tabla
	@PostMapping("/validarCodigoYRegistrar")
	public String validarCodigoYRegistrar(
			@RequestParam String idUsuarioNoValidado,
			@RequestParam String codigoUsuario,
			@RequestParam String email,
			@RequestParam(name="contrasena") String password,
			@RequestParam(name="contrasena2") String password2,
			@RequestParam String fechaNacimiento,
			@RequestParam String fechaExpiracionCodigo,
			Model model) throws MiExcepcion {
		
		//evitamos que el usuario deje espacios en blanco al inicio o final del string
		String codigoUsuarioSinEspacios = codigoUsuario.trim();
			
			//Llamamos al metodo para verificar el codigo y guardamos el resultado booleano en una variable
		Boolean isValido = servicioCodigoDeVerificacion.validarCodigoIngresado(codigoUsuarioSinEspacios);
		
		
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
	            model.addAttribute("error", error);
	        	model.addAttribute("showModalError", true);
	            return "registrarse";
			}
        	
			//Sino es valido lanzamos un error
		}else {	
			
			try {
				
				//Sumamos y obtenemos el numero de intentos del usuario a traves del email
				int numeroDeIntentos = servicioUsuario.validarIntentosVerificacion(email);
				
				//Mientras los intentos sean menores a 4 seguira mostrando este mensaje de error, luego del 4to intento pasara al else
				if (numeroDeIntentos <= 3) {
					
					LocalDateTime pasearFecha = servicioHorario.pasarFechaStringToLocalDateTime(fechaExpiracionCodigo);
					
					Boolean conteoRegresivo = true; //no muestra el conteo regresivo si hay algun error al ingresar el codigo
					String error = "<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
							 +"<p class='fs-6 text-center'>El codigo ingresado no es valido. Intentos  " + numeroDeIntentos + "/3 </p>" ;
					Boolean mostrarEnlace = false;
					model.addAttribute("error", error);
					model.addAttribute("mostrarEnlace",mostrarEnlace);
					model.addAttribute("conteoRegresivo", conteoRegresivo);
					model.addAttribute("expiracionCodigo", pasearFecha);
					model.addAttribute("idUsuarioNoValidado", idUsuarioNoValidado);
					model.addAttribute("email", email);
					model.addAttribute("contrasena", password);
					model.addAttribute("contrasena2", password2);
					model.addAttribute("fechaNacimiento", fechaNacimiento);
		        	model.addAttribute("showModalError", true);
					return "validadorDeCodigo";
				}else {
					
					String error = null;
					//Bloqueamos al usuario porque supero el numero de intentos
					servicioUsuario.bloquearUsuarioPorIntentosValidacion(email);
					
					//Este metodo solo genera true si el usuario esta bloqueado
					if (servicioUsuario.verificarYDesbloquearUsuarioValidacion(email)) {
						   error= "<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><p class='fs-6 text-center'>Ha superado el numero máximo de intentos, por favor espere unos minutos y registrese nuevamente para "
									 + "generar un nuevo código de validación.</p>";
					}else {
					     error = "<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
								 +"<span class='fs-6'>Genere un nuevo código.</span>";
					}
					
					return "redirect:/registrarse?error=" + error; 
		        	
				}
				
				
			} catch (MiExcepcion e) {
				String error = e.getMessage();
				Boolean conteoRegresivo = false; //no muestra el conteo regresivo si hay algun error al ingresar el codigo 
				Boolean mostrarEnlace = true;
				model.addAttribute("mostrarEnlace",mostrarEnlace);
				model.addAttribute("idUsuarioNoValidado", idUsuarioNoValidado);
				model.addAttribute("email", email);
				model.addAttribute("contrasena", password);
				model.addAttribute("contrasena2", password2);
				model.addAttribute("fechaNacimiento", fechaNacimiento);
				model.addAttribute("conteoRegresivo", conteoRegresivo);
				model.addAttribute("error", error);
	        	model.addAttribute("showModalError", true);
				return "validadorDeCodigo";
			}
		}
	}
			
			

	//Valida los datos del usuario al registrarse, genera un codigo y lo envia por email
	@PostMapping("/validarEmail")
	public String validarEmail(
			@RequestParam String email,
			@RequestParam String fechaNacimiento,
			@RequestParam(name="contrasena")String password,
			@RequestParam(name="contrasena2")String password2,
			@RequestParam(required = false)String accion,
			Model modelo) throws MiExcepcion {	
		
		try {
			
			if (servicioUsuario.verificarYDesbloquearUsuarioValidacion(email)) {				
				throw new MiExcepcion("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br>"
						+ "<p class= 'fs-6 text-center'> Ha superado el número de intentos máximo para validar el correo electrónico, por favor espera unos minutos e intente nuevamente.</p>");
			}
	
				//Validamos que el usuario esta registrado y con el email validado, sino es asi no entra en este condicional
			if (servicioUsuario.validarUsuarioRegistradoActivo(email)) {
				//Validamos que los datos ingresados sean correctos, sino tiramos una excepcion que sale por el catch
				servicioUsuario.validarDatosDelUsuario(email, password, password2, fechaNacimiento);
			}
				
			//creamos un usuario temporal para poder validar los intentos de validacion de codigo, si este usuario no completa su validacion sera borrado
			//por el metodo para limpiar la base da datos. El email que se guarda acá tampoco influye en los intentos para registrarse
			//Este metodo registra un nuevo usuario si no estaba registrado el email, caso contrario devuelve los datos del usuario registrado
			Usuario usuarioTemporal = servicioUsuario.guardarOEncontrarUsuarioTemporal(email, password, password2, fechaNacimiento);
			
			//Llamamos al metodo que genera el codigo y lo manda por email
			CodigoDeVerificacion idCodigo = servicioCodigoDeVerificacion.generarGuardarYEnviarCodigo(usuarioTemporal);
			
			LocalDateTime fechaExpiracionCodigo = null;
			Optional<CodigoDeVerificacion> buscarCodigo = repositorioCodigoDeVerificacion.findById(idCodigo.getId());
			if (buscarCodigo.isPresent()) {
				CodigoDeVerificacion codigo = buscarCodigo.get();
				fechaExpiracionCodigo = codigo.getExpiracion();
			}else {
				throw new MiExcepcion("No se encontraron codigos registrados");
			}
			
			
			Boolean conteoRegresivo = true; //Si el codigo se envia bien, entonces creamos esta variable para mostrar el contador en el html
			Boolean mostrarEnlace = false;		
			//Retornamos la plantilla donde se va a ingresar el codigo
			modelo.addAttribute("email",email);
			modelo.addAttribute("contrasena",password);
			modelo.addAttribute("contrasena2",password2);
			modelo.addAttribute("fechaNacimiento",fechaNacimiento);
			modelo.addAttribute("conteoRegresivo", conteoRegresivo);
			modelo.addAttribute("mostrarEnlace", mostrarEnlace);
			modelo.addAttribute("idUsuarioNoValidado", usuarioTemporal.getId());
			modelo.addAttribute("expiracionCodigo", fechaExpiracionCodigo);
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
		         @RequestParam String email, //El valor de estas variables proviene del form login
		         @RequestParam(name = "password") String contrasena,
		         ModelMap model, Model modelo) throws MiExcepcion {
			
			String emailUsuarioSinEspacios = email.trim();
			String passwordSinEspacios = contrasena.trim();
			
			//VAlidamos que la casilla de mail no esta vacia
			try {
				servicioUsuario.validarEmail(emailUsuarioSinEspacios);
			} catch (MiExcepcion e) {
				String error = e.getMessage();
				model.addAttribute("error", error);
				modelo.addAttribute("showModalError", true);
				return "login";
			}
			
			//validamos si es un usuario temporal o real a traves de su email, si su email esta validado es un usuario real sino es temporal
			//Si es temporal tiramos una excepcion porque los usuarios temporales no pueden loguearse
			if (!servicioUsuario.validarUsuarioRegistradoActivo(emailUsuarioSinEspacios)) {
				String error = "<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br>"
   					 +"<p class='fs-6 text-center'>La cuenta no se encuentra registrada en nuestro sistema, por favor registrate para poder "
   					 + "usar nuestros servicios.</p>";
				
				model.addAttribute("error", error);
				modelo.addAttribute("showModalError", true);
				modelo.addAttribute("email", emailUsuarioSinEspacios);
				return "login";
			}
			
			
			//Este metodo solo devuelve true si el usuario esta bloqueado, tambien verifica si ya pasaron los 5 min minutos de bloqueo y si es asi lo desbloquea y
			// devuelve false, si el usuario no estaba bloqueado tambien devuelve false
			if (servicioUsuario.verificarYDesbloquearUsuarioLogin(emailUsuarioSinEspacios)) {
				String error = "<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br>"
	   					 +"<p class='fs-6 text-center'>Ha superado en número máximo de intentos para iniciar sesión, por favor espera unos minutos e intente nuevamente.</p>";
					
					model.addAttribute("error", error);
					modelo.addAttribute("showModalError", true);
					modelo.addAttribute("email", emailUsuarioSinEspacios);
					return "login";
			}
			
				
			//Validamos con el mail ingresado si el usuario existe en la base de datos
			// Si existe entonces le buscamos todos los valores de las variables mostradas abajo
			Usuario usuario;
			String emailUsuario = null;
			String passwordRegistrado = null;
			Boolean activo;
			Boolean validarForm = null;
			Rol rol = null;
		     Optional<Usuario> usuarioOptional = servicioUsuario.buscarPorEmailOptional(emailUsuarioSinEspacios);
		     if (usuarioOptional.isPresent()) {		    	 
		         usuario = usuarioOptional.get(); //con el metodo get, asociamos al usuario encontrado a la variable usuario y asi poder acceder a sus atributos
		         emailUsuario = usuario.getEmail();
		         passwordRegistrado = usuario.getContrasena();
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
						modelo.addAttribute("email", emailUsuarioSinEspacios);
						return "login";
					}
			     
		     // Si usuario no existe en la base de datos, tiramos el error y el mensaje    
		     }else {
		    	 String error3 = "<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
    					 +"<span class='fs-6'>La contraseña o el nombre de usuario que has ingresado son incorrectos."
    					 + "  Por favor, verifica tus datos e intenta nuevamente.</span><br><br>"
    					 + "Gracias.";
		    	 modelo.addAttribute("error", error3);
		    	 modelo.addAttribute("email", emailUsuarioSinEspacios);
		    	 modelo.addAttribute("showModalError", true);
		     }

		     
		         try {
		        	// validamos que las contraseña ingresada coincida con la registrada en la base de datos
		        	  if (servicioUsuario.verificarPasswordLogin(passwordSinEspacios, passwordRegistrado)) {
		        		  
		        		  //Servicio para limpiar todos los valores relacionados al bloqueo por login
		        		  servicioUsuario.blanquearBloqueoLogin(emailUsuario);
		        		  
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
				            		 modelo.addAttribute("especialidad", Especialidad.values());
				            		 modelo.addAttribute("tipoEspecialidad", TipoDeEspecialidad.values());
				            		 modelo.addAttribute("tratamiento", TratamientoEnum.values());
				            		 modelo.addAttribute("DiasDeLaSemana", DiasDeLaSemana.values());
				            		 return "pagina_profesional/completarDatosProfesional";
				            	 }
				             }
		        	  }
		        	  //En caso que la contraseña ingresada no sea igual a la guardada, tiramos exta excepcion con un mensaje
		         } catch (MiExcepcion e) {
		        	 int intentosLogin = servicioUsuario.validarIntentosLogin(emailUsuario);
		        	 
		        	 if (intentosLogin >= 3) {
						servicioUsuario.bloquearUsuarioPorIntentosLogin(emailUsuario);
					}
		        	 String error2 = e.getMessage();
		        	 modelo.addAttribute("error", error2);
		        	 modelo.addAttribute("email", emailUsuarioSinEspacios);
			         modelo.addAttribute("showModalError", true);
			         return "login";
		         }
		     return "login";
		}
				            	
				                
		       
		    	


}