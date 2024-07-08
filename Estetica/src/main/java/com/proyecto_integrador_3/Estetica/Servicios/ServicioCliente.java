package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.TRUE;

import java.time.LocalDateTime;

import static java.lang.Boolean.FALSE;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.CodigoDeVerificacion;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCodigoDeVerificacion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;

import jakarta.transaction.Transactional;

@Service
public class ServicioCliente {

	@Autowired
	private RepositorioCliente repositorioCliente;
	
	@Autowired
	private RepositorioUsuario repositorioUsuario;
	
	
	@Autowired
	private ServicioUsuario servicioUsuario;
	
	@Autowired
	private ServicioCodigoDeVerificacion servicioCodigoDeVerificacion;
	
	@Autowired
	private RepositorioPersona repositorioPersona;
	
	@Autowired
	private RepositorioCodigoDeVerificacion repositorioCodigoDeVerificacion;
	
	
	
	public List <Cliente> buscarPacientePorId(String idCliente) throws MiExcepcion{
		try {
			return repositorioCliente.buscarPorId(idCliente);
		} catch (Exception e) {
			throw new MiExcepcion("Error con conectar el servidor " + e);
		}
			
	}
	
	
	
	@Transactional
	public void registrarCliente(String email, String dni, String nombre, String apellido, String ocupacion,
			String direccion, String telefono, String sexo) throws MiExcepcion {
		
		validarDatosCliente(nombre, apellido, dni, sexo, telefono, direccion, ocupacion);

		//Pasamos la fecha de nacimiento de String a un objeto tipo Date
		Sexo nuevoSexo = null;
		nuevoSexo = Sexo.valueOf(sexo.toUpperCase());
		
		try {
		//Con este optional buscamos los datos del usuario que le vamos a pasar al nuevo cliente
		//y despues borrar ese usuario
		Optional <Usuario> datosUsuario = servicioUsuario.buscarPorEmailOptional(email);
		if (datosUsuario.isPresent()) {
			Usuario datosCliente = datosUsuario.get();
			
			//Con este opcional usamos el id del usuario obtenido del optional anterior y buscamos
			//los datos de ese usario en la tabla de codigo_de_verificacion para borrarlos
			//sino borramos primero esos datos de la tabla de codigo nos da un error de llave foranea al intentar borrar el usuario viejo
			CodigoDeVerificacion datos = null;
			Optional<CodigoDeVerificacion> datosTabla = servicioCodigoDeVerificacion.obtenerDatosTablaCodigoPorUsuarioId(datosCliente.getId());
			if (datosTabla.isPresent()) {
				datos = datosTabla.get();
				System.out.println("Datos de codigo: " + datos);
			}
		
			Cliente nuevo_cliente = new Cliente();
			nuevo_cliente.setEmail(email);
			nuevo_cliente.setContrasena(datosCliente.getContrasena());
			nuevo_cliente.setRol(datosCliente.getRol());
			nuevo_cliente.setActivo(datosCliente.getActivo());
			nuevo_cliente.setValidacionForm(TRUE);
			nuevo_cliente.setFomularioDatos(FALSE);
			nuevo_cliente.setEmailValidado(TRUE);
			nuevo_cliente.setMulta(FALSE);
			nuevo_cliente.setDni(dni);
			nuevo_cliente.setNombre(nombre);
			nuevo_cliente.setApellido(apellido);
			nuevo_cliente.setTelefono(telefono);
			nuevo_cliente.setDomicilio(direccion);
			nuevo_cliente.setFechaCreacion(LocalDateTime.now()); //En esta instancia esta fecha nos puede servir para saber cuando se dio de alta el usuario
			nuevo_cliente.setFechaNacimiento(datosCliente.getFechaNacimiento());
			nuevo_cliente.setSexo(nuevoSexo);
			nuevo_cliente.setOcupacion(ocupacion);
				if (datos != null) { //se agrega este condicional, porque cuando nuevo cliente es por un cambio de rol, el valor dato es null
					repositorioCodigoDeVerificacion.delete(datos);//primero borramos los datos del usuario de la tabla de codigo
				}
				repositorioUsuario.delete(datosCliente); //segundo borramos todos los datos anteriores del usuario para que no choquen con el registro de los nuevos
				repositorioCliente.save(nuevo_cliente); //Guardamos los datos del usuario como un nuevo cliente
		}
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con el servidor " + e);
		}
		
	}
				
		
	@Transactional
	public void modificarCliente(String idCliente, String ocupacion, String email, String emailAnterior, String domicilio, String sexo, String telefono) throws MiExcepcion {
		
		verificarEmailCliente(email, emailAnterior);
		validarActualizacionDeDatosCliente(ocupacion, domicilio, sexo, telefono);
		
		
		try {
		Optional<Cliente> identificarCliente = repositorioCliente.findById(idCliente);
		if (identificarCliente.isPresent()) {
			Cliente cliente_actualizado = identificarCliente.get(); // Atribuye el objeto presente a esta nueva variable
			
			Sexo nuevoSexo = null;
			nuevoSexo = Sexo.valueOf(sexo.toUpperCase());
			
        	cliente_actualizado.setOcupacion(ocupacion);
        	cliente_actualizado.setSexo(nuevoSexo);
        	cliente_actualizado.setDomicilio(domicilio);
        	cliente_actualizado.setTelefono(telefono);
        		repositorioCliente.save(cliente_actualizado);
        	}
        	} catch (Exception e) {
        		throw new MiExcepcion("Error al conectar con el servidor " + e);
        	}
				
	}
	
	

