package com.proyecto_integrador_3.Estetica.Entidades;

import java.util.Date;

import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public abstract class Persona{

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
    protected String id;
    protected String dni;
    protected String email;

    @Enumerated(EnumType.STRING)
    protected Rol rol;
    protected Boolean activo;
	protected String nombre;
	protected String apellido;

	@Enumerated(EnumType.STRING)
	protected Sexo sexo;

	@Temporal(TemporalType.DATE)
	protected Date fechaNacimiento;

	private String domicilio;
	
	private Integer telefono;
	
	//Constructores
	public Persona() {}

	public Persona(String id, String dni, String email, Rol rol, Boolean activo, String nombre, String apellido,
			Sexo sexo, Date fechaNacimiento, String domicilio, Integer telefono) {
		super();
		this.id = id;
		this.dni = dni;
		this.email = email;
		this.rol = rol;
		this.activo = activo;
		this.nombre = nombre;
		this.apellido = apellido;
		this.sexo = sexo;
		this.fechaNacimiento = fechaNacimiento;
		this.domicilio = domicilio;
		this.telefono = telefono;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public Integer getTelefono() {
		return telefono;
	}

	public void setTelefono(Integer telefono) {
		this.telefono = telefono;
	}

	@Override
	public String toString() {
		return "Persona [id=" + id + ", dni=" + dni + ", email=" + email + ", rol=" + rol + ", activo=" + activo
				+ ", nombre=" + nombre + ", apellido=" + apellido + ", sexo=" + sexo + ", fechaNacimiento="
				+ fechaNacimiento + ", domicilio=" + domicilio + ", telefono=" + telefono + "]";
	}

	
}
