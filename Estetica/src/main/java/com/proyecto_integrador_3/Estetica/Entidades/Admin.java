package com.proyecto_integrador_3.Estetica.Entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;

import jakarta.persistence.Entity;

@Entity
public class Admin extends Persona implements Serializable {

	public Admin() {
		
	}

	public Admin(String id, List<TokenUsuario> tokens, String dni, String contrasena, String email, Rol rol, Boolean activo, Boolean ValidacionForm, String nombre,
			String apellido, String ocupacion, Sexo sexo, LocalDate fechaNacimiento, Boolean emailValidado, Boolean revalidarEmail, LocalDateTime fechaCreacion, int intentosValidacion,
			 Boolean bloqueoValidacion, LocalDateTime horaDeBloqueoValidacion, int intentosLogin, Boolean bloqueoLogin, LocalDateTime horaBloqueoLogin,
			 String domicilio, String telefono) {
		super(id, tokens, dni, contrasena, email, rol, activo, ValidacionForm, nombre, apellido, ocupacion, sexo, fechaNacimiento,  emailValidado, revalidarEmail,
				fechaCreacion, intentosValidacion, bloqueoValidacion, horaDeBloqueoValidacion, intentosLogin, bloqueoLogin, horaBloqueoLogin, domicilio, telefono);
	}
		
	
	public Admin(String id, String contrasena, String email, Rol rol, Boolean activo, String nombre, String apellido) {
		super(id, contrasena, email, rol, activo, nombre, apellido);
	}

	
	
	
	
	
}
