package com.proyecto_integrador_3.Estetica.Entidades;

import java.io.Serializable;
import java.util.Date;

import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.Enums.Tratamiento;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Profesional")
public class Profesional extends Persona implements Serializable {

	@Column(name = "matricula")
	private String matricula;
	
	@Column(name = "especialidad")
	private String especialidad;

    @Enumerated(EnumType.STRING)
    private Tratamiento tratamientos;
    
    @Enumerated(EnumType.STRING)
    private Provincias provincia;
    
    @Column(name = "calificacion")
    private Double calificacion;

    @Column(name = "precioConsulta")
    private Double precioConsulta;
    
  
    
    public Profesional() {
    	
    }

	public Profesional(String matricula, String especialidad, Tratamiento tratamientos, Provincias provincia, Double calificacion,
			Double precioConsulta) {
		this.matricula = matricula;
		this.especialidad = especialidad;
		this.tratamientos = tratamientos;
		this.provincia = provincia;
		this.calificacion = calificacion;
		this.precioConsulta = precioConsulta;
	}

	public Profesional(String id, String dni, String contrasena, String email, Rol rol, Boolean activo, Boolean ValidacionForm, String nombre,
			String apellido, String ocupacion, Sexo sexo, Date fechaNacimiento, String domicilio, String telefono) {
		super(id, dni, contrasena, email, rol, activo, ValidacionForm, nombre, apellido, ocupacion, sexo, fechaNacimiento, domicilio, telefono);

	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	public String getEspecialidad() {
		return especialidad;
	}
	
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
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
