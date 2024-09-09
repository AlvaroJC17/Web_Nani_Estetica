package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.CodigoDeVerificacion;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCodigoDeVerificacion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;

import jakarta.transaction.Transactional;

@Service
public class ServicioUsuario {
	
	  @Autowired
	  public RepositorioUsuario repositorioUsuario;
	  
	  @Autowired
	  public RepositorioCodigoDeVerificacion repositorioCodigoDeVerificacion;
	  
	  @Autowired
	  public ServicioHorario servicioHorario;
	  
	  
	  //La anotacion Scheduled indica que es un tarea programada y el cron indica en el momento que
	  //se va a realizar, en este caso es a las 12 de la noche
	  //y este metodo se encarga de borrar los registros imcompletos
	  //Es importante recordar tambien colocar la etiqueta @EnableScheduling en la pagina principal de la aplicacion, donde este el run
	  @Scheduled(cron = "0 0 0 * * ?") // Ejecuta todos los días a medianoche
	  //@Scheduled(cron = "0 * * * * *") // Ejecuta a cada minuto
	  @Transactional 
	  public void limpiarRegistrosIncompletos() throws MiExcepcion {
	        LocalDateTime limite = LocalDateTime.now().minusDays(1);
				
	        try {
	        //Obtenemos una lista de usuarios que no tengas el email validado y que la fecha sea anterior a 24hrs
	        List<Usuario> usuariosIncompletos = repositorioUsuario.findByEmailValidadoFalseAndFechaCreacionBefore(limite);
	        
	        //Con este for iteramos sobre los usuarios y obtenemos sus id
	        for (Usuario usuario : usuariosIncompletos) {
	            // Elimina en bloque todos los códigos asociados al usuario
	            List<CodigoDeVerificacion> listCodigos = repositorioCodigoDeVerificacion.findByUsuarioIdAndUsadoFalse(usuario.getId());
	            if (!listCodigos.isEmpty()) {
	                repositorioCodigoDeVerificacion.deleteAll(listCodigos); // Asegúrate de eliminar todos los códigos
	            }
	        }
	        System.out.println("!!!!!!!!!!!SE BORRARON TODOS LOS CODIGOS CON EXITO!!!!!!!!!");
	       
	        //Una vez que se borraron los codigos, ahora iteramos sobre los usuarios encontrados
	        //y los borramos
	     // Luego, elimina en bloque los usuarios
	        if (!usuariosIncompletos.isEmpty()) {
	            repositorioUsuario.deleteAll(usuariosIncompletos);
	        }
	        
	        System.out.println("¡¡¡¡¡¡¡¡¡¡¡¡¡SE BORRARON LOS USUARIOS CON EXITO!!!!!!!!!!!!");
	        
	        
	        } catch (Exception e) {
				throw new MiExcepcion("Error al conectar con el servidor e intentar borrar los registros incompletos " + e);
			}
	    }
	  
	  
	  public Boolean validarUsuarioBloqueadoLogin(String emailUsuario) throws MiExcepcion {
		  // Buscar usuario por email
	      Optional<Usuario> encontrarUsuario = repositorioUsuario.findByEmail(emailUsuario);
	      if (encontrarUsuario.isPresent()) {
			Usuario usuario = encontrarUsuario.get();
			
			if (usuario.getBloqueoLogin()) {
				return true;
			}else {
				return false;
			}
		}
	      //retorna false si el usuario no se encuentra
	      return false;
	  }
	  
