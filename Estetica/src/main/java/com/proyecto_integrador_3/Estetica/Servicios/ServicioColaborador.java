package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.CodigoDeVerificacion;
import com.proyecto_integrador_3.Estetica.Entidades.Colaborador;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCodigoDeVerificacion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioColaborador;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;

import jakarta.transaction.Transactional;

@Service
public class ServicioColaborador {
	
	@Autowired
	public RepositorioColaborador repositorioColaborador;
	
	@Autowired
	public RepositorioCodigoDeVerificacion repositorioCodigoDeVerificacion;
	
	@Autowired
	public RepositorioUsuario repositorioUsuario;
	
	@Autowired
	public RepositorioPersona repositorioPersona;
	
	@Autowired
	public RepositorioCliente repositorioCliente;
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	@Autowired
	public ServicioCodigoDeVerificacion servicioCodigoDeVerificacion;

	@Autowired
	public ServicioCliente servicioCliente;
	
	@Autowired
	public ServicioHorario servicioHorario;
	
	
	@Transactional
	public void modificarClienteColaborador(String idCliente, String ocupacion, String email, String emailAnterior, String dniAnterior, String domicilio, String sexo, String telefono, String dni, String fechaNacimiento) throws MiExcepcion {
		
		servicioCliente.verificarEmailCliente(email, emailAnterior); //Validamos que el email este correcto y que no este repetido en la base de datos
		validarDatosClienteColaborador(ocupacion, domicilio, sexo, telefono, dni, dniAnterior, fechaNacimiento);
		
		
		try {
		Optional<Cliente> identificarCliente = repositorioCliente.findById(idCliente);
		if (identificarCliente.isPresent()) {
			Cliente cliente_actualizado = identificarCliente.get(); // Atribuye el objeto presente a esta nueva variable
			
			Sexo nuevoSexo = null;
			nuevoSexo = Sexo.valueOf(sexo.toUpperCase());
			
			LocalDate fechaNacimientoLocalDate = null;
			fechaNacimientoLocalDate = servicioHorario.pasarFechaStringToLocalDateOtroFormato(fechaNacimiento);
			
			
			cliente_actualizado.setOcupacion(ocupacion);
			cliente_actualizado.setSexo(nuevoSexo);
			cliente_actualizado.setDomicilio(domicilio);
			cliente_actualizado.setTelefono(telefono);
			cliente_actualizado.setDni(dni);
			cliente_actualizado.setEmail(email);
			cliente_actualizado.setFechaNacimiento(fechaNacimientoLocalDate);
			repositorioCliente.save(cliente_actualizado);
		}
		} catch (Exception e) {
			throw new MiExcepcion("Error al modificar los datos del cliente desde el perfil colaborador" + e);
		}
		
	}
	

	public Colaborador optionalBuscarColaboradorPorId(String idColaborador) throws MiExcepcion{
		Optional<Colaborador> buscarColaborador = repositorioColaborador.findById(idColaborador);
		if (buscarColaborador.isPresent()) {
			Colaborador datosDelColaborador = buscarColaborador.get();
			return datosDelColaborador;
		}else {
			throw new MiExcepcion("No se encontro al colaborador");
		}
	}
	
