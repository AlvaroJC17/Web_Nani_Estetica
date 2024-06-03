package com.proyecto_integrador_3.Estetica.Entidades;
import com.proyecto_integrador_3.Estetica.Enums.Provincias;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

import java.time.LocalDate;

import com.proyecto_integrador_3.Estetica.Entidades.Profesional;


@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Esta etiqueta es la que permite crear tablas separadas en la base de datos
@Table(name = "Turnos")
public class Turnos {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID) 
	@Column(name = "id")
	protected String id;
	
	@Column(name = "dni")
	String dniCliente;

	@Column(name = "provincia")
	String provincia;
	
	@Column(name = "profesional")
	String profesional;
	
	@Column(name = "fecha")
	@Temporal(TemporalType.DATE)
	protected LocalDate fecha;
	
	@Column(name = "horario")
	String horario;
	
	@Column(name = "tratamiento")
	String tratamiento;
	
	@Column(name = "activo")
	Boolean activo;
	
	@Transient //Etiqueta para evitar que se cree una columna en la base de datos con este atributo
	private Profesional profesional2;
	
	@Transient
	private Provincias provincias;
	
	public Turnos() {
		
	}
	
	public Turnos(String provincia) {
		
	}
	
	public Turnos(String dniCliente, String provincia, String profesional) {
		
	}
		
	public Turnos(String provincia, String profesional, LocalDate fecha, String horario, String tratamiento, String dniCliente,
			Boolean activo) {
		this.dniCliente = dniCliente;
		this.provincia = provincia;
		this.profesional = profesional;
		this.fecha = fecha;
		this.horario = horario;
		this.tratamiento = tratamiento;
		this.activo = activo;
	}
	

	
	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public String getDniCliente() {
		return dniCliente;
	}

	public void setDniCliente(String dniCliente) {
		this.dniCliente = dniCliente;
	}

	// Getters y setters
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

    public Provincias getProvincias() {
        return provincias;
    }

    public void setProvincias(Provincias provincias) {
        this.provincias = provincias;
    }

	public Profesional getProfesional2() {
		return profesional2;
	}

	public void setProfesional(Profesional profesional2) {
		this.profesional2 = profesional2;
	}

	public void setProfesional(String profesional) {
		this.profesional = profesional;
	}
	
	public String getProfesional() {
		return profesional;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public String getTratamiento() {
		return tratamiento;
	}

	public void setTratamiento(String tratamiento) {
		this.tratamiento = tratamiento;
	}

	@Override
	public String toString() {
		return "Turnos [provincias=" + provincias + ", profesional=" + profesional + ", fecha=" + fecha + ", horario="
				+ horario + ", tratamiento=" + tratamiento + "]";
	}
    
    
}
