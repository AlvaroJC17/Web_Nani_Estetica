package com.proyecto_integrador_3.Estetica.Entidades;

import java.util.Date;

import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
public class Cliente extends Persona {

	//Constructores
	public Cliente(){
		
	}


	public Cliente(String id, String email, String contrasena, Rol rol, Boolean activo, Boolean ValidacionForm) {
		super(id, email, contrasena, rol, activo, ValidacionForm);
		
	}

	

	//Setters y Getters


	public Cliente(String dni, String nombre, String apellido, String ocupacion, Sexo sexo, Date fechaNacimiento,
			String domicilio, Integer telefono) {
		super(dni, nombre, apellido, ocupacion, sexo, fechaNacimiento, domicilio, telefono);
		
	}


	//Metodos
	@Override
	public String toString() {
		return super.toString();
	}


	
}