	  @Transactional
	  public void bloquearUsuarioPorIntentosLogin(String emailUsuario) throws MiExcepcion {
	      // Buscar el usuario por email en la base de datos
	      Optional<Usuario> encontrarUsuario = repositorioUsuario.findByEmail(emailUsuario);

	      // Verificar si el usuario existe
	      if (encontrarUsuario.isPresent()) {
	          Usuario usuario = encontrarUsuario.get();
	         
	          // Verificar si el usuario ya está bloqueado
	          if (Boolean.TRUE.equals(usuario.getBloqueoLogin())) {
	        	  throw new MiExcepcion("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
								 +"<span class='fs-6'>Ha superado en número máximo de intentos para iniciar sesión, por favor espera unos minutos e intente nuevamente.</span>");
	           
	          }

	          // Establecer el usuario como bloqueado
	          usuario.setBloqueoLogin(TRUE);

	          // Definir el tiempo actual de bloqueo
	          LocalDateTime tiempoDeBloqueo = LocalDateTime.now();
	          usuario.setHoraBloqueologin(tiempoDeBloqueo);
	          // Guardar los cambios en la base de datos
	          try {
	              repositorioUsuario.save(usuario);
	          } catch (Exception e) {
	              throw new MiExcepcion("Error al guardar el estado de bloqueo del usuario: " + e.getMessage());
	          }
	      } else {
	          throw new MiExcepcion("No se encontró un usuario con el email proporcionado.");
	      }
	  }
	  
	  
	  
	  @Transactional
	  public void bloquearUsuarioPorIntentosValidacion(String emailUsuario) throws MiExcepcion {
	      // Buscar el usuario por email en la base de datos
	      Optional<Usuario> encontrarUsuario = repositorioUsuario.findByEmail(emailUsuario);

	      // Verificar si el usuario existe
	      if (encontrarUsuario.isPresent()) {
	          Usuario usuario = encontrarUsuario.get();
	         
	          // Verificar si el usuario ya está bloqueado
	          if (Boolean.TRUE.equals(usuario.getBloqueoValidacion())) {
	        	  throw new MiExcepcion("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
								 +"<span class='fs-6'>Ha superado en número máximo de intentos, por favor espera unos minutos y genere un nuevo codigo.</span>");
	           
	          }

	          // Establecer el usuario como bloqueado
	          usuario.setBloqueoValidacion(TRUE);

	          // Definir el tiempo actual de bloqueo
	          LocalDateTime tiempoDeBloqueo = LocalDateTime.now();
	          usuario.setHoraDeBloqueoValidacion(tiempoDeBloqueo);
	          // Guardar los cambios en la base de datos
	          try {
	              repositorioUsuario.save(usuario);
	          } catch (Exception e) {
	              throw new MiExcepcion("Error al guardar el estado de bloqueo del usuario: " + e.getMessage());
	          }
	      } else {
	          throw new MiExcepcion("No se encontró un usuario con el email proporcionado.");
	      }
	  }
	  
	  @Transactional
	  public void blanquearBloqueoLogin(String emailUsuario) throws MiExcepcion {
		  
		  try {
			  //buscar al usuario por email en la base de datos
			  Optional<Usuario> encontrarUsuario = repositorioUsuario.findByEmail(emailUsuario);

			  //verificar si el usuario existe
		      if (encontrarUsuario.isPresent()) {
		          Usuario usuario = encontrarUsuario.get();
		          usuario.setIntentosLogin(0);
		          usuario.setBloqueoLogin(FALSE);
		          usuario.setHoraBloqueologin(null);
		          
		          try {
					repositorioUsuario.save(usuario);
				} catch (Exception e) {
					throw new MiExcepcion("No se pudo guardar el blanqueo de login" + e.getMessage());
				}
		          
		      }
		} catch (Exception e) {
			throw new MiExcepcion("No se encontro el usuario por el metodo blanquear usuario login");
		}
	  }
		  

