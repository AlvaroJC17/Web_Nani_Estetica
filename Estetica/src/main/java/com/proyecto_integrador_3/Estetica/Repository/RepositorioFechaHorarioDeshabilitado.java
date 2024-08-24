package com.proyecto_integrador_3.Estetica.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.FechaHorarioDeshabilitado;

@Repository
public interface RepositorioFechaHorarioDeshabilitado extends JpaRepository<FechaHorarioDeshabilitado, String> {

	List<FechaHorarioDeshabilitado> findByProfesionalIdAndFecha(String idProfesional, String fecha);
	
	Optional <FechaHorarioDeshabilitado> findByProfesionalId(String idProfesional);
	
	Optional <FechaHorarioDeshabilitado> findByFecha (String fecha);
	
	
}
