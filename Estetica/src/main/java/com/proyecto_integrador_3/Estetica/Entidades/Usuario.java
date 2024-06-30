package com.proyecto_integrador_3.Estetica.Entidades;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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
	protected Date fechaNacimiento;
	
	@Column(name="email_validado")
	protected Boolean emailValidado = false;
	
	@Column(name="fecha_creacion")
	protected LocalDateTime fechaCreacion;
	
	@Transient
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "persona_id", referencedColumnName = "id")
	private Persona persona;
	 
	
	public Usuario() {};
	
	public Usuario(String id, String email, String contrasena, Rol rol, Boolean activo, Boolean ValidacionForm, Date fechaNacimiento) {
		this.id = id;
		this.email = email;
		this.contrasena = contrasena;
		this.rol = rol;
		this.activo = activo;
		this.ValidacionForm = ValidacionForm;
		this.fechaNacimiento = fechaNacimiento;

	
		
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
	
	

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
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
		return "Usuario [id=" + id + ", email=" + email + ", contrasena=" + contrasena + ", rol=" + rol + ", activo="
				+ activo + ", ValidacionForm=" + ValidacionForm + ", fechaNacimiento=" + fechaNacimiento + ", persona="
				+ persona + "]";
	}



	
	
}
