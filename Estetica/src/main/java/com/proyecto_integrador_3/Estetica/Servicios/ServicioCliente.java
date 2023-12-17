package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.TRUE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
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
	private RepositorioPersona repositorioPersona;
	
	@Transactional
	public void registrarCliente(String email, String dni, String nombre, String apellido, String ocupacion,
			String direccion, Integer telefono, String fechaNacimiento, String sexo) throws MiExcepcion {
		
		validarDatosCliente(nombre, apellido, dni, sexo, fechaNacimiento, telefono, direccion, ocupacion);

		Optional <Usuario> datosUsuario = servicioUsuario.buscarPorEmailOptional(email);
		if (datosUsuario.isPresent()) {
			Usuario datosCliente = datosUsuario.get();
		
			Sexo nuevoSexo = null;
			nuevoSexo = Sexo.valueOf(sexo.toUpperCase());
			
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			Date fecha = null;
			try {
				fecha = formato.parse(fechaNacimiento);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			Cliente nuevo_cliente = new Cliente();
			nuevo_cliente.setEmail(email);
			nuevo_cliente.setContrasena(datosCliente.getContrasena());
			nuevo_cliente.setRol(datosCliente.getRol());
			nuevo_cliente.setActivo(datosCliente.getActivo());
			nuevo_cliente.setValidacionForm(TRUE);
			nuevo_cliente.setDni(dni);
			nuevo_cliente.setNombre(nombre);
			nuevo_cliente.setApellido(apellido);
			nuevo_cliente.setTelefono(telefono);
			nuevo_cliente.setDomicilio(direccion);
			nuevo_cliente.setFechaNacimiento(fecha);
			nuevo_cliente.setSexo(nuevoSexo);
			nuevo_cliente.setOcupacion(ocupacion);
			repositorioCliente.save(nuevo_cliente);
			repositorioUsuario.delete(datosCliente);
		}
	}
			
	

	@Transactional
	public void modificarCliente(String idCliente, String ocupacion, String email, String emailAnterior, String domicilio, String sexo, Integer telefono) throws MiExcepcion {
		
		verificarEmailCliente(email, emailAnterior);
		validarActualizacionDeDatosCliente(ocupacion, domicilio, sexo, telefono);
		
		Optional<Cliente> identificarCliente = repositorioCliente.findById(idCliente);
		
		if (identificarCliente.isPresent()) {
			Cliente cliente_actualizado = identificarCliente.get(); // Atribuye el objeto presente a esta nueva variable
			
			Sexo nuevoSexo = null;
			nuevoSexo = Sexo.valueOf(sexo.toUpperCase());
			
			cliente_actualizado.setEmail(email);
        	cliente_actualizado.setOcupacion(ocupacion);
        	cliente_actualizado.setSexo(nuevoSexo);
        	cliente_actualizado.setDomicilio(domicilio);
        	cliente_actualizado.setTelefono(telefono);
        	repositorioCliente.save(cliente_actualizado);
		}
	}

        		

	 public void validarDatosCliente(String nombre, String apellido, String dni,  String sexo,
			 String fechaNacimiento, Integer telefono, String direccion, String ocupacion) throws MiExcepcion {
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
		 if (fechaNacimiento == null || fechaNacimiento.toString().isEmpty() || fechaNacimiento.trim().isEmpty()) {
			 throw new MiExcepcion("La fecha de nacimiento no puede estar vacia");
		 }
		 if (telefono == null || telefono.toString().isEmpty() || telefono.toString().isEmpty()) {
			 throw new MiExcepcion("EL telefono no puede estar vacio");
		 }
		 if (direccion == null || direccion.isEmpty() || direccion.trim().isEmpty()) {
			 throw new MiExcepcion("La dirección no puede estar vacia");
		 }
		 if (ocupacion == null || ocupacion.isEmpty() || ocupacion.trim().isEmpty()) {
			 throw new MiExcepcion("La ocupacion no puede estar vacio");
		 }
	 }
	 	 
	 public void validarActualizacionDeDatosCliente(String ocupacion, String domicilio, String sexo, Integer telefono) throws MiExcepcion {
		 if (ocupacion == null || ocupacion.isEmpty() || ocupacion.trim().isEmpty()) {
			 throw new MiExcepcion("La ocupacion no puede estar vacio");
		 }
		 
		 if (domicilio == null || domicilio.isEmpty() || domicilio.trim().isEmpty()) {
			 throw new MiExcepcion("La direccion no puede estar vacia");
		 }
		 
		 if (telefono == null || telefono.toString().isEmpty() || telefono.toString().isEmpty()) {
			 throw new MiExcepcion("EL telefono no puede estar vacio");
		 }
		 
		 if (sexo == null || sexo.isEmpty() || sexo.trim().isEmpty() || sexo.equals("Seleccione")) {
			 throw new MiExcepcion("El sexo no puede estar vacio");
		 }
		 
		 if (!sexo.equalsIgnoreCase("masculino") && !sexo.equalsIgnoreCase("femenino") && !sexo.equalsIgnoreCase("otro")) {
			 throw new MiExcepcion("El sexo solo puede ser masculino, femenino u otro");
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
		            throw new MiExcepcion("El email no puede estar vacio");
		        }

		        // Verificar si la cadena cumple con la expresión regular
		        if (!matcher.matches()) {
		            throw new MiExcepcion("El correo electronico no es valido");
		        } 
		       
		        if (!email.equalsIgnoreCase(emailAnterior)) {
		        	if (repositorioUsuario.buscarPorEmailOptional(email).isPresent()) {
		        		throw new MiExcepcion("El email ingresado ya se encuentra registrado, por favor ingrese otro");
		        	}
						
		        }
		        
	 }
	 
	 
		        	
	 
	 



}
