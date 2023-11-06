package com.proyecto_integrador_3.Estetica.Entidades;

import java.util.Date;

import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;

import jakarta.persistence.Entity;


@Entity
public class Cliente extends Persona {

private Integer edad;
	
	//Constructores
	public Cliente(){
		
	}

	
	public Cliente(String id, String dni, String email, Rol rol, Boolean activo, String nombre, String apellido,
			Integer edad, Sexo sexo, Date fechaNacimiento, String domicilio, Integer telefono) {
		super(id, dni, email, rol, activo, nombre, apellido, sexo, fechaNacimiento, domicilio, telefono);
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