	  @Transactional
	  public Boolean verificarYDesbloquearUsuarioLogin(String emailUsuario) throws MiExcepcion {
		  
		  //buscar al usuario por email en la base de datos
	      Optional<Usuario> encontrarUsuario = repositorioUsuario.findByEmail(emailUsuario);

	      //verificar si el usuario existe
	      if (encontrarUsuario.isPresent()) {
	          Usuario usuario = encontrarUsuario.get();

	          // Verificar si el usuario está bloqueado
	          if (Boolean.TRUE.equals(usuario.getBloqueoLogin())) {
	              // Calcular la diferencia de tiempo entre el momento actual y el tiempo de bloqueo
	              LocalDateTime tiempoActual = LocalDateTime.now();
	              LocalDateTime tiempoDeBloqueo = usuario.getHoraBloqueologin();

	              // Verificar si han pasado 5 minutos desde el tiempo de bloqueo
	              if (tiempoDeBloqueo != null && ChronoUnit.MINUTES.between(tiempoDeBloqueo, tiempoActual) >= 5) {
	                  // Desbloquear al usuario
	                  usuario.setBloqueoLogin(FALSE); //Limpiar el bloqueo
	                  usuario.setHoraBloqueologin(null); // Limpiar el tiempo de bloqueo
	                  usuario.setIntentosLogin(0); //volvemos los intentos a cero

	                  try {
	                	  //guardamos la modificacion en la base de datos
	                      repositorioUsuario.save(usuario);
	                      return false; //devolvemos false si el usuario fue desbloqueado
	                  } catch (Exception e) {
	                      throw new MiExcepcion("Error al desbloquear al usuario: " + e.getMessage());
	                  }
	              }else {
	            	  return true; // devuelve true si todavia no han transcurrido los 5 min de bloqueo
	              }
	            	  
	          }else {
	        	  return false; // Retornar false si el usuario no esta bloqueado
	          }
	        	 
	      } else {
	          System.out.println("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
 					 +"<span class='fs-6'>No se encontró usuario con el correo electrónico ingresado</span><br><br>");
	          
	          return false; //tambien devolvemos false si el usuario no esta registrado en la base de datos
	      }
	  }
	  
	  
	  @Transactional
	  public Boolean verificarYDesbloquearUsuarioValidacion(String emailUsuario) throws MiExcepcion {
		  
		  //buscar al usuario por email en la base de datos
	      Optional<Usuario> encontrarUsuario = repositorioUsuario.findByEmail(emailUsuario);

	      //verificar si el usuario existe
	      if (encontrarUsuario.isPresent()) {
	          Usuario usuario = encontrarUsuario.get();

	          // Verificar si el usuario está bloqueado
	          if (Boolean.TRUE.equals(usuario.getBloqueoValidacion())) {
	              // Calcular la diferencia de tiempo entre el momento actual y el tiempo de bloqueo
	              LocalDateTime tiempoActual = LocalDateTime.now();
	              LocalDateTime tiempoDeBloqueo = usuario.getHoraDeBloqueoValidacion();

	              // Verificar si han pasado 5 minutos desde el tiempo de bloqueo
	              if (tiempoDeBloqueo != null && ChronoUnit.MINUTES.between(tiempoDeBloqueo, tiempoActual) >= 5) {
	                  // Desbloquear al usuario
	                  usuario.setBloqueoValidacion(FALSE); //Limpiar el bloqueo
	                  usuario.setHoraDeBloqueoValidacion(null); // Limpiar el tiempo de bloqueo
	                  usuario.setIntentosValidacion(0); //volvemos los intentos a cero

	                  try {
	                	  //guardamos la modificacion en la base de datos
	                      repositorioUsuario.save(usuario);
	                      return false; //devolvemos false si el usuario fue desbloqueado
	                  } catch (Exception e) {
	                      throw new MiExcepcion("Error al desbloquear al usuario: " + e.getMessage());
	                  }
	              }else {
	            	  return true; // devuelve true si todavia no han transcurrido los 5 min de bloqueo
	              }
	            	  
	          }else {
	        	  return false; // Retornar false si el usuario no esta bloqueado
	          }
	        	 
	      } else {
	          System.out.println("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
 					 +"<span class='fs-6'>No se encontró usuario con el correo electrónico ingresado</span><br><br>");
	          
	          return false; //tambien devolvemos false si el usuario no esta registrado en la base de datos
	      }
	  }

	  
	  
	  public Boolean validarUsuarioRegistradoActivo(String emailUsuario) throws MiExcepcion {
		  // Buscar usuario por email
	      Optional<Usuario> encontrarUsuario = repositorioUsuario.findByEmail(emailUsuario);
	      if (encontrarUsuario.isPresent()) {
			Usuario usuario = encontrarUsuario.get();
			
			if (usuario.getEmailValidado()) {
				return true;
			}else {
				return false;
			}
		}
	      
	      return false;
	  }
	  
