package com.proyecto_integrador_3.Estetica.Entidades;
import com.proyecto_integrador_3.Estetica.Enums.EstadoDelTurno;
import com.proyecto_integrador_3.Estetica.Enums.Provincias;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

import java.time.LocalDate;

import com.proyecto_integrador_3.Estetica.Entidades.Profesional;


@Entity
//@Inheritance(strategy = InheritanceType.JOINED) // Esta etiqueta es la que permite crear tablas separadas en la base de datos
@Table(name = "Turnos")
public class Turnos {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID) 
	@Column(name = "id")
	protected String id;
	
	@Column(name = "dni")
	String dni;
	
	@Column(name = "email")
	String email;

	@Column(name = "provincia")
	String provincia;
	
	@ManyToOne
	@JoinColumn(name = "profesional_id")
	private Profesional profesional;
	
	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;
	
	@Column(name = "fecha")
	@Temporal(TemporalType.DATE)
	protected LocalDate fecha;
	
	@Column(name = "horario")
	String horario;
	
	@Column(name = "tratamiento")
	String tratamiento;
	
	@Column(name = "activo")
	Boolean activo;
	
	@Column(name = "multa")
	Boolean multa;
	
	@Column(name = "costo_multa")
	String costoMulta = "5000";
	
	@Transient
	private Provincias provincias;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "estado")
	EstadoDelTurno estado;
	
	public Turnos() {
		
	}
	
	
	public Turnos(String provincia) {
		
	}
	
	public Turnos(String dniCliente, String provincia, String profesional) {
		
	}
		
	public Turnos(String provincia, Profesional profesional, LocalDate fecha, String horario, String tratamiento, String dni,
			String email, Boolean activo, Boolean multa, String costoMulta, EstadoDelTurno estado) {
		this.dni = dni;
		this.email = email;
		this.provincia = provincia;
		this.profesional = profesional;
		this.fecha = fecha;
		this.horario = horario;
		this.tratamiento = tratamiento;
		this.activo = activo;
		this.multa = multa;
		this.costoMulta = costoMulta;
		this.estado = estado;
	}
	




	public EstadoDelTurno getEstado() {
		return estado;
	}


	public void setEstado(EstadoDelTurno estado) {
		this.estado = estado;
	}


	public String getCostoMulta() {
		return costoMulta;
	}


	public void setCostoMulta(String costoMulta) {
		this.costoMulta = costoMulta;
	}


	public Boolean getMulta() {
		return multa;
	}


	public void setMulta(Boolean multa) {
		this.multa = multa;
	}


	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public String getDniCliente() {
		return dni;
	}

	public void setDniCliente(String dni) {
		this.dni = dni;
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

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public Profesional getProfesional() {
		return profesional;
	}

	public void setProfesional(Profesional profesional) {
		this.profesional = profesional;
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
