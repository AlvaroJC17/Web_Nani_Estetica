package com.proyecto_integrador_3.Estetica.Entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "Cliente")
public class Cliente extends Persona implements Serializable {
	
	
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
	private List<Turnos> turnos;
	
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

	@Column(name = "fomulario_datos")
	Boolean fomularioDatos;
	
	@Column(name = "embarazo")
	String embarazo;
	
	@Column(name = "amamantando")
	String amamantando;
	
	@Column(name = "ciclo_menstrual")
	String ciclo_menstrual;
	
	@Column(name = "alteracion_hormonal")
	String alteracion_hormonal;
	
	@Column(name = "vitaminas")
	String vitaminas;
	
	@Column(name = "corticoides")
	String corticoides;
	
	@Column(name = "hormonas")
	String hormonas;
	
	@Column(name = "metodo_anticonceptivo")
	String metodo_anticonceptivo;
	
	@Column(name = "sufre_enfermedad")
	String sufre_enfermedad;
	
	@Column(name = "cual_enfermedad")
	String cual_enfermedad;
	
	@Column(name = "tiroides")
	String tiroides;
	
	@Column(name = "paciente_oncologica")
	String paciente_oncologica;
	
	@Column(name = "fractura_facial")
	String fractura_facial;
	
	@Column(name = "cirugia_estetica")
	String cirugia_estetica;
	
	@Column(name = "indique_cirugia_estetica")
	String indique_cirugia_estetica;
	
	@Column(name = "tiene_implantes")
	String tiene_implantes;
	
	@Column(name = "marca_pasos")
	String marca_pasos;
	
	@Column(name = "horas_sueno")
	String horas_sueno;
	
	@Column(name = "exposicion_sol")
	String exposicion_sol;
	
	@Column(name = "protector_solar")
	String protector_solar;
	
	@Column(name = "reaplica_protector")
	String reaplica_protector;
	
	@Column(name = "consumo_carbohidratos")
	String consumo_carbohidratos;
	
	@Column(name = "tratamientos_faciales_anteriores")
	String tratamientos_faciales_anteriores;
	
	@Column(name = "resultados_tratamiento_anterior")
	String resultados_tratamiento_anterior;
	
	@Column(name = "cuidado_de_piel")
	String cuidado_de_piel;
	
	@Column(name = "motivo_consulta")
	String motivo_consulta;
	
	@Column(name = "notas_profesional")
	String notas_profesional;
	
