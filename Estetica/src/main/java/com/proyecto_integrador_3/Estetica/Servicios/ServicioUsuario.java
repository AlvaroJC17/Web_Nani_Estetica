package com.proyecto_integrador_3.Estetica.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.time.LocalDate;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;

import jakarta.transaction.Transactional;

@Service
public class ServicioUsuario {
	
	  @Autowired
	  public RepositorioUsuario repositorioUsuario;
	  
	  @Autowired
	  public RepositorioCliente repositorioCliente;
	  
	  @Autowired
	  public RepositorioPersona repositorioPersona;
	  
	  @Transactional
	    public void guardarUsuario(String email, String password, String password2, String fechaNacimiento) throws MiExcepcion {

		  verificarEmail(email);
		  verificarPassword(password, password2);
		  validarEdad(fechaNacimiento);
		  
		    //pasamos la fecha de string a date
	        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			Date fecha = null;
			try {
				fecha = formato.parse(fechaNacimiento);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
	        Usuario usuario = new Usuario();
	        usuario.setEmail(email);
	        usuario.setContrasena(password);
	        usuario.setFechaNacimiento(fecha);
	        usuario.setActivo(TRUE);
	        usuario.setRol(Rol.CLIENTE);
	        usuario.setValidacionForm(FALSE);
	        repositorioUsuario.save(usuario);
	        
	    }
	  
	  @Transactional
	    public void modificarContrasena( String id, String oldPass, String newPass, String repeatNewPass) throws MiExcepcion {
		  
		  verificarPasswordCambioContrasena(id, oldPass, newPass, repeatNewPass); //verificamos que la contraseña anterior y la nueva no sean iguales o que la contraseña anteriro sea correcta

	        Optional<Usuario> presente = repositorioUsuario.findById(id);

	        if (presente.isPresent()) {
	            Usuario usuario = presente.get();
	            usuario.setContrasena(newPass);
	            repositorioUsuario.save(usuario);
	        }
	  }
	            
	                        	  
	  @Transactional()
	    public List<Usuario> listarUsuarios() {
		  List<Usuario> usuario = new ArrayList();
		  usuario = repositorioUsuario.listarUsuarios();
		  return usuario;
	  }

	  
		@Transactional
		public List<Usuario> buscarId(String dni) {
			List<Usuario> idUsuario = repositorioUsuario.obtenerDatosPersonaUsuarioPorId(dni);
		return idUsuario;
		}

		@Transactional
		public List<Usuario> buscarDni(String dni) {
			List<Usuario> dniUsuario = repositorioUsuario.obtenerDatosPersonaUsuarioPorDNI(dni);
		return dniUsuario;
		}
		
		@Transactional
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
		public Optional<Usuario> buscarPorEmailOptional(String email) {
			Optional<Usuario> emailUsuario = repositorioUsuario.buscarPorEmailOptional(email);
		return emailUsuario;
		}
		
		@Transactional
		public Optional<Usuario> buscarPorIdOptional(String id) {
			Optional<Usuario> usuarioId = repositorioUsuario.buscarPorIdOptional(id);
		return usuarioId;
		}
		
		@Transactional
		public Optional<Usuario> buscarPorDniOptional(String dni) {
			Optional<Usuario> dnilUsuario = repositorioUsuario.obtenerDatosPersonaUsuarioPorDniOptional(dni);
		return dnilUsuario;
		}
		
		@Transactional
		public Optional<Usuario> buscarPorNombreOptional(String nombre) {
			Optional<Usuario> nombreUsuario = repositorioUsuario.obtenerDatosPersonaUsuarioPorNombreOptional(nombre);
		return nombreUsuario;
		}
		
		@Transactional
		public Optional<Usuario> buscarContrasena(String contrasena) {
			Optional<Usuario> contrasenaUsuario = repositorioUsuario.buscarContrasena(contrasena);
		return contrasenaUsuario;
		}
		
	
		
		@Transactional
		public List<Rol> buscarRol() {
			List<Rol> rolUsuario = repositorioUsuario.buscarRol();
		return rolUsuario;
		}

		//Borra un admin de la base de datos
		public void borrarUsuario(String id) {
			Optional <Usuario> identificarUsuario = repositorioUsuario.findById(id);
			
			if (identificarUsuario.isPresent()) {
				Usuario usuarioDelete = identificarUsuario.get();
				repositorioUsuario.delete(usuarioDelete);
				
			}
		}
		
		 @Transactional
		    public void bajaUsuario(String id) {
		        Optional<Usuario> presente = repositorioUsuario.findById(id);

		        if (presente.isPresent()) {
		        	Usuario usuario_baja = new Usuario();
		        	usuario_baja = presente.get();
		        	usuario_baja.setActivo(FALSE); //El valor false se importa de un clase propia de java
		        	repositorioUsuario.save(usuario_baja);
		        }
		    }
		 
		 //Metodo para cambiar a usuarios que estaban inactivos a activos
		 @Transactional
		    public void altaUsuario(String id) {

		        Optional<Usuario> presente = repositorioUsuario.findById(id);

		        if (presente.isPresent()) {
		        	Usuario usuario_alta = new Usuario();
		        	usuario_alta = presente.get();
		        	usuario_alta.setActivo(TRUE);
		        	repositorioUsuario.save(usuario_alta);
		        }
		    }
		
		 
		 @Transactional
		    public void modificarRol(String id, Rol rol) {

		        Optional<Usuario> presente = repositorioUsuario.findById(id);

		        if (presente.isPresent()) {
		        	Usuario usuario_rol = new Admin();
		        	usuario_rol = presente.get();
		        	usuario_rol.setRol(rol);
		        	usuario_rol.setValidacionForm(FALSE); //volvemos el form a false para que el cambio de rol obligue al usuario a llenar la nueva planilla de su rol
		        	repositorioUsuario.save(usuario_rol);
		        }
		        
		    }
		
	  
	  //VALIDACIONES
	  public void verificarEmail(String email) throws MiExcepcion {
		// Expresión regular para validar un correo electrónico
	        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

	        // Compilar la expresión regular
	        Pattern pattern = Pattern.compile(regex);

	        // Crear un objeto Matcher
	        Matcher matcher = pattern.matcher(email);

	        if (Objects.equals(email, null) || email.isEmpty() || email.trim().isEmpty()) {
	            throw new MiExcepcion("El correo electronico no puede estar vacío");
	        }
	        // Verificar si la cadena cumple con la expresión regular
	        if (!matcher.matches()) {
	            throw new MiExcepcion("El correo electronico no es valido");
	        } 
	       
	        if (repositorioUsuario.buscarPorEmailOptional(email).isPresent()) {
	            throw new MiExcepcion("El correo electronico ya se encuentra registrado");
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
	        	 throw new MiExcepcion("La constrasena no puede estar vacía");
	         }
	         if (pass2.isEmpty()) {
	        	 throw new MiExcepcion("La constrasena no puede estar vacía");
			}
	         // Verificar si la cadena cumple con la expresión regular
	         if (!matcher.matches()) {
	        	 throw new MiExcepcion("La contrasena debe tener al menos 8 digitos, una mayuscula, una minuscula y un numero.");
	         }  
	         if (!pass.equals(pass2)) {
	        	 throw new MiExcepcion("Las contrasenas no son iguales");
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
	    		throw new MiExcepcion("La contrasena anterior no puede estar vacia");
			}
	    	if (contrasenaNueva.isEmpty()) {
	    		throw new MiExcepcion("La nueva contrasena no puede estar vacia");
			}
	    	if (repetirNuevaContrasena.isEmpty()) {
	    		throw new MiExcepcion("Repetir la contrasena no puede estar vacia");	
			}
	    	if (!contrasenaRegistrada.equals(oldPassword)) {
				throw new MiExcepcion("La contrasena anterior es incorrecta");
			}
	    	 if (!matcher.matches()) {
	        	 throw new MiExcepcion("La contrasena debe tener al menos 8 digitos, una mayuscula, una minuscula y un numero.");
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
	    			throw new MiExcepcion("La fecha de nacimiento no puede estar vacia");
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
	    			throw new MiExcepcion("No cumples la edad minima para usar nuestros servicios");
				}
	    		return true;
	    	}
	    		
}
