package com.proyecto_integrador_3.Estetica.Servicios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Tratamiento;
import com.proyecto_integrador_3.Estetica.Enums.TratamientoEnum;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioTratamiento;

@Service
public class ServicioTratamiento {

	@Autowired
	public RepositorioTratamiento repositorioTratamiento;
	
	public List<Tratamiento> obtenerTratamientosPorIdProfesional(String idProfesional) throws MiExcepcion{
		
		try {
			List<Tratamiento> tratamientos = repositorioTratamiento.findByProfesionalId(idProfesional);
			return tratamientos;
		} catch (Exception e) {
			throw new MiExcepcion("No se encontraron tratamientos");
		}
		
	}
	
	public List<Tratamiento> tratamientosActuales (String idProfesional) throws MiExcepcion{
		try {
			List<Tratamiento> tratamientos = repositorioTratamiento.findByProfesionalIdAndActualTrue(idProfesional);
			return tratamientos;
		} catch (Exception e) {
			throw new MiExcepcion("Error al encontrar tratamientos actuales");
		}
	}
	
	
	public List<Tratamiento> obtenerUltimosTratamientos (String idProfesional) throws MiExcepcion{
		try {
			List<Tratamiento> tratamientos = repositorioTratamiento.findByProfesionalId(idProfesional);
			
			 Map<TratamientoEnum, Tratamiento> tratamientoMap = new HashMap<>();

		        for (Tratamiento tratamiento : tratamientos) {
		            TratamientoEnum nombre = tratamiento.getNombreTratamientos();
		            if (!tratamientoMap.containsKey(nombre) || tratamiento.getFechaCreacion().isAfter(tratamientoMap.get(nombre).getFechaCreacion())) {
		                tratamientoMap.put(nombre, tratamiento);
		            }
		        }

		        return new ArrayList<>(tratamientoMap.values());
		} catch (Exception e) {
			throw new MiExcepcion("Error al encontrar tratamientos profesional");
		}
	}
	
}
