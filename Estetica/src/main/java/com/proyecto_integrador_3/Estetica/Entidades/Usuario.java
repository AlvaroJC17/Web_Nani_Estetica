package com.proyecto_integrador_3.Estetica.Entidades;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.proyecto_integrador_3.Estetica.Enums.Rol;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;


@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Esta etiqueta es la que permite crear tablas separadas en la base de datos
@Table(name = "Usuario")
public class Usuario implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID) // genera valores alfanumericos como id
	@Column(name = "id") //Identifica este campo como id en la tabla
	protected String id;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	protected List<TokenUsuario> tokens = new ArrayList<>();
	
	@Column(name = "email")
	protected String email;
	
	@Column(name = "contrasena")
	protected String contrasena;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "rol")
	protected Rol rol;
	
	@Column(name = "activo")
	protected Boolean activo;
	
	@Column(name = "ValidacionForm")
	protected boolean ValidacionForm;
	
	@Temporal(TemporalType.DATE)
	protected LocalDate fechaNacimiento;
	
	@Column(name="email_validado")
	protected Boolean emailValidado = false;
	
	@Column(name="revalidar_email")
	protected Boolean revalidarEmail = false;
	
	@Column(name="fecha_creacion")
	protected LocalDateTime fechaCreacion;
	
	@Column(name ="intentos_verificacion")
	protected int intentosValidacion = 0;
	
	@Column(name = "bloqueado_verificacion")
	protected Boolean bloqueoValidacion = false;
	
	@Column(name = "hora_bloqueo_validacion")
	protected LocalDateTime horaDeBloqueoValidacion;
	
	@Column(name ="intentos_login")
	protected int intentosLogin = 0;
	
	@Column(name = "bloqueo_login")
	protected Boolean bloqueoLogin = false;
	
	@Column(name = "hora_bloqueo_login")
	protected LocalDateTime horaBloqueologin;
	
	
	@Transient
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "persona_id", referencedColumnName = "id")
	private Persona persona;
	 
	
	public Usuario() {};
	
	public Usuario(String id) {
		this.id = id;
	}
	
	
	public Usuario(String id, List<TokenUsuario> tokens, String email, String contrasena, Rol rol, Boolean activo, Boolean ValidacionForm, LocalDate fechaNacimiento,
			Boolean emailValidado, Boolean revalidarEmail, LocalDateTime fechaCreacion, int intentosValidacion, Boolean bloqueoValidacion,  LocalDateTime horaDeBloqueoValidacion,
			int intentosLogin, Boolean bloqueoLogin, LocalDateTime horaBloqueoLogin) {
		this.id = id;
		this.tokens = tokens;
		this.email = email;
		this.contrasena = contrasena;
		this.rol = rol;
		this.activo = activo;
		this.ValidacionForm = ValidacionForm;
		this.fechaNacimiento = fechaNacimiento;
		this.emailValidado = emailValidado;
		this.revalidarEmail = revalidarEmail;
		this.fechaCreacion = fechaCreacion;
		this.intentosValidacion = intentosValidacion;
		this.bloqueoValidacion = bloqueoValidacion;
		this.horaDeBloqueoValidacion = horaDeBloqueoValidacion;
		this.intentosLogin = intentosLogin;
		this.bloqueoLogin = bloqueoLogin;
		this.horaDeBloqueoValidacion = horaBloqueoLogin;
	}

	
		




	public Boolean getRevalidarEmail() {
		return revalidarEmail;
	}

	public void setRevalidarEmail(Boolean revalidarEmail) {
		this.revalidarEmail = revalidarEmail;
	}

	public Boolean getBloqueoValidacion() {
		return bloqueoValidacion;
	}

	public void setBloqueoValidacion(Boolean bloqueoValidacion) {
		this.bloqueoValidacion = bloqueoValidacion;
	}

	public LocalDateTime getHoraDeBloqueoValidacion() {
		return horaDeBloqueoValidacion;
	}

	public void setHoraDeBloqueoValidacion(LocalDateTime horaDeBloqueoValidacion) {
		this.horaDeBloqueoValidacion = horaDeBloqueoValidacion;
	}

	public Boolean getBloqueoLogin() {
		return bloqueoLogin;
	}

	public void setBloqueoLogin(Boolean bloqueoLogin) {
		this.bloqueoLogin = bloqueoLogin;
	}

	public LocalDateTime getHoraBloqueologin() {
		return horaBloqueologin;
	}

	public void setHoraBloqueologin(LocalDateTime horaBloqueologin) {
		this.horaBloqueologin = horaBloqueologin;
	}

	public int getIntentosValidacion() {
		return intentosValidacion;
	}

	public void setIntentosValidacion(int intentosValidacion) {
		this.intentosValidacion = intentosValidacion;
	}

	public int getIntentosLogin() {
		return intentosLogin;
	}

	public void setIntentosLogin(int intentosLogin) {
		this.intentosLogin = intentosLogin;
	}

	public void setValidacionForm(boolean validacionForm) {
		ValidacionForm = validacionForm;
	}

	public List<TokenUsuario> getTokens() {
		return tokens;
	}

	public void setTokens(List<TokenUsuario> tokens) {
		this.tokens = tokens;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Boolean getEmailValidado() {
		return emailValidado;
	}

	public void setEmailValidado(Boolean emailValidado) {
		this.emailValidado = emailValidado;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	
	public Boolean getValidacionForm() {
		return ValidacionForm;
	}
	
	public void setValidacionForm(Boolean ValidacionForm) {
		this.ValidacionForm = ValidacionForm;
	}
	
	

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", tokens=" + tokens + ", email=" + email + ", contrasena=" + contrasena + ", rol="
				+ rol + ", activo=" + activo + ", ValidacionForm=" + ValidacionForm + ", fechaNacimiento="
				+ fechaNacimiento + ", emailValidado=" + emailValidado + ", fechaCreacion=" + fechaCreacion
				+ ", persona=" + persona + "]";
	}


	



	
	
}
