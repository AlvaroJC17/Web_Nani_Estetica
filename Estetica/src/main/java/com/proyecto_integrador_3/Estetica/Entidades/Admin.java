package com.proyecto_integrador_3.Estetica.Entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;

import jakarta.persistence.Entity;

@Entity
public class Admin extends Persona implements Serializable {

	public Admin() {
		
	}

	public Admin(String id, List<TokenUsuario> tokens, String dni, String contrasena, String email, Rol rol, Boolean activo, Boolean ValidacionForm, String nombre,
			String apellido, String ocupacion, Sexo sexo, Date fechaNacimiento, String domicilio, String telefono) {
		super(id, tokens, dni, contrasena, email, rol, activo, ValidacionForm, nombre, apellido, ocupacion, sexo, fechaNacimiento, domicilio, telefono);
		
	}
	
	public Admin(String id, String contrasena, String email, Rol rol, Boolean activo, String nombre, String apellido) {
		super(id, contrasena, email, rol, activo, nombre, apellido);
	}

	
	
	
	
	
}
