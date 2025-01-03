package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.TRUE;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioAdmin;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;

import jakarta.transaction.Transactional;

@Service
public class ServicioAdmin {

	@Autowired
	public RepositorioAdmin repositorioAdmin;
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	@Autowired
	public RepositorioUsuario repositorioUsuario;
	
	@Autowired
	public RepositorioCliente repositorioCliente;
	
	@Autowired
	public RepositorioPersona repositorioPersona;
	
	public Admin buscarDatosAdmin(String idAdmin)throws MiExcepcion {
		Optional<Admin> buscarAdmin = repositorioAdmin.findById(idAdmin);
		if (buscarAdmin.isPresent()) {
			Admin datosDelAdmin = buscarAdmin.get();
			return datosDelAdmin;
		}else {
			throw new MiExcepcion("No se encontro al Admin");
		}
	}
	
	@Transactional
	public void registrarAdmin(String email) throws MiExcepcion {
		
		//validarDatosAdmin(sexo, telefono, direccion, ocupacion); //No lo validamos de vuelta porque ya se valida el registrar el cliente

		try {
		Optional <Usuario> datosUsuario = servicioUsuario.buscarPorEmailOptional(email);
		if (datosUsuario.isPresent()) {
			Usuario datosDelUsuario = datosUsuario.get();
			
			//Buscamos los datos de nombre, apellido, fechaNacimiento y dni de la persona y los guardamos como nuevo profesional, asi la persona no los tiene que ingresar de nuevo en el formulario
			String nombrePersona = null;
			String apellidoPersona = null;
			String dniPersona = null;
			String telefono = null;
			String domicilio = null;
			String ocupacion = null;
			Sexo sexo = null;
			LocalDate fechaNacimientoPerson = null;
			
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
				fechaNacimientoPerson = datosPersonalesPersona.getFechaNacimiento();
				sexo = datosPersonalesPersona.getSexo();
			}
			
			Admin nuevo_admin = new Admin();
			nuevo_admin.setEmail(email.trim());
			nuevo_admin.setContrasena(datosDelUsuario.getContrasena());
			nuevo_admin.setRol(datosDelUsuario.getRol());
			nuevo_admin.setActivo(datosDelUsuario.getActivo());
			nuevo_admin.setEmailValidado(datosDelUsuario.getEmailValidado());
			nuevo_admin.setFechaCreacion(datosDelUsuario.getFechaCreacion());
			nuevo_admin.setValidacionForm(TRUE);
			nuevo_admin.setDni(dniPersona);
			nuevo_admin.setNombre(nombrePersona);
			nuevo_admin.setApellido(apellidoPersona);
			nuevo_admin.setTelefono(telefono.trim());
			nuevo_admin.setDomicilio(domicilio.trim());
			nuevo_admin.setFechaNacimiento(fechaNacimientoPerson);
			nuevo_admin.setSexo(sexo);
			nuevo_admin.setOcupacion(ocupacion.trim());
			repositorioAdmin.save(nuevo_admin);
			repositorioUsuario.delete(datosDelUsuario);
		}
		} catch (Exception e) {
			System.out.println("Error al conectar con el servidor " + e);
		}
	}
				
	
	@Transactional
	public void modificarAdmin(String idAdmin, String ocupacion, String email, String emailAnterior, String domicilio, String sexo, String telefono) throws MiExcepcion {
		
		verificarEmailAdmin(email, emailAnterior);
		validarActualizacionDeDatosAdmin(ocupacion, domicilio, sexo, telefono);
		
		
		try {
		Optional<Admin> identificarAdmin = repositorioAdmin.findById(idAdmin);
		if (identificarAdmin.isPresent()) {
			Admin admin_actualizado = identificarAdmin.get(); // Atribuye el objeto presente a esta nueva variable
			
			Sexo nuevoSexo = null;
			nuevoSexo = Sexo.valueOf(sexo.toUpperCase());
			
			admin_actualizado.setEmail(email);
			admin_actualizado.setOcupacion(ocupacion);
			admin_actualizado.setSexo(nuevoSexo);
			admin_actualizado.setDomicilio(domicilio);
			admin_actualizado.setTelefono(telefono);
			repositorioAdmin.save(admin_actualizado);
		}
		} catch (Exception e) {
			System.out.println("Error al conectar con el servidor " + e);
		}
				
	}
	 
	 public void validarDatosAdmin(String sexo, String telefono, String direccion, String ocupacion) throws MiExcepcion {
		 
		 // Expresión regular para validar un telefono
		 String regex = "\\d{7,10}";
		 
		 // Compilar la expresión regular
		 Pattern pattern = Pattern.compile(regex);
		 
		 // Crear un objeto Matcher
		 Matcher matcher = pattern.matcher(telefono);
		 
		 if (!matcher.matches()) {
			 throw new MiExcepcion("<span class='fs-6'>El telefono no cumple con el formato solicitado, por favor verifique e intente nuevamente.</span>");
		 } 
	        
		 if (sexo == null || sexo.isEmpty() || sexo.trim().isEmpty() || sexo.equals("Seleccione")) {
			 throw new MiExcepcion("El sexo no puede estar vacio");
		 }

		 if (telefono == null || telefono.isEmpty() || telefono.trim().isEmpty()) {
			 throw new MiExcepcion("EL telefono no puede estar vacio");
		 }
		 if (direccion == null || direccion.isEmpty() || direccion.trim().isEmpty()) {
			 throw new MiExcepcion("La dirección no puede estar vacia");
		 }
		 if (ocupacion == null || ocupacion.isEmpty() || ocupacion.trim().isEmpty()) {
			 throw new MiExcepcion("La ocupacion no puede estar vacio");
		 }
	 
	 }
	 
	 public void validarActualizacionDeDatosAdmin(String ocupacion, String domicilio, String sexo, String telefono) throws MiExcepcion {
		 
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
			 throw new MiExcepcion("La ocupacion no puede estar vacia");
		 }
		 
		 if (domicilio == null || domicilio.isEmpty() || domicilio.trim().isEmpty()) {
			 throw new MiExcepcion("El domicilio no puede estar vacio");
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
		 
	 
	 public void verificarEmailAdmin(String email, String emailAnterior) throws MiExcepcion {
		 
			// Expresión regular para validar un correo electrónico
		        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

		        // Compilar la expresión regular
		        Pattern pattern = Pattern.compile(regex);

		        // Crear un objeto Matcher
		        Matcher matcher = pattern.matcher(email);
		        
		        if (!matcher.matches()) {
		            throw new MiExcepcion("<span class='fs-6'>El email no es valido, por favor verifique e intente nuevamente.</span>");
		        } 
		        
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