	 public void validarDatosCliente(String nombre, String apellido, String dni,  String sexo,
			 String telefono, String direccion, String ocupacion) throws MiExcepcion {
		 
		 // Expresión regular para validar un telefono
		 String regex = "\\d{7,10}";
		 
		 // Compilar la expresión regular
		 Pattern pattern = Pattern.compile(regex);
		 
		 // Crear un objeto Matcher
		 Matcher matcher = pattern.matcher(telefono);
		 
		 if (!matcher.matches()) {
			 throw new MiExcepcion("<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"
					 + "<span class='fs-6'>El telefono no cumple con el formato solicitado, por favor verifique e intente nuevamente.</span>");
		 } 
		 
		 if (nombre == null || nombre.isEmpty() || nombre.trim().isEmpty()) {
			 throw new MiExcepcion("El nombre no puede estar vacio");
		 }
		 if (apellido == null || apellido.isEmpty() || apellido.trim().isEmpty()) {
			 throw new MiExcepcion("El apellido no puede estar vacio");
		 }
		 if (dni == null || dni.isEmpty() || dni.trim().isEmpty()) {
			 throw new MiExcepcion("El dni no puede estar vacio");
		 } 
		 if(repositorioPersona.buscarPorDniOptional(dni).isPresent()) {
			 throw new MiExcepcion("El numero de dni ya está registrado");
		 }
		 if (sexo == null || sexo.isEmpty() || sexo.trim().isEmpty() || sexo.equals("Seleccione")) {
			 throw new MiExcepcion("El sexo no puede estar vacio");
		 }
		 if (direccion == null || direccion.isEmpty() || direccion.trim().isEmpty()) {
			 throw new MiExcepcion("La dirección no puede estar vacia");
		 }
		 if (telefono == null || telefono.isEmpty() || telefono.trim().isEmpty()) {
			 throw new MiExcepcion("EL telefono no puede estar vacio");
		 }
		 if (ocupacion == null || ocupacion.isEmpty() || ocupacion.trim().isEmpty()) {
			 throw new MiExcepcion("La ocupacion no puede estar vacio");
		 }
	 }
	 
	 	 
	 public void validarActualizacionDeDatosCliente(String ocupacion, String domicilio, String sexo, String telefono) throws MiExcepcion {
		
		 // Expresión regular para validar un telefono
		 String regex = "\\d{7,10}";
		 
		 // Compilar la expresión regular
		 Pattern pattern = Pattern.compile(regex);
		 
		 // Crear un objeto Matcher
		 Matcher matcher = pattern.matcher(telefono);
		 
		 if (!matcher.matches()) {
			 throw new MiExcepcion("<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"
					 + "<span class='fs-6'>El telefono no cumple con el formato solicitado, por favor verifique e intente nuevamente.</span>");
		 } 
		 
		 if (ocupacion == null || ocupacion.isEmpty() || ocupacion.trim().isEmpty()) {
			 throw new MiExcepcion("<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"
 					+ "<span class='fs-6'>El campo de la ocupación no puede estar vacío.</span>");
		 }
		 
		 if (domicilio == null || domicilio.isEmpty() || domicilio.trim().isEmpty()) {
			 throw new MiExcepcion("<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"
 								 + "<span class='fs-6'>El campo de la dirección no puede estar vacío.</span>");
		 }
		 
		 if (telefono == null || telefono.toString().isEmpty() || telefono.toString().isEmpty()) {
			 throw new MiExcepcion("<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"
 					+ "<span class='fs-6'>El campo del teléfono no puede estar vacío.</span>");
		 }
		 
	 }
	 
	 public void verificarEmailCliente(String email, String emailAnterior) throws MiExcepcion {
		 
			// Expresión regular para validar un correo electrónico
		        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

		        // Compilar la expresión regular
		        Pattern pattern = Pattern.compile(regex);

		        // Crear un objeto Matcher
		        Matcher matcher = pattern.matcher(email);
		        
		        if (email == null || email.isEmpty() || email.trim().isEmpty()) {
		            throw new MiExcepcion("<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"
		            					+ "<span class='fs-6'>El email no puede estar vacio.</span>");
		        }

		        // Verificar si la cadena cumple con la expresión regular
		        if (!matcher.matches()) {
		            throw new MiExcepcion("<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"
        								+ "<span class='fs-6'>El email no es valido, por favor verifique e intente nuevamente.</span>");
		        } 
		       
		        if (!email.equalsIgnoreCase(emailAnterior)) {
		        	if (repositorioUsuario.buscarPorEmailOptional(email).isPresent()) {
		        		throw new MiExcepcion("<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"
            								+ "<span class='fs-6'>El email ingresado ya se encuentra registrado.</span>");
		        	}
	 
		        }
	 }
		        	
	 
	
	 
	 
}
	 
		        	
	 
	 



