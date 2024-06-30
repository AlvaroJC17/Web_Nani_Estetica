package com.proyecto_integrador_3.Estetica.Entidades;


import jakarta.persistence.Transient;

//Objeto para crear el cuerpo del email

public class EmailUsuarios {

	@Transient
	private String destinatario;
	
	@Transient
	private String asunto;
	
	@Transient
	private String mensaje;
	
	
	
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}
