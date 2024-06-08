package com.proyecto_integrador_3.Estetica.Servicios;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.HorariosDisponibles;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioHorariosDisponibles;

@Service
public class ServicioHorario {
	
	 @Autowired
	    private RepositorioHorariosDisponibles repositorioHorariosDisponibles;
	
	 //busca la lista de horarios guardados en la base de datos pertenecientes a la fecha que le pasamos por parametro
	 //si la fecha no existe en la base de datos, entonces crea una lista de horarios para esa fecha
	 public List<String> obtenerHorariosDisponibles(String fecha) {
	        return repositorioHorariosDisponibles.findById(fecha)
	                .orElse(new HorariosDisponibles(fecha, List.of("10:00", "11:00", "12:00", "13:00", "14:00")))
	                .getHorarios();
	    }

	 //Le pasamos una fecha y una lista de horarios, las busca en la base de datos y actualiza sus valores.
	 //Si no existe la fecha, guarda esa nueva fecha y lista de horarios que le pasamos en la base de datos 
	    public void actualizarHorariosDisponibles(String fecha, List<String> horarios) {
	        HorariosDisponibles horarioDisponible = repositorioHorariosDisponibles.findById(fecha)
	                .orElse(new HorariosDisponibles(fecha, horarios));
	        horarioDisponible.setHorarios(horarios);
	        repositorioHorariosDisponibles.save(horarioDisponible);
	    }

	    //Le pasamos un fecha y un horario, busca la fecha en la base de datos y verifica si el horario
	    //esta guardado en la lista de esa fecha, sino esta guardado lo agrega nuevamnete a la lista
	    public void agregarHorarioDisponible(String fecha, String horario) {
	        HorariosDisponibles horarioDisponible = repositorioHorariosDisponibles.findById(fecha)
	                .orElse(new HorariosDisponibles(fecha, List.of()));
	        List<String> horarios = horarioDisponible.getHorarios();
	        if (!horarios.contains(horario)) {
	            horarios.add(horario);
	        }
	        horarioDisponible.setHorarios(horarios);
	        repositorioHorariosDisponibles.save(horarioDisponible);
	    }

	    //le pasamos una fecha y una lista de horarios y los guarda en la base de datos
		public void guardarHorariosDisponibles(String fecha, List<String> horarios) {
			  HorariosDisponibles horariosDisponibles = new HorariosDisponibles(fecha, horarios);
		        repositorioHorariosDisponibles.save(horariosDisponibles);
		    }
			
		
}