	  public Boolean validarUsuarioRegistradoTemporal(String emailUsuario) throws MiExcepcion {
		  // Buscar usuario por email
	      Optional<Usuario> encontrarUsuario = repositorioUsuario.findByEmail(emailUsuario);
	      if (encontrarUsuario.isPresent()) {
			Usuario usuario = encontrarUsuario.get();
			
			if (encontrarUsuario.isPresent() && !usuario.getActivo() && !usuario.getEmailValidado()) {
				return true;
			}else {
				return false;
			}
		}
	      
	      return false;
	  }
		  
		  @Transactional
		  public int validarIntentosVerificacion(String emailUsuario) throws MiExcepcion {
		      // Buscar usuario por email
		      Optional<Usuario> encontrarUsuario = repositorioUsuario.findByEmail(emailUsuario);

		      if (!encontrarUsuario.isPresent()) {
		          throw new MiExcepcion("Usuario no encontrado con el email proporcionado.");
		      }

		      // Obtener usuario existente
		      Usuario usuario = encontrarUsuario.get();
		      int numeroIntentos = usuario.getIntentosValidacion();

		      // Incrementar el contador de intentos de verificación
		      numeroIntentos++; 
		      usuario.setIntentosValidacion(numeroIntentos); 

		      try {
		          // Guardar el usuario actualizado en la base de datos
		          Usuario usuarioActualizado = repositorioUsuario.save(usuario);
		          return usuarioActualizado.getIntentosValidacion();
		      } catch (Exception e) {
		          throw new MiExcepcion("No se pudieron guardar los intentos realizados");
		      }
		  }
		  
		  @Transactional
		  public int validarIntentosLogin(String emailUsuario) throws MiExcepcion {
		      // Buscar usuario por email
		      Optional<Usuario> encontrarUsuario = repositorioUsuario.findByEmail(emailUsuario);

		      if (!encontrarUsuario.isPresent()) {
		          throw new MiExcepcion("Usuario no encontrado con el email proporcionado.");
		      }

		      // Obtener usuario existente
		      Usuario usuario = encontrarUsuario.get();
		      int numeroIntentosLogin = usuario.getIntentosLogin();

		      // Incrementar el contador de intentos de verificación
		      numeroIntentosLogin++; 
		      usuario.setIntentosLogin(numeroIntentosLogin); 

		      try {
		          // Guardar el usuario actualizado en la base de datos
		          Usuario usuarioActualizado = repositorioUsuario.save(usuario);
		          return usuarioActualizado.getIntentosLogin();
		      } catch (Exception e) {
		          throw new MiExcepcion("No se pudieron guardar los intentos realizados");
		      }
		  }


	  
	  
	  @Transactional
	    public Usuario guardarOEncontrarUsuarioTemporal(String emailUsuario, String password, String password2, String fechaNacimiento) throws MiExcepcion {
		  
		//Buscamos la fecha actual para registrar cuando se creo el usuario
			//esto sirve para despues limpiar registros imcompletos viejos
			LocalDateTime fechaActual = LocalDateTime.now();
			
						
			try {
				
				  Optional<Usuario> encontrarUsuario = repositorioUsuario.findByEmail(emailUsuario);

			      if (!encontrarUsuario.isPresent()) {
			    	  
			    	  //Validamos los datos ingresados por el usuario
			    	  validarDatosUsuarioTemporal(emailUsuario, password, password2, fechaNacimiento);
			    	  
			    	  //Si los datos estan bien, creamos el nuevo usuario temporal
			    	  Usuario nuevoUsuario = new Usuario();
						nuevoUsuario.setFechaCreacion(fechaActual);
						nuevoUsuario.setEmail(emailUsuario);
						
						Usuario nuevoUsuarioTemp =  repositorioUsuario.save(nuevoUsuario);
						return nuevoUsuarioTemp;
			      }else {
			    	  //si el usuario ya esta registrado, obtenemos sus datos y los retornamos.
			    	 Usuario nuevoUsuario = encontrarUsuario.get();
			    	 return nuevoUsuario;
			      }
					
				
			} catch (Exception e) {
				throw new MiExcepcion(e.getMessage());
			}	
	    }
	  
	    
	  @Transactional
	    public void guardarUsuario(String email, String password, String password2, String fechaNacimiento, String idUsuarioNoValidado) throws MiExcepcion {
			
			LocalDate fechaDeNacimiento = servicioHorario.pasarFechaStringToLocalDate(fechaNacimiento);
			
			try {
				Optional <Usuario> usuarioNoValidado = buscarPorIdOptional(idUsuarioNoValidado);
				if (usuarioNoValidado.isPresent()) {
					Usuario NuevoUsuario = usuarioNoValidado.get();
					NuevoUsuario.setEmail(email);
					NuevoUsuario.setContrasena(password);
					NuevoUsuario.setFechaNacimiento(fechaDeNacimiento);
					NuevoUsuario.setActivo(TRUE);
					NuevoUsuario.setRol(Rol.CLIENTE);
					NuevoUsuario.setEmailValidado(TRUE);
					NuevoUsuario.setValidacionForm(FALSE);
					NuevoUsuario.setBloqueoValidacion(FALSE);
					NuevoUsuario.setBloqueoLogin(FALSE);
					NuevoUsuario.setHoraDeBloqueoValidacion(null);
					NuevoUsuario.setHoraBloqueologin(null);
					NuevoUsuario.setIntentosValidacion(0);
					NuevoUsuario.setIntentosLogin(0);
					repositorioUsuario.save(NuevoUsuario);
				}else {
					throw new MiExcepcion("Usuario no encontrando");
				}
			} catch (Exception e) {
				throw new MiExcepcion("Error al conectar con el servidor " + e);
			}	
	    }
	  
