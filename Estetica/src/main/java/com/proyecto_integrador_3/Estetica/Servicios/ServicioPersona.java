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
			String direccion, Integer telefono, String fechaNacimiento, String sexo) throws MiExcepcion {
		
		validarDatosPersona(nombre, apellido, dni, sexo, fechaNacimiento, telefono, direccion, ocupacion);

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
			nueva_persona.setDomicilio(direccion);
			nueva_persona.setFechaNacimiento(fecha);
			nueva_persona.setSexo(nuevoSexo);
			nueva_persona.setOcupacion(ocupacion);
			repositorioPersona.save(nueva_persona);
		
			String idPersona = nueva_persona.getId();
			System.out.println("IDPERSONA: " + idPersona);
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
					
	 public void validarDatosPersona(String nombre, String apellido, String dni,  String sexo,
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
}
