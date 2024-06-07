package com.proyecto_integrador_3.Estetica.Entidades;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;

@Entity
public class HorariosDisponibles {

	
	@Id
    private String fecha;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "horario")
    private List<String> horarios;
    
    public HorariosDisponibles() {
    	
    }

	public HorariosDisponibles(String fecha, List<String> horarios) {
		this.fecha = fecha;
        this.horarios = horarios;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public List<String> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<String> horarios) {
		this.horarios = horarios;
	}
    
    
}
