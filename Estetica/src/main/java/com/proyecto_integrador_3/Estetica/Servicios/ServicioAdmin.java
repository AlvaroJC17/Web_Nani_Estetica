package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioAdmin;

import jakarta.transaction.Transactional;

@Service
public class ServicioAdmin {

	@Autowired
	public RepositorioAdmin repositorioAdmin;
	
	@Transactional
	public void registrarAdmin(String dni, String email, Rol rol, Boolean activo, String nombre, String apellido,
			Sexo sexo, Date fechaNacimiento, String domicilio, Integer telefono) {
		
		Admin admin = new Admin();
		
		admin.setEmail(email);
		admin.setRol(rol.ADMIN);
		admin.setActivo(activo);
		admin.setDni(dni);
		admin.setNombre(nombre);
		admin.setApellido(apellido);
		admin.setTelefono(telefono);
		admin.setDomicilio(domicilio);
		admin.setFechaNacimiento(fechaNacimiento);
		admin.setSexo(sexo);
        repositorioAdmin.save(admin);
		
	}
	
	@Transactional
	public void modificarAdmin(Admin admin, String id, String dni, String email, Rol rol, Boolean activo, String nombre, String apellido,
			Sexo sexo, Date fechaNacimiento, String domicilio, Integer telefono ) {
		
		Optional<Admin> identificarAdmin = repositorioAdmin.findById(id);
		
		if (identificarAdmin.isPresent()) {
			Admin admin_modificado = new Admin();
			admin_modificado.setDni(dni);
			admin_modificado.setEmail(email);
			admin_modificado.setRol(rol);
			admin_modificado.setActivo(activo);
			admin_modificado.setNombre(nombre);
			admin_modificado.setApellido(apellido);
			admin_modificado.setSexo(sexo);
			admin_modificado.setFechaNacimiento(fechaNacimiento);
			admin_modificado.setDomicilio(domicilio);
			admin_modificado.setTelefono(telefono);
			
			repositorioAdmin.save(admin_modificado);
		}
		
	}
	
	public void borrarAdmin(Admin admin) {
		Optional <Admin> identificarAdmin = repositorioAdmin.findById(admin.getId().toString());
		
		if (identificarAdmin.isPresent()) {
			Admin adminDelete = identificarAdmin.get();
			repositorioAdmin.delete(adminDelete);
			
		}
	}
	
	 @Transactional
	    public void darBajaAdmin(String id) {
	        Optional<Admin> presente = repositorioAdmin.findById(id);

	        if (presente.isPresent()) {
	        	Admin admin = new Admin();
	        	admin = presente.get();
	        	admin.setActivo(FALSE); //El valor false se importa de un clase propia de java
	        	repositorioAdmin.save(admin);
	        }
	    }
	 
	 //Metodo para cambiar a usuarios que estaban inactivos a activos
	 @Transactional
	    public void darAltaAdmin(String id) {

	        Optional<Admin> presente = repositorioAdmin.findById(id);

	        if (presente.isPresent()) {
	        	Admin admin = new Admin();
	            admin = presente.get();
	            admin.setActivo(TRUE);
	            repositorioAdmin.save(admin);
	        }
	    }
	
}