	  @Transactional
	    public void modificarContrasena( String id, String oldPass, String newPass, String repeatNewPass) throws MiExcepcion {
		  
		  verificarPasswordCambioContrasena(id, oldPass, newPass, repeatNewPass); //verificamos que la contraseña anterior y la nueva no sean iguales o que la contraseña anteriro sea correcta

		  try {
			  Optional<Usuario> presente = repositorioUsuario.findById(id);
			  if (presente.isPresent()) {
				  Usuario usuario = presente.get();
				  usuario.setContrasena(newPass);
				  repositorioUsuario.save(usuario);
			  }
		  } catch (Exception e) {
			  throw new MiExcepcion("Error al conectar con el servidor " + e);
		  }
	  }
			

	    public List<Usuario> listarUsuarios() throws MiExcepcion {
	    	try {
	    		List<Usuario> usuario = new ArrayList<Usuario>();
	    		usuario = repositorioUsuario.listarUsuarios();
	    		return usuario;
	    	} catch (Exception e) {
	    		throw new MiExcepcion("Error al conectar con el servidor " + e);
	    	}
	    }
				
		public List<Usuario> buscarId(String dni) {
			List<Usuario> idUsuario = repositorioUsuario.obtenerDatosPersonaUsuarioPorId(dni);
		return idUsuario;
		}

		
		public List<Usuario> buscarDni(String dni) {
			List<Usuario> dniUsuario = repositorioUsuario.obtenerDatosPersonaUsuarioPorDNI(dni);
		return dniUsuario;
		}
		
		
		public List<Usuario> buscarNombre(String nombre) {
			List<Usuario> nombreUsuario = repositorioUsuario.obtenerDatosPersonaUsuarioPorNombre(nombre);
		return nombreUsuario;
		}
		
		@Transactional
		public List<Usuario> buscarPorEmail(String email) {
			List<Usuario> emailUsuario = repositorioUsuario.obtenerDatosPersonaUsuarioPorEmail(email);
		return emailUsuario;
		}
		
		
		@Transactional
		public Optional<Usuario> buscarPorEmailOptional(String email) throws MiExcepcion {
			try {
				Optional<Usuario> emailUsuario = repositorioUsuario.buscarPorEmailOptional(email);
				return emailUsuario;
			} catch (Exception e) {
				throw new MiExcepcion("Error al conectar con la base de datos");
			}
		}
		
		
		public Optional<Usuario> buscarPorIdOptional(String id) {
			Optional<Usuario> usuarioId = repositorioUsuario.buscarPorIdOptional(id);
		return usuarioId;
		}
		
	
		public Optional<Usuario> buscarPorDniOptional(String dni) {
			Optional<Usuario> dnilUsuario = repositorioUsuario.obtenerDatosPersonaUsuarioPorDniOptional(dni);
		return dnilUsuario;
		}
		
	
		public Optional<Usuario> buscarPorNombreOptional(String nombre) {
			Optional<Usuario> nombreUsuario = repositorioUsuario.obtenerDatosPersonaUsuarioPorNombreOptional(nombre);
		return nombreUsuario;
		}
		
	
		public Optional<Usuario> buscarContrasena(String contrasena) {
			Optional<Usuario> contrasenaUsuario = repositorioUsuario.buscarContrasena(contrasena);
		return contrasenaUsuario;
		}
		

