package com.proyecto_integrador_3.Estetica.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.CodigoDeVerificacion;

@Repository
public interface RepositorioCodigoDeVerificacion extends JpaRepository<CodigoDeVerificacion, String> {

	CodigoDeVerificacion findByCodigo(String codigo);
	
	Optional<CodigoDeVerificacion> findByUsuarioId(String usuarioId);
	
	Optional<CodigoDeVerificacion> findById(String idCodigo);
	
	List<CodigoDeVerificacion> findByUsuarioIdAndUsadoFalse(String id);
	
	
	
	
}
