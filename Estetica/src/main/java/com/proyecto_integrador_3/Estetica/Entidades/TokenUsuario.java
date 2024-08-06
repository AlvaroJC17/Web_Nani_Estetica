package com.proyecto_integrador_3.Estetica.Entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TokenUsuario {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID) 
	@Column(name = "id")
	protected String id;
	
	private String token;
	
	@ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
	
	private String emailUsuario;
	
	private LocalDateTime fechaDeCreacion;
	
	private LocalDateTime fechaExpiracion;
	
	public TokenUsuario() {
		
	}
	
	public TokenUsuario(String token, Usuario usuario, String emailUsuario, LocalDateTime fechaCreacion, LocalDateTime fechaExpiracion) {
		this.token = token;
		this.usuario = usuario;
		this.emailUsuario = emailUsuario;
		this.fechaDeCreacion = fechaCreacion;
		this.fechaExpiracion = fechaExpiracion;
	}
	
	

	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public LocalDateTime getFechaDeCreacion() {
		return fechaDeCreacion;
	}

	public void setFechaDeCreacion(LocalDateTime fechaDeCreacion) {
		this.fechaDeCreacion = fechaDeCreacion;
	}

	public LocalDateTime getFechaExpiracion() {
		return fechaExpiracion;
	}

	public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}

	@Override
	public String toString() {
		return "TokenUsuario [token=" + token + ", usuario=" + usuario + ", fechaDeCreacion=" + fechaDeCreacion
				+ ", fechaExpiracion=" + fechaExpiracion + "]";
	}
	
	
}