		public List<Usuario> buscarRol(Rol rol) {
			List<Usuario> rolUsuario = repositorioUsuario.findByRol(rol);
		return rolUsuario;
		}
		

		//Borra un admin de la base de datos
		@Transactional
		public void borrarUsuario(String id) throws MiExcepcion {
			try {
				Optional <Usuario> identificarUsuario = repositorioUsuario.findById(id);
				if (identificarUsuario.isPresent()) {
					Usuario usuarioDelete = identificarUsuario.get();
					repositorioUsuario.delete(usuarioDelete);
				}
			} catch (Exception e) {
				throw new MiExcepcion("Error al conectar con el servidor " + e);
			}
		}
					
				
			
		
		 @Transactional
		    public void bajaUsuario(String id) throws MiExcepcion {
			 try {
				 Optional<Usuario> presente = repositorioUsuario.findById(id);
				 if (presente.isPresent()) {
					 Usuario usuario_baja = new Usuario();
					 usuario_baja = presente.get();
					 usuario_baja.setActivo(FALSE); //El valor false se importa de un clase propia de java
					 repositorioUsuario.save(usuario_baja);
				 }
			 } catch (Exception e) {
				 throw new MiExcepcion("Error al conectar con el servidor " + e);
			 }
		 }
				 
				
		 
		 //Metodo para cambiar a usuarios que estaban inactivos a activos
		 @Transactional
		    public void altaUsuario(String id) throws MiExcepcion {
			 try {
				 Optional<Usuario> presente = repositorioUsuario.findById(id);
				 if (presente.isPresent()) {
					 Usuario usuario_alta = new Usuario();
					 usuario_alta = presente.get();
					 usuario_alta.setActivo(TRUE);
					 repositorioUsuario.save(usuario_alta);
				 }
			 } catch (Exception e) {
				 throw new MiExcepcion("Error al conectar con el servidor " + e);
			 }
		 }
				
		 @Transactional
		    public void modificarRol(String id, Rol nuevoRol) throws MiExcepcion {
			 try {
				 Optional<Usuario> presente = repositorioUsuario.findById(id);
				 if (presente.isPresent()) {
					 Usuario usuario_rol = new Usuario();
					 usuario_rol = presente.get();
					 usuario_rol.setValidacionForm(FALSE); //volvemos el form a false para que el cambio de rol obligue al usuario a llenar la nueva planilla de su rol
					 usuario_rol.setRol(nuevoRol);
					 repositorioUsuario.save(usuario_rol);
				 }
			 } catch (Exception e) {
				 throw new MiExcepcion("Error al conectar con el servidor " + e);
			 }
		 }
				
		 public void validarDatosUsuarioTemporal(String email, String password, String password2, String fechaNacimiento) throws MiExcepcion { 
			 verificarEmailUsuarioTemporal(email);
			 verificarPassword(password, password2);
			 validarEdad(fechaNacimiento);
			 
		 }
		 
		 //VALIDACIONES
		 public void validarDatosDelUsuario(String email, String password, String password2, String fechaNacimiento) throws MiExcepcion {
			 verificarEmail(email);
			 verificarPassword(password, password2);
			 validarEdad(fechaNacimiento);
		 }
		 
			  
		 public void validarEmail(String email) throws MiExcepcion {
			  if (Objects.equals(email, null) || email.isEmpty() || email.trim().isEmpty()) {
		            throw new MiExcepcion("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
		            					 +"<span class='fs-6'>El campo de correo electrónico no puede quedar vacío."
		            					 + " Por favor ingrese un correo electrónico válido</span>");
		        }
		  }
		 
