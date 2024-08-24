package com.proyecto_integrador_3.Estetica.Servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.FechaHorarioDeshabilitado;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioFechaHorarioDeshabilitado;

@Service
public class ServicioFechaHorarioDeshabilitado {

	@Autowired
	private RepositorioFechaHorarioDeshabilitado repositorioFechaHorarioDeshabilitado;
	
	public List<FechaHorarioDeshabilitado> buscarHorariosDeshabilitadosPorIdProfesionalAndFecha(String idProfesional, String fecha){
		return repositorioFechaHorarioDeshabilitado.findByProfesionalIdAndFecha(idProfesional, fecha);
		
	}
	
	public Optional <FechaHorarioDeshabilitado> horariosDeshabilitadosPorIdProfesionalOptional (String idProfesional){
		return repositorioFechaHorarioDeshabilitado.findByProfesionalId(idProfesional);
	}
	
	public Optional <FechaHorarioDeshabilitado> horariosDeshabilitadosPorFecha(String fecha){
		return repositorioFechaHorarioDeshabilitado.findByFecha(fecha);
	}
	
		
		
}
