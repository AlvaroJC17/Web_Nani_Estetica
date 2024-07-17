package com.proyecto_integrador_3.Estetica.Entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Horarios_disponibles")
public class HorariosDisponibles {

	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name="id")
	public String id;
	
	@Column(name="fecha")
    public String fecha;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "horarios", joinColumns = @JoinColumn(name = "horarios_disponibles_id"))
    @Column(name = "horario")
    private List<String> horarios = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "profesional_id", referencedColumnName = "id")
    private Profesional profesional;
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Profesional getProfesional() {
		return profesional;
	}

	public void setProfesional(Profesional profesional) {
		this.profesional = profesional;
	}

	public HorariosDisponibles() {
    	
    }

	public HorariosDisponibles(String fecha, List<String> horarios, Profesional profesional) {
		this.fecha = fecha;
        this.horarios = horarios;
        this.profesional = profesional;
	}
	
	public HorariosDisponibles(List<String> horarios, Profesional profesional) {
		this.horarios = horarios;
		this.profesional = profesional;
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