		 public void verificarEmailUsuarioTemporal(String email) throws MiExcepcion {
				// Expresión regular para validar un correo electrónico
			        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

			        // Compilar la expresión regular
			        Pattern pattern = Pattern.compile(regex);

			        // Crear un objeto Matcher
			        Matcher matcher = pattern.matcher(email);

			        if (Objects.equals(email, null) || email.isEmpty() || email.trim().isEmpty()) {
			            throw new MiExcepcion("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
		   					 +"<span class='fs-6'>El campo de correo electrónico no puede quedar vacío."
		   					 + " Por favor ingrese un correo electrónico válido</span>");
			        }
			        // Verificar si la cadena cumple con la expresión regular
			        if (!matcher.matches()) {
			            throw new MiExcepcion("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
		   					 +"<span class='fs-6'>El correo electrónico no es válido.</span>");
			        } 

			    }
		 
		 
	  public void verificarEmail(String email) throws MiExcepcion {
		// Expresión regular para validar un correo electrónico
	        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

	        // Compilar la expresión regular
	        Pattern pattern = Pattern.compile(regex);

	        // Crear un objeto Matcher
	        Matcher matcher = pattern.matcher(email);

	        if (Objects.equals(email, null) || email.isEmpty() || email.trim().isEmpty()) {
	            throw new MiExcepcion("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
   					 +"<span class='fs-6'>El campo de correo electrónico no puede quedar vacío."
   					 + " Por favor ingrese un correo electrónico válido</span>");
	        }
	        // Verificar si la cadena cumple con la expresión regular
	        if (!matcher.matches()) {
	            throw new MiExcepcion("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
   					 +"<span class='fs-6'>El correo electrónico no es válido.</span>");
	        } 
	       
	        if (repositorioUsuario.buscarPorEmailOptional(email).isPresent()) {
	            throw new MiExcepcion("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
   					 +"<span class='fs-6'>El correo electrónico ya se encuentra registrado, por favor"
   					 + " ingrese otro.</span>");
	        }

	    }
	  	  

	  //Esta verificacion es para el login y el registrarse
	    public boolean verificarPassword(String password, String password2) throws MiExcepcion {
	    	
	    	 String pass = password.trim();
	    	 String pass2 = password2.trim();
	    	 String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";

	         // Compilar la expresión regular
	         Pattern pattern = Pattern.compile(regex);

	         // Crear un objeto Matcher
	         Matcher matcher = pattern.matcher(password);
	         
	         
	         if (pass.isEmpty()) {
	        	 throw new MiExcepcion("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
    					 +"<span class='fs-6'>El campo de la contraseña no puede quedar vacío."
    					 + "Por favor ingrese una contraseña válida.</span>");
	         }
	         if (pass2.isEmpty()) {
	        	 throw new MiExcepcion("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
    					 +"<span class='fs-6'>El campo de la contraseña no puede quedar vacío."
    					 + "Por favor ingrese una contraseña válida.</span>");
			}
	         // Verificar si la cadena cumple con la expresión regular
	         if (!matcher.matches()) {
	        	 throw new MiExcepcion("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
    					 +"<span class='fs-6'>La contraseña debe tener al menos 8 dígitos y debe tener mínimo"
    					 + " una letra mayúscula,"
    					 + " una letra minúscula y un número.</span>");
	         }  
	         if (!pass.equals(pass2)) {
	        	 throw new MiExcepcion("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
    					 +"<span class='fs-6'>Las contraseñas ingresadas no coinciden, por favor"
    					 + " intente nuevamente.</span>");
	         }
	         return true;
	    }
	    
	    //Esta verificacion es para el login y el registrarse
	    public boolean verificarPasswordLogin(String password, String password2) throws MiExcepcion {
	    	
	    	 String pass = password.trim();
	    	 String pass2 = password2.trim();
	    	 
	         if (pass.isEmpty()) {
	        	 throw new MiExcepcion("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
    					 +"<span class='fs-6'>El campo de la contraseña no puede quedar vacío."
    					 + "Por favor ingrese una contraseña válida.</span>");
	         }
	         
	 
	         if (!pass.equals(pass2)) {
	        	 throw new MiExcepcion("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
    					 +"<span class='fs-6'>Usuario o contraseña incorrectos, por favor intente nuevamente.</span>");
	         }
	         return true;
	    }
	    
