package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;

import jakarta.transaction.Transactional;

@Service
public class ServicioPersona {

	@Autowired
	public RepositorioPersona repositorioPersona;
	
	@Autowired
	public RepositorioUsuario repositorioUsuario;
	
	@Transactional
	public String registrarPersona(String dni, String email, String nombre, String apellido, String ocupacion,
			String domicilio, Integer telefono, String fechaNacimiento, String sexo) throws MiExcepcion {
		
		validarDatosPersona(dni, email, nombre, apellido,ocupacion, domicilio, telefono);

			Sexo nuevoSexo = null;
			nuevoSexo = Sexo.valueOf(sexo.toUpperCase());
			
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			Date fecha = null;
			try {
				fecha = formato.parse(fechaNacimiento);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			Persona nueva_persona = new Persona();
			nueva_persona.setDni(dni);
			nueva_persona.setNombre(nombre);
			nueva_persona.setApellido(apellido);
			nueva_persona.setTelefono(telefono);
			nueva_persona.setDomicilio(domicilio);
			nueva_persona.setFechaNacimiento(fecha);
			nueva_persona.setSexo(nuevoSexo);
			nueva_persona.setOcupacion(ocupacion);
			repositorioPersona.save(nueva_persona);
		
			String idPersona = nueva_persona.getId();
			return idPersona;
		}
	
	@Transactional
	public void validarForm(String email, String idPersona) {
		
		//Buscamos el usuario por mail y obtenemos sus valores
		Optional<Usuario> usuarioOptional = repositorioUsuario.buscarPorEmailOptional(email);
		if (usuarioOptional.isPresent()) {
			Usuario usuario = usuarioOptional.get();
	
		//Buscamos el usuario que jpa genero en blanco por el id, que viene siedno el mismo id de la tabla de persona
		//Le asiganamos a este nuevoUsuario  los valores obtenidos de usuario y los actualizamos en la base de datos
		// Por ultimo borramos el usuario anterior	
		Optional<Usuario> nuevoUsuarioOptional = repositorioUsuario.findById(idPersona);
		if (nuevoUsuarioOptional.isPresent()) {
			Usuario nuevoUsuario = nuevoUsuarioOptional.get() ;
			nuevoUsuario.setEmail(usuario.getEmail());
			nuevoUsuario.setContrasena(usuario.getContrasena());
			nuevoUsuario.setActivo(TRUE);
			nuevoUsuario.setRol(Rol.CLIENTE);
			nuevoUsuario.setValidacionForm(TRUE);
	        repositorioUsuario.save(nuevoUsuario);
	        repositorioUsuario.delete(usuario);
		}
		}
	}
	
	//Borra una persona de la base de datos
			public void borrarPersona(String id) {
				Optional <Persona> identificarPersona = repositorioPersona.findById(id);
				
				if (identificarPersona.isPresent()) {
					Persona personaDelete = identificarPersona.get();
					repositorioPersona.delete(personaDelete);
					
				}
			}
			

	
	 public static void validarDatosPersona(String dni, String email, String nombre, String apellido,
			 String ocupacion, String domicilio, Integer telefono) throws MiExcepcion {
	 
	 if (dni == null || dni.isEmpty() || dni.trim().isEmpty()) {
		throw new MiExcepcion("El dni no puede estar vacio");
	}
	 if (email == null || email.isEmpty() || email.trim().isEmpty()) {
			throw new MiExcepcion("El email no puede estar vacio");
		}
	 if (nombre == null || nombre.isEmpty() || nombre.trim().isEmpty()) {
			throw new MiExcepcion("El nombre no puede estar vacio");
		}
	 if (apellido == null || apellido.isEmpty() || apellido.trim().isEmpty()) {
			throw new MiExcepcion("El apellido no puede estar vacio");
		}
	 if (ocupacion == null || ocupacion.isEmpty() || ocupacion.trim().isEmpty()) {
			throw new MiExcepcion("La ocupacion no puede estar vacio");
		}
	 if (domicilio == null || domicilio.isEmpty() || domicilio.trim().isEmpty()) {
			throw new MiExcepcion("El domicilio no puede estar vacio");
		}
	 if (telefono == null || telefono.toString().isEmpty() || telefono.toString().isEmpty()) {
			throw new MiExcepcion("EL telefono no puede estar vacio");
		}
	 
	 
 }
}
