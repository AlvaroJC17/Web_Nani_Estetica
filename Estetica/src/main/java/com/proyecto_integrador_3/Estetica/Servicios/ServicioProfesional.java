package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.TRUE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
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
	public void registrarProfesional(String email, String matricula, String especialidad, String dni, String nombre, String apellido,
			String direccion, Integer telefono, String fechaNacimiento, String sexo) throws MiExcepcion {
		
		validarDatosProfesional(nombre, apellido, dni, matricula, especialidad, sexo, fechaNacimiento, telefono, direccion);

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
			
			Profesional nuevo_profesional = new Profesional();
			nuevo_profesional.setEmail(email);
			nuevo_profesional.setContrasena(datosCliente.getContrasena());
			nuevo_profesional.setRol(datosCliente.getRol());
			nuevo_profesional.setActivo(datosCliente.getActivo());
			nuevo_profesional.setValidacionForm(TRUE);
			nuevo_profesional.setDni(dni);
			nuevo_profesional.setNombre(nombre);
			nuevo_profesional.setApellido(apellido);
			nuevo_profesional.setMatricula(matricula);
			nuevo_profesional.setEspecialidad(especialidad);;
			nuevo_profesional.setTelefono(telefono);
			nuevo_profesional.setDomicilio(direccion);
			nuevo_profesional.setFechaNacimiento(fecha);
			nuevo_profesional.setSexo(nuevoSexo);
			repositorioProfesional.save(nuevo_profesional);
			repositorioUsuario.delete(datosCliente);
		}
	}
	
	 public void validarDatosProfesional(String nombre, String apellido, String dni, String matricula, String especialidad,  String sexo,
			 String fechaNacimiento, Integer telefono, String direccion) throws MiExcepcion {
	
		 
		 if (nombre == null || nombre.isEmpty() || nombre.trim().isEmpty()) {
			 throw new MiExcepcion("El nombre no puede estar vacio");
		 }
		 if (apellido == null || apellido.isEmpty() || apellido.trim().isEmpty()) {
			 throw new MiExcepcion("El apellido no puede estar vacio");
		 }
		 if (dni == null || dni.isEmpty() || dni.trim().isEmpty()) {
			 throw new MiExcepcion("El dni no puede estar vacio");
		 }
		 if (matricula == null || matricula.isEmpty() || matricula.trim().isEmpty()) {
			 throw new MiExcepcion("La matricula no puede estar vacia");
		 }
		 if (especialidad == null || especialidad.isEmpty() || especialidad.trim().isEmpty()) {
			 throw new MiExcepcion("La especialidad no puede estar vacia");
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
		
	 
 }
	
}
