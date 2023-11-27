package com.proyecto_integrador_3.Estetica.Entidades;

import java.util.Date;

import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;

import jakarta.persistence.Entity;

@Entity
public class Admin extends Persona {

	public Admin() {
		
	}

	public Admin(String id, String dni, String contrasena, String email, Rol rol, Boolean activo, Boolean ValidacionForm, String nombre,
			String apellido, String ocupacion, Sexo sexo, Date fechaNacimiento, String domicilio, Integer telefono) {
		super(id, dni, contrasena, email, rol, activo, ValidacionForm, nombre, apellido, ocupacion, sexo, fechaNacimiento, domicilio, telefono);
		
	}

	
	
	
	
	
}