	public Colaborador optionalBuscarColaboradorPorEmail(String emailColaborador) throws MiExcepcion{
		Optional<Colaborador> buscarColaborador = repositorioColaborador.findByEmail(emailColaborador);
		if (buscarColaborador.isPresent()) {
			Colaborador datosDelColaborador = buscarColaborador.get();
			return datosDelColaborador;
		}else {
			throw new MiExcepcion("No se encontro al colaborador");
		}
	}
	
	
	@Transactional
	public void registrarColaborador(String email) throws MiExcepcion {
	
		try {
		//Con este optional buscamos los datos del usuario que le vamos a pasar al nuevo cliente
		//y despues borrar ese usuario
		Optional <Usuario> datosUsuario = servicioUsuario.buscarPorEmailOptional(email);
	
		if (datosUsuario.isPresent()) {
			Usuario datosCliente = datosUsuario.get();
			
			//Buscamos los datos de nombre, apellido, fechaNacimiento y dni de la persona y los guardamos como nuevo profesional, asi la persona no los tiene que ingresar de nuevo en el formulario
			String nombrePersona = null;
			String apellidoPersona = null;
			String dniPersona = null;
			String telefono = null;
			String domicilio = null;
			String ocupacion = null;
			Sexo sexo = null;
			
			//Con el email buscamos todos los datos que pertenecen a la clase persona para registrar al admin
			Optional <Persona> datosPersona = repositorioPersona.buscarPorEmailOptional(email);
			if (datosPersona.isPresent()) {
				Persona datosPersonalesPersona = datosPersona.get();
				nombrePersona = datosPersonalesPersona.getNombre();
				apellidoPersona = datosPersonalesPersona.getApellido();
				dniPersona = datosPersonalesPersona.getDni();
				telefono = datosPersonalesPersona.getTelefono();
				domicilio = datosPersonalesPersona.getDomicilio();
				ocupacion = datosPersonalesPersona.getOcupacion();
				sexo = datosPersonalesPersona.getSexo();
			}
	

			//Con este opcional usamos el id del usuario obtenido del optional anterior y buscamos
			//los datos de ese usario en la tabla de codigo_de_verificacion para borrarlos
			//sino borramos primero esos datos de la tabla de codigo nos da un error de llave foranea al intentar borrar el usuario viejo
			CodigoDeVerificacion datos = null;
			Optional<CodigoDeVerificacion> datosTabla = servicioCodigoDeVerificacion.obtenerDatosTablaCodigoPorUsuarioId(datosCliente.getId());
			if (datosTabla.isPresent()) {
				datos = datosTabla.get();
			}
		
			Colaborador nuevo_colaborador = new Colaborador();
			nuevo_colaborador.setEmail(email.trim());
			nuevo_colaborador.setContrasena(datosCliente.getContrasena());
			nuevo_colaborador.setRol(datosCliente.getRol());
			nuevo_colaborador.setActivo(datosCliente.getActivo());
			nuevo_colaborador.setEmailValidado(datosCliente.getEmailValidado());
			nuevo_colaborador.setIntentosLogin(datosCliente.getIntentosLogin());
			nuevo_colaborador.setIntentosValidacion(datosCliente.getIntentosValidacion());
			nuevo_colaborador.setValidacionForm(TRUE);
			//nuevo_colaborador.setFomularioDatos(FALSE);
			//nuevo_colaborador.setMulta(FALSE);
			nuevo_colaborador.setDni(dniPersona.trim());
			nuevo_colaborador.setNombre(nombrePersona.trim());
			nuevo_colaborador.setApellido(apellidoPersona.trim());
			nuevo_colaborador.setTelefono(telefono.trim());
			nuevo_colaborador.setDomicilio(domicilio.trim());
			nuevo_colaborador.setFechaCreacion(LocalDateTime.now()); //En esta instancia esta fecha nos puede servir para saber cuando se dio de alta el usuario
			nuevo_colaborador.setFechaNacimiento(datosCliente.getFechaNacimiento());
			nuevo_colaborador.setSexo(sexo);
			nuevo_colaborador.setOcupacion(ocupacion);
				if (datos != null) { //se agrega este condicional, porque cuando nuevo cliente es por un cambio de rol, el valor dato es null
					repositorioCodigoDeVerificacion.delete(datos);//primero borramos los datos del usuario de la tabla de codigo
				}
				repositorioUsuario.delete(datosCliente); //segundo borramos todos los datos anteriores del usuario para que no choquen con el registro de los nuevos
				repositorioColaborador.save(nuevo_colaborador); //Guardamos los datos del usuario como un nuevo cliente
		}
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con el servidor en registrar clientes" + e.getMessage());
		}
		
	}
	
	
	
	@Transactional
	public void modificarColaborador(String idColaborador, String ocupacion, String email, String emailAnterior, String domicilio, String sexo, String telefono) throws MiExcepcion {
		
		verificarEmailColaborador(email, emailAnterior);
		validarActualizacionDeDatosColaborador(ocupacion, domicilio, sexo, telefono);
		
		
		try {
		Optional<Colaborador> identificarColaborador = repositorioColaborador.findById(idColaborador);
		if (identificarColaborador.isPresent()) {
			Colaborador colaborador_actualizado = identificarColaborador.get(); // Atribuye el objeto presente a esta nueva variable
			
			Sexo nuevoSexo = null;
			nuevoSexo = Sexo.valueOf(sexo.toUpperCase());
			
			colaborador_actualizado.setOcupacion(ocupacion);
			colaborador_actualizado.setSexo(nuevoSexo);
			colaborador_actualizado.setDomicilio(domicilio);
			colaborador_actualizado.setTelefono(telefono);
        		repositorioColaborador.save(colaborador_actualizado);
        	}
        	} catch (Exception e) {
        		throw new MiExcepcion("Error al conectar con el servidor " + e);
        	}
				
	}
	
	 
	
