package com.proyecto_integrador_3.Estetica.Entidades;

import java.io.Serializable;
import java.util.Date;

import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "Persona")
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona extends Usuario implements Serializable{

	@Column(name = "dni")
    protected String dni;

    @Column(name = "nombre")
	protected String nombre;
    
    @Column(name = "apellido")
	protected String apellido;
    
    @Column(name = "ocupacion")
    protected String ocupacion;

	@Enumerated(EnumType.STRING)
	@Column(name = "sexo")
	protected Sexo sexo;

	
	@Column(name = "domicilio")
	private String domicilio;
	
	@Column(name = "telefono")
	private Integer telefono;
	
	
	
	//Constructores
	public Persona() {}

	public Persona(String id, String dni, String contrasena, String email, Rol rol, Boolean activo, Boolean ValidacionForm, String nombre, String apellido,
			String ocupacion, Sexo sexo, Date fechaNacimiento, String domicilio, Integer telefono) {
		super(id, email, contrasena, rol, activo, ValidacionForm, fechaNacimiento);
		this.dni = dni;
		this.nombre = nombre;
		this.apellido = apellido;
		this.ocupacion = ocupacion;
		this.sexo = sexo;
		this.domicilio = domicilio;
		this.telefono = telefono;
	}
	
	public Persona(String dni, String nombre, String apellido, String ocupacion, Sexo sexo, String domicilio,
			Integer telefono) {
		this.dni = dni;
		this.nombre = nombre;
		this.apellido = apellido;
		this.ocupacion = ocupacion;
		this.sexo = sexo;
		this.domicilio = domicilio;
		this.telefono = telefono;
	}

	public Persona(String id, String email, String contrasena, Rol rol, Boolean activo, Boolean ValidacionForm, Date fechaNacimiento) {
		super(id, email, contrasena, rol, activo, ValidacionForm, fechaNacimiento);
	}
		
	public Persona(String id, String email, String contrasena, Rol rol, Boolean activo, Boolean validacionForm,
			String dni, String nombre, String apellido, String ocupacion, Sexo sexo, Date fechaNacimiento,
			String domicilio, Integer telefono) {
		super(id, email, contrasena, rol, activo, validacionForm, fechaNacimiento);
		this.dni = dni;
		this.nombre = nombre;
		this.apellido = apellido;
		this.ocupacion = ocupacion;
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
	
	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}
	
	public String getOcupacion() {
		return ocupacion;
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
		return "Persona [dni=" + dni + ", nombre=" + nombre + ", apellido=" + apellido + ", ocupacion=" + ocupacion
				+ ", sexo=" + sexo + ", domicilio=" + domicilio + ", telefono=" + telefono + "]";
	}



	
}
