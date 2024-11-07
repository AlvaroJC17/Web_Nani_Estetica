package com.proyecto_integrador_3.Estetica.Entidades;

import java.time.LocalDateTime;

import com.proyecto_integrador_3.Estetica.Enums.TratamientoEnum;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tratamientos")
public class Tratamiento {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	public String id;
	
//	@ManyToMany(mappedBy = "tratamientos")
//	private List<Turnos> turnos = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name = "profesional_id")
	private Profesional profesional; // Relación con Profesional
	
    @CollectionTable(name = "tratamiento_nombres", joinColumns = @JoinColumn(name = "tratamiento_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "nombre_tratamiento")
    private TratamientoEnum nombreTratamientos;
	
	@Column(name="costo")
	long costoTratamiento;
	
	@Column(name="fecha_creacion")
	private LocalDateTime fechaCreacion;
	
	@Column(name="actual")
	private Boolean actual;
	
	public Tratamiento() {
		
	}
	
	public Tratamiento(TratamientoEnum nombreTratamientos, Profesional profesional, long costoTratamiento, LocalDateTime fechaCreacion, Boolean actual) {
		this.nombreTratamientos = nombreTratamientos;
		this.profesional = profesional;
		this.costoTratamiento = costoTratamiento;
		this.fechaCreacion = fechaCreacion;
		this.actual = actual;
	}

	

	public Boolean getActual() {
		return actual;
	}

	public void setActual(Boolean actual) {
		this.actual = actual;
	}

	public Profesional getProfesional() {
		return profesional;
	}

	public void setProfesional(Profesional profesional) {
		this.profesional = profesional;
	}

//	public List<Turnos> getTurnos() {
//		return turnos;
//	}
//
//	public void setTurnos(List<Turnos> turnos) {
//		this.turnos = turnos;
//	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public TratamientoEnum getNombreTratamientos() {
		return nombreTratamientos;
	}

	public void setNombreTratamientos(TratamientoEnum nombreTratamientos) {
		this.nombreTratamientos = nombreTratamientos;
	}

	public long getCostoTratamiento() {
		return costoTratamiento;
	}

	public void setCostoTratamiento(long costoTratamiento) {
		this.costoTratamiento = costoTratamiento;
	}
	
	
}