	    //Esta verifiacion es para cambiar el password de clientes, profesionales y admins
	    public boolean verificarPasswordCambioContrasena(String id,  String oldPassword, String newPassword, String repeatNewPassword) throws MiExcepcion {
	    	
	    	String contrasenaAnterior = oldPassword.trim();
	    	String contrasenaNueva = newPassword.trim();
	    	String repetirNuevaContrasena = repeatNewPassword.trim();
	    	String contrasenaRegistrada = null;
	    	String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";
	    	

	         // Compilar la expresión regular
	         Pattern pattern = Pattern.compile(regex);

	         // Crear un objeto Matcher
	         Matcher matcher = pattern.matcher(contrasenaNueva);
	    	
	    	Optional<Usuario> presente = repositorioUsuario.findById(id);
	    	if (presente.isPresent()) {
				Usuario infoUsuario = presente.get();
				contrasenaRegistrada = infoUsuario.getContrasena();
			}
	    	
	    	if (contrasenaAnterior.isEmpty()) {
	    		throw new MiExcepcion("La contrasena anterior no puede estar vacio");
			}
	    	if (contrasenaNueva.isEmpty()) {
	    		throw new MiExcepcion("La nueva contrasena no puede estar vacio");
			}
	    	if (repetirNuevaContrasena.isEmpty()) {
	    		throw new MiExcepcion("Repetir la contrasena no puede estar vacio");	
			}
	    	if (!contrasenaRegistrada.equals(oldPassword)) {
				throw new MiExcepcion("La contrasena anterior es incorrecta");
			}
	    	 if (!matcher.matches()) {
	        	 throw new MiExcepcion("La nueva contrasena debe tener al menos 8 dígitos, una mayúscula, una minúscula y un numeró.");
	         }  
	    	if (contrasenaRegistrada.equals(contrasenaNueva)) {
	    		throw new MiExcepcion("La nueva contrasena no puede ser igual a la enterior");
			}
	    	 if (!contrasenaNueva.equals(repetirNuevaContrasena)) {
	        	 throw new MiExcepcion("Las contrasenas no son iguales");
	         }
	    	return true;
	    }
	    	
	    	public boolean validarEdad(String fechaNacimiento) throws MiExcepcion {
	    		
	    		Date fecha = null;
	    		LocalDate fechaUsuario = null;
	    		LocalDate fechaActual = null; //Obtenemos la fecha actual 
	    		
	    		//Validamos que la fecha no sea null antes de pasarla a LocalDate
	    		if (Objects.equals(fechaNacimiento, null) || fechaNacimiento.isEmpty()) {
	    			throw new MiExcepcion("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
	    					 +"<span class='fs-6'>El campo de la fecha de nacimiento no puede"
	    					 + " estar vacío.</span>");
				}
	    		
	    		// pasamos la fecha de String a date y luego a LocalDate
	    		 try {
	    	            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
	    	            fecha = formatoFecha.parse(fechaNacimiento);
	    	            fechaUsuario =  fecha.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
	    	        } catch (ParseException e) {
	    	            e.printStackTrace(); // Manejo de excepciones, por ejemplo, formato de fecha incorrecto
	    	           
	    	        }
	    		
	    		//hacemos la resta de la fecha del usuario con la fecha actual
	    		Integer edad = fechaActual.now().getYear() - fechaUsuario.getYear();
	  
	    		//Validamos que el usuario sea mayor de 13 años
	    		if (edad <= 13) {
	    			throw new MiExcepcion("<span class= 'fs-6 fw-bold'>Estimado usuario,</span><br><br>"
	    					 +"<span class='fs-6'>Lamentamos informarte que no cumples con la edad mínima"
	    					 + " requerida para utilizar los servicios de nuestra página.</span>");
				}
	    		return true;
	    	}
	    		
}
