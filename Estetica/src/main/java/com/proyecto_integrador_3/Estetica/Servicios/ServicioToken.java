package com.proyecto_integrador_3.Estetica.Servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.TokenUsuario;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioToken;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;

import jakarta.transaction.Transactional;

@Service
public class ServicioToken {

	@Autowired
	RepositorioToken repositorioToken;
	
	@Autowired
	RepositorioUsuario repositorioUsuario;
	
	@Autowired
	ServicioUsuario servicioUsuario;
	
	
	public String generarToken() {
        return UUID.randomUUID().toString(); // Genera un token único
    }
	
	
	@Transactional
	public void almacenarToken(String token, String emailUsuario) throws MiExcepcion {
		LocalDateTime fechaCreacion = LocalDateTime.now();
		LocalDateTime fechaExpiracion = fechaCreacion.plusMinutes(60);
		
		try {
				 Optional<Usuario> buscarUsuario = servicioUsuario.buscarPorEmailOptional(emailUsuario);
			        if (buscarUsuario.isPresent()) {
			            Usuario usuario = buscarUsuario.get();
			           
			            // Crear el token y asociarlo al usuario
			            TokenUsuario tokenInfo = new TokenUsuario(token, usuario, emailUsuario, fechaCreacion, fechaExpiracion);
			            usuario.getTokens().add(tokenInfo);
			            
			            // Guardar el usuario, lo cual persistirá el token asociado
			            repositorioUsuario.save(usuario);
			
			}else {
				System.out.println("Usuario no encontrado");
			}
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con el servidor");
		}
			
		
		
	}
	
	@Transactional
	public boolean validarToken(String token) {
		Optional<TokenUsuario> tokenInfo = repositorioToken.findByToken(token);
		
		if (tokenInfo.isPresent()) {
			LocalDateTime fechaActual = LocalDateTime.now();
			LocalDateTime fechaExpiracion = tokenInfo.get().getFechaExpiracion();
			return fechaActual.isBefore(fechaExpiracion);
		}
		
		return false; 
	}
	
}