	@Column(name = "multa")
	Boolean multa;
	
	
	

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
			String domicilio, String telefono) {
		super(dni, nombre, apellido, ocupacion, sexo, domicilio, telefono);
		
	}


	public Cliente(String fuma, String drogas, String alcohol, String deportes, String ejercicios, String medicamentos, String nombreMedicamento,
			Boolean fomularioDatos, String embarazo, String amamantando, String ciclo_menstrual, String alteracion_hormonal,
			String vitaminas, String corticoides, String hormonas, String metodo_anticonceptivo, String sufre_enfermedad,
			String cual_enfermedad, String tiroides, String paciente_oncologica, String fractura_facial, String cirugia_estetica, 
			String indique_cirugia_estetica, String tiene_implantes, String marca_pasos, String horas_sueno, String exposicion_sol,
			String protector_solar, String reaplica_protector, String consumo_carbohidratos, String tratamientos_faciales_anteriores,
			String resultados_tratamiento_anterior, String cuidado_de_piel, String motivo_consulta, String notas_profesional, Boolean multa) {
		super();
		this.fuma = fuma;
		this.drogas = drogas;
		this.alcohol = alcohol;
		this.deportes = deportes;
		this.ejercicio = ejercicios;
		this.medicamentos = medicamentos;
		this.nombreMedicamento = nombreMedicamento;
		this.fomularioDatos = fomularioDatos;
		this.embarazo = embarazo;
		this.amamantando = amamantando;
		this.ciclo_menstrual = ciclo_menstrual;
		this.alteracion_hormonal = alteracion_hormonal;
		this.vitaminas = vitaminas;
		this.corticoides = corticoides;
		this.hormonas = hormonas;
		this.metodo_anticonceptivo = metodo_anticonceptivo;
		this.sufre_enfermedad = sufre_enfermedad;
		this.cual_enfermedad = cual_enfermedad;
		this.tiroides = tiroides;
		this.paciente_oncologica = paciente_oncologica;
		this.fractura_facial = fractura_facial;
		this.cirugia_estetica = cirugia_estetica;
		this.indique_cirugia_estetica = indique_cirugia_estetica;
		this.tiene_implantes = tiene_implantes;
		this.marca_pasos = marca_pasos;
		this.horas_sueno = horas_sueno;
		this.exposicion_sol = exposicion_sol;
		this.protector_solar = protector_solar;
		this.reaplica_protector = reaplica_protector;
		this.consumo_carbohidratos = consumo_carbohidratos;
		this.tratamientos_faciales_anteriores = tratamientos_faciales_anteriores;
		this.resultados_tratamiento_anterior = resultados_tratamiento_anterior;
		this.cuidado_de_piel = cuidado_de_piel;
		this.motivo_consulta = motivo_consulta;
		this.notas_profesional = notas_profesional;
		this.multa = multa;
	}


	
	
	public List<Turnos> getTurnos() {
		return turnos;
	}


	public void setTurnos(List<Turnos> turnos) {
		this.turnos = turnos;
	}


	public Boolean getMulta() {
		return multa;
	}


	public void setMulta(Boolean multa) {
		this.multa = multa;
	}


	public String getNotas_profesional() {
		return notas_profesional;
	}

	public void setNotas_profesional(String notas_profesional) {
		this.notas_profesional = notas_profesional;
	}
	

	public String getSufre_enfermedad() {
		return sufre_enfermedad;
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

	public void setSufre_enfermedad(String sufre_enfermedad) {
		this.sufre_enfermedad = sufre_enfermedad;
	}

	public String getCual_enfermedad() {
		return cual_enfermedad;
	}

	public void setCual_enfermedad(String cual_enfermedad) {
		this.cual_enfermedad = cual_enfermedad;
	}

	public String getTiroides() {
		return tiroides;
	}

	public void setTiroides(String tiroides) {
		this.tiroides = tiroides;
	}

	public String getPaciente_oncologica() {
		return paciente_oncologica;
	}

	public void setPaciente_oncologica(String paciente_oncologica) {
		this.paciente_oncologica = paciente_oncologica;
	}

	public String getFractura_facial() {
		return fractura_facial;
	}

	public void setFractura_facial(String fractura_facial) {
		this.fractura_facial = fractura_facial;
	}

	public String getCirugia_estetica() {
		return cirugia_estetica;
	}

	public void setCirugia_estetica(String cirugia_estetica) {
		this.cirugia_estetica = cirugia_estetica;
	}

	public String getIndique_cirugia_estetica() {
		return indique_cirugia_estetica;
	}

	public void setIndique_cirugia_estetica(String indique_cirugia_estetica) {
		this.indique_cirugia_estetica = indique_cirugia_estetica;
	}

	public String getTiene_implantes() {
		return tiene_implantes;
	}

	public void setTiene_implantes(String tiene_implantes) {
		this.tiene_implantes = tiene_implantes;
	}

	public String getMarca_pasos() {
		return marca_pasos;
	}

	public void setMarca_pasos(String marca_pasos) {
		this.marca_pasos = marca_pasos;
	}

	public String getHoras_sueno() {
		return horas_sueno;
	}

	public void setHoras_sueno(String horas_sueno) {
		this.horas_sueno = horas_sueno;
	}

	public String getExposicion_sol() {
		return exposicion_sol;
	}

	public void setExposicion_sol(String exposicion_sol) {
		this.exposicion_sol = exposicion_sol;
	}

	public String getProtector_solar() {
		return protector_solar;
	}

	public void setProtector_solar(String protector_solar) {
		this.protector_solar = protector_solar;
	}

	public String getReaplica_protector() {
		return reaplica_protector;
	}

	public void setReaplica_protector(String reaplica_protector) {
		this.reaplica_protector = reaplica_protector;
	}

	public String getConsumo_carbohidratos() {
		return consumo_carbohidratos;
	}

	public void setConsumo_carbohidratos(String consumo_carbohidratos) {
		this.consumo_carbohidratos = consumo_carbohidratos;
	}

	public String getTratamientos_faciales_anteriores() {
		return tratamientos_faciales_anteriores;
	}

	public void setTratamientos_faciales_anteriores(String tratamientos_faciales_anteriores) {
		this.tratamientos_faciales_anteriores = tratamientos_faciales_anteriores;
	}

	public String getResultados_tratamiento_anterior() {
		return resultados_tratamiento_anterior;
	}

	public void setResultados_tratamiento_anterior(String resultados_tratamiento_anterior) {
		this.resultados_tratamiento_anterior = resultados_tratamiento_anterior;
	}

	public String getCuidado_de_piel() {
		return cuidado_de_piel;
	}

	public void setCuidado_de_piel(String cuidado_de_piel) {
		this.cuidado_de_piel = cuidado_de_piel;
	}

	public String getMotivo_consulta() {
		return motivo_consulta;
	}

	public void setMotivo_consulta(String motivo_consulta) {
		this.motivo_consulta = motivo_consulta;
	}

	public String getMetodo_anticonceptivo() {
		return metodo_anticonceptivo;
	}

	public void setMetodo_anticonceptivo(String metodo_anticonceptivo) {
		this.metodo_anticonceptivo = metodo_anticonceptivo;
	}

	public Boolean getFomularioDatos() {
		return fomularioDatos;
	}

	public void setFomularioDatos(Boolean fomularioDatos) {
		this.fomularioDatos = fomularioDatos;
	}

	public String getEmbarazo() {
		return embarazo;
	}

	public void setEmbarazo(String embarazo) {
		this.embarazo = embarazo;
	}

	public String getAmamantando() {
		return amamantando;
	}

	public void setAmamantando(String amamantando) {
		this.amamantando = amamantando;
	}

	public String getCiclo_menstrual() {
		return ciclo_menstrual;
	}

	public void setCiclo_menstrual(String ciclo_menstrual) {
		this.ciclo_menstrual = ciclo_menstrual;
	}

	public String getAlteracion_hormonal() {
		return alteracion_hormonal;
	}

	public void setAlteracion_hormonal(String alteracion_hormonal) {
		this.alteracion_hormonal = alteracion_hormonal;
	}

	public String getVitaminas() {
		return vitaminas;
	}

	public void setVitaminas(String vitaminas) {
		this.vitaminas = vitaminas;
	}

	public String getCorticoides() {
		return corticoides;
	}

	public void setCorticoides(String corticoides) {
		this.corticoides = corticoides;
	}

	public String getHormonas() {
		return hormonas;
	}

	public void setHormonas(String hormonas) {
		this.hormonas = hormonas;
	}

	//Metodos
	@Override
	public String toString() {
		return super.toString();
	}


	
}
