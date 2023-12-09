package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
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
	
	@Transactional
	public void registrarAdmin(String email, String dni, String nombre, String apellido, String ocupacion,
			String direccion, Integer telefono, String fechaNacimiento, String sexo) throws MiExcepcion {
		
		validarDatosAdmin(nombre, apellido, dni, sexo, fechaNacimiento, telefono, direccion, ocupacion);

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
			
			Admin nuevo_admin = new Admin();
			nuevo_admin.setEmail(email);
			nuevo_admin.setContrasena(datosCliente.getContrasena());
			nuevo_admin.setRol(datosCliente.getRol());
			nuevo_admin.setActivo(datosCliente.getActivo());
			nuevo_admin.setValidacionForm(TRUE);
			nuevo_admin.setDni(dni);
			nuevo_admin.setNombre(nombre);
			nuevo_admin.setApellido(apellido);
			nuevo_admin.setTelefono(telefono);
			nuevo_admin.setDomicilio(direccion);
			nuevo_admin.setFechaNacimiento(fecha);
			nuevo_admin.setSexo(nuevoSexo);
			nuevo_admin.setOcupacion(ocupacion);
			repositorioAdmin.save(nuevo_admin);
			repositorioUsuario.delete(datosCliente);
		}
	}
	
	/*
	@Transactional
	public List<Admin> buscarDni(String dni) {
		List<Admin> dniAdmin = repositorioAdmin.buscarPorDni(dni);
	return dniAdmin;
	}
	
	//Actualiza los datos de un Admin en la base de datos
	@Transactional
	public void modificarAdmin(Admin admin ) {
		
		Optional<Admin> identificarAdmin = repositorioAdmin.findById(admin.getId());
		if (identificarAdmin.isPresent()) {
			Admin admin_modificado = identificarAdmin.get(); // Atribuye el objeto presente a esta nueva variable
			admin_modificado.setDni(admin.getDni());
			admin_modificado.setEmail(admin.getEmail());
			admin_modificado.setRol(admin.getRol().ADMIN);
			admin_modificado.setActivo(admin.getActivo());
			admin_modificado.setNombre(admin.getNombre());
			admin_modificado.setApellido(admin.getApellido());
			admin_modificado.setSexo(admin.getSexo());
			admin_modificado.setFechaNacimiento(admin.getFechaNacimiento());
			admin_modificado.setDomicilio(admin.getApellido());
			admin_modificado.setTelefono(admin.getTelefono());
			repositorioAdmin.save(admin_modificado);
		}
	}
	
	 
*/
	 
	 
	 public void validarDatosAdmin(String nombre, String apellido, String dni,  String sexo,
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
