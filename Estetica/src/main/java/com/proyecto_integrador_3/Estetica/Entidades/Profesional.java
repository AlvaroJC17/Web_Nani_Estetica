package com.proyecto_integrador_3.Estetica.Entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.proyecto_integrador_3.Estetica.Enums.DiasDeLaSemana;
import com.proyecto_integrador_3.Estetica.Enums.Especialidad;
import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.Enums.TipoDeEspecialidad;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Profesional")
public class Profesional extends Persona implements Serializable {
	
	@OneToMany(mappedBy = "profesional", cascade = CascadeType.ALL)
	private List<Turnos> turnos;
	
	@OneToMany(mappedBy = "profesional", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<HorariosDisponibles> horariosDisponibles;
	
	@OneToMany(mappedBy = "profesional", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<FechaHorarioDeshabilitado> fechasHorariosDeshabilitados = new ArrayList<>();
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "fechas_deshabilitadas", joinColumns = @JoinColumn(name = "profesional_id"))
	@Column(name = "fecha_inactiva")
	private List<String> fechasDeshabilitadas = new ArrayList<>();
	
	@Column(name = "matricula")
	private String matricula;
	
    @Enumerated(EnumType.STRING)
    private Provincias provincia;
    
    @Column(name = "calificacion")
    private Double calificacion;

    @Column(name = "valor_multa")
    private String valorMulta;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "especialidad")
    private Especialidad especialidad;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_especialidad")
    private TipoDeEspecialidad tipoEspecialidad;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "profesional_id")
    private List<Tratamiento> tratamientos = new ArrayList<>();
       
    @ElementCollection(targetClass = DiasDeLaSemana.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "dias_de_la_semana", joinColumns = @JoinColumn(name = "profesional_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "dias_de_la_semana")
    private List<DiasDeLaSemana> DiasDeLaSemana;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "profesional_horarios", joinColumns = @JoinColumn(name = "profesional_id"))
    @Column(name = "horarios_laborales")
    private List<String> horariosLaborales = new ArrayList<>();
    
  
    
    public Profesional() {
    	
    }
    
    public Profesional(String id) {
    	super(id);
    }

	public Profesional(String matricula, Provincias provincia, Double calificacion,
			String valorMulta, List<Turnos> turnos, List<String> horariosLaborales, List<DiasDeLaSemana> DiasDeLaSemana,
			List<Tratamiento> tratamientos, Especialidad especialidad, TipoDeEspecialidad tipoEspecialidad) {
		this.turnos = turnos;
		this.matricula = matricula;
		this.especialidad = especialidad;
		this.tipoEspecialidad = tipoEspecialidad;
		this.tratamientos = tratamientos;
		this.provincia = provincia;
		this.calificacion = calificacion;
		this.valorMulta = valorMulta;
		this.horariosLaborales = horariosLaborales;
		this.DiasDeLaSemana = DiasDeLaSemana;
	}

	public Profesional(String id, List<TokenUsuario> tokens, String dni, String contrasena, String email, Rol rol, Boolean activo, Boolean ValidacionForm,
			String nombre, String apellido, String ocupacion, Sexo sexo, LocalDate fechaNacimiento, Boolean emailValidado, LocalDateTime fechaCreacion,
			int intentosValidacion,  Boolean bloqueoValidacion, LocalDateTime horaDeBloqueoValidacion, int intentosLogin, Boolean bloqueoLogin,
			LocalDateTime horaBloqueoLogin, String domicilio, String telefono) {
		super(id, tokens, dni, contrasena, email, rol, activo, ValidacionForm, nombre, apellido, ocupacion, sexo, fechaNacimiento, emailValidado,
				fechaCreacion, intentosValidacion, bloqueoValidacion, horaDeBloqueoValidacion, intentosLogin, bloqueoLogin, horaBloqueoLogin, domicilio, telefono);

	}
	
	
	public List<FechaHorarioDeshabilitado> getFechasHorariosDeshabilitados() {
		return fechasHorariosDeshabilitados;
	}

	public void setFechasHorariosDeshabilitados(List<FechaHorarioDeshabilitado> fechasHorariosDeshabilitados) {
		this.fechasHorariosDeshabilitados = fechasHorariosDeshabilitados;
	}

	public List<String> getFechasDeshabilitadas() {
		return fechasDeshabilitadas;
	}

	public void setFechasDeshabilitadas(List<String> fechasDeshabilitadas) {
		this.fechasDeshabilitadas = fechasDeshabilitadas;
	}

	public Especialidad getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(Especialidad especialidad) {
		this.especialidad = especialidad;
	}

	public TipoDeEspecialidad getTipoEspecialidad() {
		return tipoEspecialidad;
	}

	public void setTipoEspecialidad(TipoDeEspecialidad tipoEspecialidad) {
		this.tipoEspecialidad = tipoEspecialidad;
	}

	public List<Tratamiento> getTratamientos() {
		return tratamientos;
	}

	public void setTratamientos(List<Tratamiento> tratamientos) {
		this.tratamientos = tratamientos;
	}

	public List<Turnos> getTurnos() {
		return turnos;
	}

	public void setTurnos(List<Turnos> turnos) {
		this.turnos = turnos;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public List<HorariosDisponibles> getHorariosDisponibles() {
		return horariosDisponibles;
	}

	public void setHorariosDisponibles(List<HorariosDisponibles> horariosDisponibles) {
		this.horariosDisponibles = horariosDisponibles;
	}


	public List<DiasDeLaSemana> getDiasDeLaSemana() {
		return DiasDeLaSemana;
	}

	public void setDiasDeLaSemana(List<DiasDeLaSemana> diasDeLaSemana) {
		DiasDeLaSemana = diasDeLaSemana;
	}

	public List<String> getHorariosLaborales() {
		return horariosLaborales;
	}

	public void setHorariosLaborales(List<String> horariosLaborales) {
		this.horariosLaborales = horariosLaborales;
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

	public String getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(String valorMulta) {
		this.valorMulta = valorMulta;
	}

	@Override
	public String toString() {
		return super.toString() + "Profesional [matricula=" + matricula + ", tratamientos=" + tratamientos + ", provincia=" + provincia
				+ ", calificacion=" + calificacion + ", valorMulta=" + valorMulta + "]";
	}
	
	
    
	
    
}
