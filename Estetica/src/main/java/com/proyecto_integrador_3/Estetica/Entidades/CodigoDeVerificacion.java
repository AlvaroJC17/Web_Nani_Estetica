package com.proyecto_integrador_3.Estetica.Entidades;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class CodigoDeVerificacion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name="id")
    private String id;
	
	@Column(name="codigo")
    private String codigo;
    
	@Column(name="expiracion")
    private LocalDateTime expiracion;
	
	@Column(name="usado")
    private boolean usado;

	//Esta anotacion crea un campo id de usuario en la tabla de CodigoDeValidacion y asi relacionarlos
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Usuario usuario;
    
    public CodigoDeVerificacion() {
    	
    }
    
    public CodigoDeVerificacion(String id) {
    	
    }

    public boolean esValido() {
        return !usado && LocalDateTime.now().isBefore(expiracion);
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public LocalDateTime getExpiracion() {
		return expiracion;
	}

	public void setExpiracion(LocalDateTime expiracion) {
		this.expiracion = expiracion;
	}

	public boolean isUsado() {
		return usado;
	}

	public void setUsado(boolean usado) {
		this.usado = usado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
    
    
}
