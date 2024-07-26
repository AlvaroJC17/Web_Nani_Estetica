package com.proyecto_integrador_3.Estetica.Entidades;

import java.util.ArrayList;
import java.util.List;

import com.proyecto_integrador_3.Estetica.Enums.TratamientoEnum;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name="tratamientos")
public class Tratamiento {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	public String id;
	
	@ManyToMany(mappedBy = "tratamientos")
	private List<Turnos> turnos = new ArrayList<>();
	
    @CollectionTable(name = "tratamiento_nombres", joinColumns = @JoinColumn(name = "tratamiento_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "nombre_tratamiento")
    private TratamientoEnum nombreTratamientos;
	
	@Column(name="costo")
	int costoTratamiento;
	
	public Tratamiento() {
		
	}
	
	public Tratamiento(TratamientoEnum nombreTratamientos, int costoTratamiento) {
		this.nombreTratamientos = nombreTratamientos;
		this.costoTratamiento = costoTratamiento;
	}

	

	public TratamientoEnum getNombreTratamientos() {
		return nombreTratamientos;
	}

	public void setNombreTratamientos(TratamientoEnum nombreTratamientos) {
		this.nombreTratamientos = nombreTratamientos;
	}

	public int getCostoTratamiento() {
		return costoTratamiento;
	}

	public void setCostoTratamiento(int costoTratamiento) {
		this.costoTratamiento = costoTratamiento;
	}
	
	
}
