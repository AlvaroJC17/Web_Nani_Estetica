package com.proyecto_integrador_3.Estetica.Entidades;

import java.util.Date;

import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.Enums.Tratamiento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
public class Profesional extends Persona {


	private String matricula;

    @Enumerated(EnumType.STRING)
    private Tratamiento tratamientos;
    
    @Enumerated(EnumType.STRING)
    private Provincias provincia;
    
    private Double calificacion;

    private Double precioConsulta;
    
    public Profesional() {
    	
    }

	public Profesional(String matricula, Tratamiento tratamientos, Provincias provincia, Double calificacion,
			Double precioConsulta) {
		this.matricula = matricula;
		this.tratamientos = tratamientos;
		this.provincia = provincia;
		this.calificacion = calificacion;
		this.precioConsulta = precioConsulta;
	}

	public Profesional(String id, String dni, String contrasena, String email, Rol rol, Boolean activo, Boolean ValidacionForm, String nombre,
			String apellido, String ocupacion, Sexo sexo, Date fechaNacimiento, String domicilio, Integer telefono) {
		super(id, dni, contrasena, email, rol, activo, ValidacionForm, nombre, apellido, ocupacion, sexo, fechaNacimiento, domicilio, telefono);

	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public Tratamiento getTratamientos() {
		return tratamientos;
	}

	public void setTratamientos(Tratamiento tratamientos) {
		this.tratamientos = tratamientos;
	}

	public Provincias getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincias provincia) {
		this.provincia = provincia;
	}

	public Double getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(Double calificacion) {
		this.calificacion = calificacion;
	}

	public Double getPrecioConsulta() {
		return precioConsulta;
	}

	public void setPrecioConsulta(Double precioConsulta) {
		this.precioConsulta = precioConsulta;
	}

	@Override
	public String toString() {
		return super.toString() + "Profesional [matricula=" + matricula + ", tratamientos=" + tratamientos + ", provincia=" + provincia
				+ ", calificacion=" + calificacion + ", precioConsulta=" + precioConsulta + "]";
	}
	
	
    
	
    
}
