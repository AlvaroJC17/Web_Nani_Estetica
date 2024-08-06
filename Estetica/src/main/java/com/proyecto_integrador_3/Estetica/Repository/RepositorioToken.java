package com.proyecto_integrador_3.Estetica.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.TokenUsuario;

@Repository
public interface RepositorioToken extends JpaRepository<TokenUsuario, String> {

	Optional<TokenUsuario> findByToken(String token);
	
	
	
	
}