	 public void validarActualizacionDeDatosColaborador(String ocupacion, String domicilio, String sexo, String telefono) throws MiExcepcion {
			
		 // Expresión regular para validar un telefono
		 String regex = "\\d{7,10}";
		 
		 // Compilar la expresión regular
		 Pattern pattern = Pattern.compile(regex);
		 
		 // Crear un objeto Matcher
		 Matcher matcher = pattern.matcher(telefono);
		  
		 if (!matcher.matches()) {
			 throw new MiExcepcion("<span class='fs-6'>El telefono no cumple con el formato solicitado, por favor verifique e intente nuevamente.</span>");
		 } 
		 
		 if (ocupacion == null || ocupacion.isEmpty() || ocupacion.trim().isEmpty()) {
			 throw new MiExcepcion("<span class='fs-6'>El campo de la ocupación no puede estar vacío.</span>");
		 }
		 
		 if (domicilio == null || domicilio.isEmpty() || domicilio.trim().isEmpty()) {
			 throw new MiExcepcion("<span class='fs-6'>El campo de la dirección no puede estar vacío.</span>");
		 }
		 
		 if (telefono == null || telefono.toString().isEmpty() || telefono.toString().isEmpty()) {
			 throw new MiExcepcion("<span class='fs-6'>El campo del teléfono no puede estar vacío.</span>");
		 }
		 
	 }
	 
	 public void verificarEmailColaborador(String email, String emailAnterior) throws MiExcepcion {
		 
			// Expresión regular para validar un correo electrónico
		        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

		        // Compilar la expresión regular
		        Pattern pattern = Pattern.compile(regex);

		        // Crear un objeto Matcher
		        Matcher matcher = pattern.matcher(email);
		        
		        if (email == null || email.isEmpty() || email.trim().isEmpty()) {
		            throw new MiExcepcion("<span class='fs-6'>El email no puede estar vacio.</span>");
		        }

		        // Verificar si la cadena cumple con la expresión regular
		        if (!matcher.matches()) {
		            throw new MiExcepcion("<span class='fs-6'>El email no es valido, por favor verifique e intente nuevamente.</span>");
		        } 
		       
		        if (!email.equalsIgnoreCase(emailAnterior)) {
		        	if (repositorioUsuario.buscarPorEmailOptional(email).isPresent()) {
		        		throw new MiExcepcion("<span class='fs-6'>El email ingresado ya se encuentra registrado.</span>");
		        	}
	 
		        }
	 }
	 
	 public void validarDatosClienteColaborador(String ocupacion, String domicilio, String sexo, String telefono, String dni, String dniAnterior, String fechaNacimiento) throws MiExcepcion {
			
		 // Expresión regular para validar un telefono
		 String regex = "\\d{7,10}";
		 
		 // Compilar la expresión regular
		 Pattern pattern = Pattern.compile(regex);
		 
		 // Crear un objeto Matcher
		 Matcher matcher = pattern.matcher(telefono);
		  
		 if (!matcher.matches()) {
			 throw new MiExcepcion("<span class='fs-6'>El telefono no cumple con el formato solicitado, por favor verifique e intente nuevamente.</span>");
		 } 
		 
		 if (ocupacion == null || ocupacion.isEmpty() || ocupacion.trim().isEmpty()) {
			 throw new MiExcepcion("<span class='fs-6'>El campo de la ocupación no puede estar vacío.</span>");
		 }
		 
		 if (domicilio == null || domicilio.isEmpty() || domicilio.trim().isEmpty()) {
			 throw new MiExcepcion("<span class='fs-6'>El campo de la dirección no puede estar vacío.</span>");
		 }
		 
		 if (telefono == null || telefono.toString().isEmpty() || telefono.toString().isEmpty()) {
			 throw new MiExcepcion("<span class='fs-6'>El campo del teléfono no puede estar vacío.</span>");
		 }
		 
		 if (dni == null || dni.isEmpty() || dni.trim().isEmpty()) {
			 throw new MiExcepcion("El dni no puede estar vacio");
		 }
		 
		//Expresion regular para validar el numero de dni		 
		 String regexDni = "\\d{7,8}";
		 Pattern patternDni = Pattern.compile(regexDni);
		 Matcher matcherDni = patternDni.matcher(dni);
		 
		 if (!matcherDni.matches()) {
			 throw new MiExcepcion("<span class='fs-6'>El dni no cumple con el formato solicitado, por favor verifique e intente nuevamente.</span>");
		 } 
		 if (!dni.equalsIgnoreCase(dniAnterior)) { //solo si el dni ingresado es diferente al actual, hace la validacion si ya esta registrado en la base de datos
			 if(repositorioPersona.buscarPorDniOptional(dni).isPresent()) {
				 throw new MiExcepcion("El numero de dni ya está registrado");
			 }
		}
		 
		 
		 if (fechaNacimiento == null || fechaNacimiento.isEmpty() || fechaNacimiento.trim().isEmpty()) {
			 throw new MiExcepcion("La fecha de nacimiento no puedo estar vacía");
		 }
		 
	 }
	 
	
}



