package com.proyecto_integrador_3.Estetica.Entidades;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.proyecto_integrador_3.Estetica.Enums.EstadoDelTurno;
import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.Enums.Rol;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;


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
	
	@Column(name = "fecha_creacion")
	protected LocalDateTime fechaCreacion;
	
	@Column(name = "fechaModificacion")
	private LocalDate fechaModificacion;
	
	@Column(name = "fecha_cancelacion")
	private LocalDateTime fechaCancelacion;
	
	@Column(name = "horario")
	String horario;
	
//	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	@JoinTable( name = "turno_tratamiento",  joinColumns = @JoinColumn(name = "turno_id"), inverseJoinColumns = @JoinColumn(name = "tratamiento_id"))
//	private List<Tratamiento> tratamientos = new ArrayList<>();
	
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "turno_tratamientos", joinColumns = @JoinColumn(name = "turno_id"))
    @Column(name = "tratamientos")
    private List<String> tratamientos = new ArrayList<>();
	
	@Column(name = "activo")
	Boolean activo;
	
	@Column(name = "multa")
	Boolean multa;
	
	@Column(name = "consulta", length = 500) //length = 500 es para crear un campo varchar(500)
	String consulta;
	
	@Column(name = "costo_multa")
	String costoMulta;
	
	@Transient
	private Provincias provincias;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "estado")
	EstadoDelTurno estado;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "cancelado_por")
	Rol canceladoPor;
	
	@Column(name="remanente_dias")
	Boolean remanenteDias;
	
	@Column(name="remanente_horas")
	Boolean remanenteHoras;
	
	@Column(name="remanente_tratamientos")
	Boolean remanenteTratamientos;
	
	public Turnos() {
		
	}
	
	
	public Turnos(String provincia) {
		
	}
	
	public Turnos(String dniCliente, String provincia, String profesional) {
		
	}
		
	public Turnos(String provincia, Profesional profesional, LocalDate fecha, LocalDateTime fechaCracion, String horario, List<String> tratamientos, String dni,
			String email, Boolean activo, Boolean multa, String consulta, String costoMulta, EstadoDelTurno estado, Rol canceladoPor, LocalDate fechaModificacion, 
			LocalDateTime fechaCancelacion, Boolean remanenteDias, Boolean remanenteHoras, Boolean remanenteTratamientos) {
		this.dni = dni;
		this.email = email;
		this.provincia = provincia;
		this.profesional = profesional;
		this.fecha = fecha;
		this.fechaCreacion = fechaCracion;
		this.horario = horario;
		this.tratamientos = tratamientos;
		this.activo = activo;
		this.multa = multa;
		this.consulta = consulta;
		this.costoMulta = costoMulta;
		this.estado = estado;
		this.canceladoPor = canceladoPor;
		this.fechaModificacion = fechaModificacion;
		this.fechaCancelacion = fechaCancelacion;
		this.remanenteDias = remanenteDias;
		this.remanenteHoras = remanenteHoras;
		this.remanenteTratamientos = remanenteTratamientos;
	}
	

	



	


	public List<String> getTratamientos() {
		return tratamientos;
	}


	public void setTratamientos(List<String> tratamientos) {
		this.tratamientos = tratamientos;
	}


	public Boolean getRemanenteDias() {
		return remanenteDias;
	}


	public void setRemanenteDias(Boolean remanenteDias) {
		this.remanenteDias = remanenteDias;
	}


	public Boolean getRemanenteHoras() {
		return remanenteHoras;
	}


	public void setRemanenteHoras(Boolean remanenteHoras) {
		this.remanenteHoras = remanenteHoras;
	}


	public Boolean getRemanenteTratamientos() {
		return remanenteTratamientos;
	}


	public void setRemanenteTratamientos(Boolean remanenteTratamientos) {
		this.remanenteTratamientos = remanenteTratamientos;
	}


	public LocalDateTime getFechaCancelacion() {
		return fechaCancelacion;
	}


	public void setFechaCancelacion(LocalDateTime fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}

	public LocalDate getFechaModificacion() {
		return fechaModificacion;
	}


	public void setFechaModificacion(LocalDate fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}


	public String getConsulta() {
		return consulta;
	}


	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}


	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}


	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}


	public Rol getCanceladoPor() {
		return canceladoPor;
	}


	public void setCanceladoPor(Rol canceladoPor) {
		this.canceladoPor = canceladoPor;
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

//
//	public List<Tratamiento> getTratamientos() {
//		return tratamientos;
//	}
//
//
//	public void setTratamientos(List<Tratamiento> tratamientos) {
//		this.tratamientos = tratamientos;
//	}


	@Override
	public String toString() {
		return "Turnos [provincias=" + provincias + ", profesional=" + profesional + ", fecha=" + fecha + ", horario="
				+ horario + ", tratamiento=" + tratamientos + "]";
	}
    
    
}
