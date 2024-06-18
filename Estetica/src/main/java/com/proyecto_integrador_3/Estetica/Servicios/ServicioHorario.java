package com.proyecto_integrador_3.Estetica.Servicios;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.HorariosDisponibles;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioHorariosDisponibles;

@Service
public class ServicioHorario {
	
	 @Autowired
	    private RepositorioHorariosDisponibles repositorioHorariosDisponibles;
	 
	 @Autowired
	 ServicioCliente servicioCliente;
	
	 //busca la lista de horarios guardados en la base de datos pertenecientes a la fecha y el profesional que le pasamos por parametro
	 //si la fecha no existe en la base de datos, entonces crea una lista de horarios para esa fecha y para ese profesional
	 //Si existe, entonces devuelve la lista de horarios pertenecientes a esa fecha y profesional
	 public List<String> crearyObtenerHorariosDisponibles(String fecha, String idProfesional) {
		 List<HorariosDisponibles> horarios = repositorioHorariosDisponibles.findHorariosByProfesionalIdAndFecha(idProfesional, fecha);

		    if (horarios.isEmpty()) {
		        return List.of("10:00", "11:00", "12:00", "13:00", "14:00");
		    } else {
		        return horarios.get(0).getHorarios();
		    }
	 }
	        
	        
	 //le pasamos una fecha,  una lista de horarios y un idprofesional y los guarda en la base de datos
	 //Si la lista de horarios existe, la edita con la nueva lista que le estamos pasando
	 // Sino existe crea una nueva lista de horarios para la fecha y profesional que le pasamos y los guarda en la base
		public void guardarHorariosDisponibles(String fecha, List<String> horarios, String idProfesional) {
			
			 // Buscar si ya existe un registro de HorariosDisponibles para la fecha y profesional dado
		    Optional<HorariosDisponibles> optionalHorarios = repositorioHorariosDisponibles.findOptionalHorariosByProfesionalIdAndFecha(idProfesional, fecha);
		    
		    if (optionalHorarios.isPresent()) {
		        // Si ya existe, actualizar los horarios existentes con los nuevos horarios
		        HorariosDisponibles horariosDisponibles = optionalHorarios.get();
		        horariosDisponibles.setHorarios(horarios);
		        repositorioHorariosDisponibles.save(horariosDisponibles);
		    } else {
		        // Si no existe, crear un nuevo objeto HorariosDisponibles y guardarlo
		        HorariosDisponibles nuevoHorariosDisponibles = new HorariosDisponibles(fecha, horarios, new Profesional(idProfesional));
		        repositorioHorariosDisponibles.save(nuevoHorariosDisponibles);
		    }
		    }
		
		
		// Obtener horarios disponibles para un profesional en una fecha específica.
		//Si encuentra una lista de horarios por idprofesional y fecha pero esta vacia, devuelve una lista vacia
		//SI la lista no esta vacia, devuelve los horarios disponibles de esa lista
		public List<String> obtenerHorariosDisponiblesPorProfesionalYFecha(String idProfesional, String fecha) {
	        
			// Obtener los horarios disponibles para el profesional y la fecha dada
			List<HorariosDisponibles> horariosDisponibles = repositorioHorariosDisponibles.findHorariosByProfesionalIdAndFecha(idProfesional, fecha);
			
	        // Si no hay horarios disponibles para ese profesional y fecha, retornar una lista vacía
	        if (horariosDisponibles.isEmpty()) {
	            // Manejar la ausencia de horarios disponibles
	            return List.of(); // Lista vacía
	        }
	        // Si hay horarios disponibles, retornarlos
	        return horariosDisponibles.get(0).getHorarios();
	    }
		
		

	 //Le pasamos una fecha, una lista y un idProfesional, busca una lista de horarios en la base de datos
	 //Si la lista que encuentra esta vacia, crea una nueva lista para esa fecha, horarios e idprofesional
	 // Sino esta vacia, obtiene los valores de la lista y la actualiza con los nuevos valores que le estamos pasando y luego los guarda en la base de datos
	    public void actualizarHorariosDisponibles(String fecha, List<String> horarios, String idProfesional) {

	    	List<HorariosDisponibles> horariosDisponibles = repositorioHorariosDisponibles.findHorariosByProfesionalIdAndFecha(idProfesional, fecha);
	        HorariosDisponibles horarioDisponible;

	        if (horariosDisponibles.isEmpty()) {
	            horarioDisponible = new HorariosDisponibles(fecha, horarios, new Profesional(idProfesional));
	        } else {
	            horarioDisponible = horariosDisponibles.get(0);
	            horarioDisponible.setHorarios(horarios);
	        }

	        repositorioHorariosDisponibles.save(horarioDisponible);
	    }
	    
//
//	    //Le pasamos un fecha y un horario, busca la fecha en la base de datos y verifica si el horario
//	    //esta guardado en la lista de esa fecha, sino esta guardado lo agrega nuevamnete a la lista
//	    public void agregarHorarioDisponible(String fecha, String horario) {
//	    	
//	        HorariosDisponibles horarioDisponible = repositorioHorariosDisponibles.findById(fecha)
//	                .orElse(new HorariosDisponibles(fecha, List.of()));
//	        List<String> horarios = horarioDisponible.getHorarios();
//	        if (!horarios.contains(horario)) {
//	            horarios.add(horario);
//	        }
//	        horarioDisponible.setHorarios(horarios);
//	        repositorioHorariosDisponibles.save(horarioDisponible);
//	    }

	   
			
		
		
//		//Sirve para buscar una lista de horarios por idProfesional y fecha
//		public List <HorariosDisponibles> obtenerHorariosProfesional(String idProfesional, String fecha){
//			List<HorariosDisponibles> horarios = repositorioHorariosDisponibles.findHorariosByProfesionalIdAndFecha(idProfesional, fecha);
//			return horarios;
//		}
//		
//		public List<String> obtenerHorariosDisponiblesBeta(String fecha, String idProfesional) {
//	        return repositorioHorariosDisponibles.findHorariosByProfesionalIdAndFecha(idProfesional, fecha)
//	                .stream()
//	                .flatMap(horario -> horario.getHorarios().stream())
//	                .collect(Collectors.toList());
//	    }
			
			
		
}


