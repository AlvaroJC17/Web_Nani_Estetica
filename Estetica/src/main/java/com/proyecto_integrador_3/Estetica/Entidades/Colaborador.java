package com.proyecto_integrador_3.Estetica.Entidades;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;

import jakarta.persistence.Entity;

@Entity
public class Colaborador extends Persona{

	public Colaborador() {
		
	}
	
	public Colaborador(String id, List<TokenUsuario> tokens, String email, String contrasena, Rol rol, Boolean activo, Boolean ValidacionForm, 
			String dni, String nombre, String apellido, String ocupacion, Sexo sexo, LocalDate fechaNacimiento,
			String domicilio, Integer telefono, Boolean emailValidado, Boolean revalidarEmail, LocalDateTime fechaCreacion, int intentosValidacion,
			Boolean bloqueoValidacion, LocalDateTime horaDeBloqueoValidacion,  int intentosLogin, Boolean bloqueoLogin, LocalDateTime horaBloqueoLogin ) {
		super(id, tokens, email, contrasena, rol, activo, ValidacionForm, fechaNacimiento, emailValidado, revalidarEmail, fechaCreacion, intentosValidacion,  
				bloqueoValidacion,  horaDeBloqueoValidacion, intentosLogin, bloqueoLogin, horaBloqueoLogin);
	}
}
