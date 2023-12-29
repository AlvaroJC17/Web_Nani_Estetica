package com.proyecto_integrador_3.Estetica.Entidades;

import java.io.Serializable;
import java.util.Date;

import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "Cliente")
public class Cliente extends Persona implements Serializable {

	@Column(name = "fomulario_datos")
	Boolean fomularioDatos;
	
	@Column(name = "fumador")
	String fuma;
	
	@Column(name = "drogas")
	String drogas;
	
	@Column(name = "alcohol")
	String alcohol;
	
	@Column(name = "deportes")
	String deportes;
	
	@Column(name = "ejercicios")
	String ejercicio;
	
	@Column(name = "medicamentos")
	String medicamentos;
	
	@Column(name = "nombre_medicamento")
	String nombreMedicamento;
	
	
	
	//Constructores
	public Cliente(){
		
	}
	
	public Cliente(String id) {
	
	}


	public Cliente(String id, String email, String contrasena, Rol rol, Boolean activo, Boolean ValidacionForm, Date fechaNacimiento) {
		super(id, email, contrasena, rol, activo, ValidacionForm, fechaNacimiento);
		
	}

	public Cliente(String id, String email, String contrasena, Rol rol, Boolean activo, Boolean ValidacionForm, String dni, String nombre, String apellido, String ocupacion, Sexo sexo, Date fechaNacimiento,
			String domicilio, Integer telefono ) {
		super(id, email, contrasena, rol, activo, ValidacionForm, fechaNacimiento);
	}
		
	
	public Cliente(String dni, String nombre, String apellido, String ocupacion, Sexo sexo,
			String domicilio, Integer telefono) {
		super(dni, nombre, apellido, ocupacion, sexo, domicilio, telefono);
		
	}


	public Cliente(Boolean fomularioDatos, String fuma, String drogas, String alcohol, String deportes,
			String ejercicio, String medicamentos, String nombreMedicamento) {
		super();
		this.fomularioDatos = fomularioDatos;
		this.fuma = fuma;
		this.drogas = drogas;
		this.alcohol = alcohol;
		this.deportes = deportes;
		this.ejercicio = ejercicio;
		this.medicamentos = medicamentos;
		this.nombreMedicamento = nombreMedicamento;
	}

	public Boolean getFomularioDatos() {
		return fomularioDatos;
	}

	public void setFomularioDatos(Boolean fomularioDatos) {
		this.fomularioDatos = fomularioDatos;
	}

	public String getFuma() {
		return fuma;
	}

	public void setFuma(String fuma) {
		this.fuma = fuma;
	}

	public String getDrogas() {
		return drogas;
	}

	public void setDrogas(String drogas) {
		this.drogas = drogas;
	}

	public String getAlcohol() {
		return alcohol;
	}

	public void setAlcohol(String alcohol) {
		this.alcohol = alcohol;
	}

	public String getDeportes() {
		return deportes;
	}

	public void setDeportes(String deportes) {
		this.deportes = deportes;
	}

	public String getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(String ejercicio) {
		this.ejercicio = ejercicio;
	}

	public String getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(String medicamentos) {
		this.medicamentos = medicamentos;
	}

	public String getNombreMedicamento() {
		return nombreMedicamento;
	}

	public void setNombreMedicamento(String nombreMedicamento) {
		this.nombreMedicamento = nombreMedicamento;
	}

	//Metodos
	@Override
	public String toString() {
		return super.toString();
	}


	
}
