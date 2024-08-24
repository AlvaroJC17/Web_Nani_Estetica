package com.proyecto_integrador_3.Estetica.Entidades;

import java.io.Serializable;
import java.util.ArrayList;
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
@Table(name = "fecha_horario_deshabilitado")
public class FechaHorarioDeshabilitado implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "fecha")
    private String fecha;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "horarios_deshabilitados", joinColumns = @JoinColumn(name = "fecha_horario_deshabilitado_id"))
    @Column(name = "horario_inactivo")
    private List<String> horariosDeshabilitados = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "profesional_id")
    private Profesional profesional;
    
    public FechaHorarioDeshabilitado() {
    	
    }

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public List<String> getHorariosDeshabilitados() {
		return horariosDeshabilitados;
	}

	public void setHorariosDeshabilitados(List<String> horariosDeshabilitados) {
		this.horariosDeshabilitados = horariosDeshabilitados;
	}

	public Profesional getProfesional() {
		return profesional;
	}

	public void setProfesional(Profesional profesional) {
		this.profesional = profesional;
	}
    
    
    
}
