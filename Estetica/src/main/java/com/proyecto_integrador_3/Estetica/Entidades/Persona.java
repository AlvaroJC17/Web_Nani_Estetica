package com.proyecto_integrador_3.Estetica.Entidades;

import java.util.Date;

import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "Persona")
public abstract class Persona{

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
    protected String id;
	@Column(name = "dni")
    protected String dni;
	@Column(name = "email")
    protected String email;
	@Column(name = "contrasena")
    protected String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    protected Rol rol;
    @Column(name = "activo")
    protected Boolean activo;
    @Column(name = "nombre")
	protected String nombre;
    @Column(name = "apellido")
	protected String apellido;

	@Enumerated(EnumType.STRING)
	@Column(name = "sexo")
	protected Sexo sexo;

	@Temporal(TemporalType.DATE)
	protected Date fechaNacimiento;
	
	@Column(name = "domicilio")
	private String domicilio;
	
	@Column(name = "telefono")
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
