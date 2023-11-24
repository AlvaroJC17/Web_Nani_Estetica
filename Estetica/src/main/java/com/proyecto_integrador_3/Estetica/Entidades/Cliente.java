package com.proyecto_integrador_3.Estetica.Entidades;

import java.util.Date;

import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
public class Cliente extends Persona {

private Integer edad;
	
	//Constructores
	public Cliente(){
		
	}

	public Cliente(Integer edad) {
		super();
		this.edad = edad;
	}

	public Cliente(String id, String email, String contrasena, Rol rol, Boolean activo) {
		super(id, email, contrasena, rol, activo);
		
	}

	public Cliente(String id, String dni, String contrasena, String email, Rol rol, Boolean activo, String nombre,
			String apellido, Sexo sexo, Date fechaNacimiento, String domicilio, Integer telefono) {
		super(id, dni, contrasena, email, rol, activo, nombre, apellido, sexo, fechaNacimiento, domicilio, telefono);
		this.edad = edad;
	}

	//Setters y Getters
	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	//Metodos
	@Override
	public String toString() {
		return super.toString() + "Cliente [edad=" + edad + "]";
	}
}
