package com.proyecto_integrador_3.Estetica.Entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class Consulta {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	protected String id;
	
	@OneToOne
    private Profesional profesional;

    @OneToOne
    private Cliente cliente;

    @Column(name = "diagnostico", length = 6000)
    private String diagnostico;

    @Column(name = "fecha")
    private String fecha;

    @Column(name = "horario")
    private Integer horario;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "calificacion")
    private Integer calificacion;
    
    public Consulta() {
    	
    }

	public Consulta(String id, Profesional profesional, Cliente cliente, String diagnostico, String fecha,
			Integer horario, Double precio, Integer calificacion) {
		super();
		this.id = id;
		this.profesional = profesional;
		this.cliente = cliente;
		this.diagnostico = diagnostico;
		this.fecha = fecha;
		this.horario = horario;
		this.precio = precio;
		this.calificacion = calificacion;
	}

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

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public Integer getHorario() {
		return horario;
	}

	public void setHorario(Integer horario) {
		this.horario = horario;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Integer getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(Integer calificacion) {
		this.calificacion = calificacion;
	}

	@Override
	public String toString() {
		return "Consulta [id=" + id + ", profesional=" + profesional + ", cliente=" + cliente + ", diagnostico="
				+ diagnostico + ", fecha=" + fecha + ", horario=" + horario + ", precio=" + precio + ", calificacion="
				+ calificacion + "]";
	}
    
    
    
    
    
}

