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

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioProfesional;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;

import jakarta.transaction.Transactional;

@Service
public class ServicioProfesional {
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	@Autowired
	public RepositorioUsuario repositorioUsuario;
	
	@Autowired
	public RepositorioProfesional repositorioProfesional;
	
	@Autowired
	public RepositorioPersona repositorioPersona;

	@Transactional
	public void registrarProfesional(String email, String matricula, String especialidad,
			String direccion, Integer telefono, String sexo) throws MiExcepcion {
		
		validarDatosProfesional(matricula, especialidad, sexo,telefono, direccion);

		Optional <Usuario> datosUsuario = servicioUsuario.buscarPorEmailOptional(email);
		if (datosUsuario.isPresent()) {
			Usuario datosDelUsuario = datosUsuario.get();
			
			//Buscamos los datos de nombre, apellido, fechaNacimiento y dni de la persona y los guardamos como nuevo profesional, asi la persona no los tiene que ingresar de nuevo en el formulario
			String nombrePersona = null;
			String apellidoPersona = null;
			String dniPersona = null;
			Date fechaNacimientoPerson = null;
			Optional <Persona> datosPersona = repositorioPersona.buscarPorEmailOptional(email);
			if (datosPersona.isPresent()) {
				Persona datosPersonalesPersona = datosPersona.get();
				nombrePersona = datosPersonalesPersona.getNombre();
				apellidoPersona = datosPersonalesPersona.getApellido();
				dniPersona = datosPersonalesPersona.getDni();
				fechaNacimientoPerson = datosPersonalesPersona.getFechaNacimiento();
			}
		
			Sexo nuevoSexo = null;
			nuevoSexo = Sexo.valueOf(sexo.toUpperCase());
			
		/*	SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			Date fecha = null;
			try {
				fecha = formato.parse(fechaNacimiento);
			} catch (ParseException e) {
				e.printStackTrace();
			} */
			
			Profesional nuevo_profesional = new Profesional();
			nuevo_profesional.setEmail(email);
			nuevo_profesional.setContrasena(datosDelUsuario.getContrasena());
			nuevo_profesional.setRol(datosDelUsuario.getRol());
			nuevo_profesional.setActivo(datosDelUsuario.getActivo());
			nuevo_profesional.setValidacionForm(TRUE);
			nuevo_profesional.setDni(dniPersona);
			nuevo_profesional.setNombre(nombrePersona);
			nuevo_profesional.setApellido(apellidoPersona);
			nuevo_profesional.setMatricula(matricula);
			nuevo_profesional.setEspecialidad(especialidad);;
			nuevo_profesional.setTelefono(telefono);
			nuevo_profesional.setDomicilio(direccion);
			nuevo_profesional.setFechaNacimiento(fechaNacimientoPerson);
			nuevo_profesional.setSexo(nuevoSexo);
			repositorioProfesional.save(nuevo_profesional);
			repositorioUsuario.delete(datosDelUsuario);
		}
	}
	
	@Transactional
	public void modificarProfesional(String idAdmin, String email, String emailAnterior, String domicilio, String sexo, Integer telefono) throws MiExcepcion {
		
		verificarEmailProfesional(email, emailAnterior);
		validarActualizacionDeDatosProfesional(domicilio, sexo, telefono);
		
		Optional<Profesional> identificarProfesional = repositorioProfesional.findById(idAdmin);
		
		if (identificarProfesional.isPresent()) {
			Profesional profesional_actualizado = identificarProfesional.get(); // Atribuye el objeto presente a esta nueva variable
			
			Sexo nuevoSexo = null;
			nuevoSexo = Sexo.valueOf(sexo.toUpperCase());
			
			profesional_actualizado.setEmail(email);
			profesional_actualizado.setSexo(nuevoSexo);
			profesional_actualizado.setDomicilio(domicilio);
			profesional_actualizado.setTelefono(telefono);
        	repositorioProfesional.save(profesional_actualizado);
		}
	}
	
	 public void validarDatosProfesional(String matricula, String especialidad,  String sexo, Integer telefono, String direccion) throws MiExcepcion {
	
		 
	/*	 if (nombre == null || nombre.isEmpty() || nombre.trim().isEmpty()) {
			 throw new MiExcepcion("El nombre no puede estar vacio");
		 }
		 if (apellido == null || apellido.isEmpty() || apellido.trim().isEmpty()) {
			 throw new MiExcepcion("El apellido no puede estar vacio");
		 }
		 if (dni == null || dni.isEmpty() || dni.trim().isEmpty()) {
			 throw new MiExcepcion("El dni no puede estar vacio"); 
		 } */
		 
		 if (matricula == null || matricula.isEmpty() || matricula.trim().isEmpty()) {
			 throw new MiExcepcion("La matricula no puede estar vacia");
		 }
		 if (especialidad == null || especialidad.isEmpty() || especialidad.trim().isEmpty()) {
			 throw new MiExcepcion("La especialidad no puede estar vacia");
		 }
		 
		/* if(repositorioPersona.buscarPorDniOptional(dni).isPresent()) {
			 throw new MiExcepcion("El numero de dni ya está registrado");
		 }*/
		 
		 if (sexo == null || sexo.isEmpty() || sexo.trim().isEmpty() || sexo.equals("Seleccione")) {
			 throw new MiExcepcion("El sexo no puede estar vacio");
		 }
		 /*
		 if (fechaNacimiento == null || fechaNacimiento.toString().isEmpty() || fechaNacimiento.trim().isEmpty()) {
			 throw new MiExcepcion("La fecha de nacimiento no puede estar vacia");
		 }*/
		 
		 if (telefono == null || telefono.toString().isEmpty() || telefono.toString().isEmpty()) {
			 throw new MiExcepcion("EL telefono no puede estar vacio");
		 }
		 if (direccion == null || direccion.isEmpty() || direccion.trim().isEmpty()) {
			 throw new MiExcepcion("La dirección no puede estar vacia");
		 }
	 }

	 
	 public void validarActualizacionDeDatosProfesional(String domicilio, String sexo, Integer telefono) throws MiExcepcion {
		/*
		 if (ocupacion == null || ocupacion.isEmpty() || ocupacion.trim().isEmpty()) {
			 throw new MiExcepcion("La ocupacion no puede estar vacia");
		 }
		 */
		 
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
	 
	 public void verificarEmailProfesional(String email, String emailAnterior) throws MiExcepcion {
		 
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
