package com.proyecto_integrador_3.Estetica.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.CodigoDeVerificacion;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;

@Repository
public interface RepositorioCodigoDeVerificacion extends JpaRepository<CodigoDeVerificacion, String> {

	CodigoDeVerificacion findByCodigoAndUsuario(String codigo, Usuario usuario);
	
	Optional<CodigoDeVerificacion> findByUsuarioId(String usuarioId);
	
	
}
