package com.proyecto_integrador_3.Estetica.Servicios;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.HorariosDisponibles;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioHorariosDisponibles;

import jakarta.transaction.Transactional;

@Service
public class ServicioHorario {
	
	 @Autowired
	 private RepositorioHorariosDisponibles repositorioHorariosDisponibles;
	
	
	 //busca la lista de horarios guardados en la base de datos pertenecientes a la fecha y el profesional que le pasamos por parametro
	 //si la fecha no existe en la base de datos, entonces crea una lista de horarios para esa fecha y para ese profesional
	 //Si existe, entonces devuelve la lista de horarios pertenecientes a esa fecha y profesional
	 public List<String> crearyObtenerHorariosDisponibles(String fecha, String idProfesional) throws MiExcepcion {
		 
		 try {
			 List<HorariosDisponibles> horarios = repositorioHorariosDisponibles.findHorariosByProfesionalIdAndFecha(idProfesional, fecha);
			
		    if (horarios.isEmpty()) {
		        return List.of("10:00", "11:00", "12:00", "13:00", "14:00", "21:00");
		    } else {
		        return horarios.get(0).getHorarios();
		    }
		 } catch (Exception e) {
				throw new MiExcepcion("Error al conectar con el servidor " + e);
			}
	 }
	        
	        
	 //le pasamos una fecha,  una lista de horarios y un idprofesional y los guarda en la base de datos
	 //Si la lista de horarios existe, la edita con la nueva lista que le estamos pasando
	 // Sino existe crea una nueva lista de horarios para la fecha y profesional que le pasamos y los guarda en la base
	 @Transactional
		public void guardarHorariosDisponibles(String fecha, List<String> horarios, String idProfesional) throws MiExcepcion {
			
		 try {
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
		    } catch (Exception e) {
		    	throw new MiExcepcion("Error al conectar con el servidor" + e);
		    }
	 }
			
		
		
		// Obtener horarios disponibles para un profesional en una fecha específica.
		//Si encuentra una lista de horarios por idprofesional y fecha pero esta vacia, devuelve una lista vacia
		//SI la lista no esta vacia, devuelve los horarios disponibles de esa lista
		public List<String> obtenerHorariosDisponiblesPorProfesionalYFecha(String idProfesional, String fecha) throws MiExcepcion {
	        
			try {
				// Obtener los horarios disponibles para el profesional y la fecha dada
				List<HorariosDisponibles> horariosDisponibles = repositorioHorariosDisponibles.findHorariosByProfesionalIdAndFecha(idProfesional, fecha);
				
				// Si no hay horarios disponibles para ese profesional y fecha, retornar una lista vacía
				if (horariosDisponibles.isEmpty()) {
					// Manejar la ausencia de horarios disponibles
					return List.of(); // Lista vacía
				}
				// Si hay horarios disponibles, retornarlos
				return horariosDisponibles.get(0).getHorarios();
				
			} catch (Exception e) {
				throw new MiExcepcion("Error al conectar con el servidor " + e);
			}
		}
		
		

	 //Le pasamos una fecha, una lista y un idProfesional, busca una lista de horarios en la base de datos
	 //Si la lista que encuentra esta vacia, crea una nueva lista para esa fecha, horarios e idprofesional
	 // Sino esta vacia, obtiene los valores de la lista y la actualiza con los nuevos valores que le estamos pasando y luego los guarda en la base de datos
		@Transactional
		public void actualizarHorariosDisponibles(String fecha, List<String> horarios, String idProfesional) throws MiExcepcion {

			try {
				List<HorariosDisponibles> horariosDisponibles = repositorioHorariosDisponibles.findHorariosByProfesionalIdAndFecha(idProfesional, fecha);
				HorariosDisponibles horarioDisponible;
				
				if (horariosDisponibles.isEmpty()) {
					horarioDisponible = new HorariosDisponibles(fecha, horarios, new Profesional(idProfesional));
				} else {
					horarioDisponible = horariosDisponibles.get(0);
					horarioDisponible.setHorarios(horarios);
				}
				
				repositorioHorariosDisponibles.save(horarioDisponible);
			} catch (Exception e) {
				throw new MiExcepcion("Error al conectar con el servidor " + e);
			}
		}
	    
	    
	    //Metodo para eliminar los horarios que se van quedadon viejos al pasar de las horas
	    //Este metodo solo va eliminando horarios viejos si el dia para seleccionar el turno
	    //es igual al dia actual,
	    public void EliminarHorarioViejos(String idProfesional, String fecha) throws MiExcepcion {
	    	
	    	try {
				
			
	    	List<String> horariosDisponibles = obtenerHorariosDisponiblesPorProfesionalYFecha(idProfesional, fecha);
	        if (horariosDisponibles.isEmpty()) {
	            System.out.println("la lista de horarios a actualizar esta vacia");
	            return; //Si la lista esta vacia, salimos del metodo
	        }
	        
	        //Lista para ir guardando temporalmente los horarios pasados
	        List<String> horariosAEliminar = new ArrayList<>();
	        
	        //Buscamos la fecha y hora actual
	        LocalDateTime fechaHoraActual = LocalDateTime.now();
	        
	        //Designamos el formato de la fecha y hora a parsear
	        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	        for (String horario : horariosDisponibles) {
	            // Crear una variable String para la fecha y hora
	            String fechaHoraStr = fecha + " " + horario;
	            
	            //Parseamos la fecha y hora a LocalDateTime
	            LocalDateTime fechaProporcionada = LocalDateTime.parse(fechaHoraStr, dateFormatter);

	            // Comparar con la fecha y hora actual
	            if (fechaProporcionada.isBefore(fechaHoraActual)) {
	            	//si entra al if, agragamos ese horario a la lista temporal horariosAEliminar
	            	horariosAEliminar.add(horario);
	            	System.out.println("Se actualizo la lista de horarios para la fecha: " + fecha);
	            }
	        }

	        // Eliminamos los horarios guardados en la lista temporal de la lista de horarios disponibles
	        horariosDisponibles.removeAll(horariosAEliminar);
	        //Actualizamos la lista de horarios disponibles con los horarios eliminados
	        actualizarHorariosDisponibles(fecha, horariosDisponibles, idProfesional);
	    	} catch (Exception e) {
				throw new MiExcepcion("Error al conectar con el servidor " +  e);
			}
	    }
	    
	    
	    //Le pasamos un fecha y un horario, busca la fecha en la base de datos y verifica si el horario
	    //esta guardado en la lista de esa fecha, sino esta guardado lo agrega nuevamnete a la lista
	    @Transactional
	    public void agregarHorarioDisponible(String fecha, String horario, String idProfesional) throws MiExcepcion {
	    				
	    	try {
	    	 // Obtener los horarios disponibles para el profesional y la fecha dada
	        List<HorariosDisponibles> horariosDisponibles = repositorioHorariosDisponibles.findHorariosByProfesionalIdAndFecha(idProfesional, fecha);
	        
	        HorariosDisponibles horarioDisponible;
	        if (horariosDisponibles.isEmpty()) {
	            // Si no hay horarios disponibles para ese profesional y fecha, crear uno nuevo
	            horarioDisponible = new HorariosDisponibles(fecha, List.of(horario), new Profesional(idProfesional));
	        } else {
	            // Si hay horarios disponibles, obtener el primero (suponiendo que solo hay uno por fecha y profesional)
	            horarioDisponible = horariosDisponibles.get(0);
	            List<String> horarios = horarioDisponible.getHorarios();
	            
	            // Si el horario no está ya en la lista, agregarlo
	            if (!horarios.contains(horario)) {
	                horarios.add(horario);
	            }
	            
	            horarioDisponible.setHorarios(horarios);
	        }
	        
	        // Guardar el registro actualizado
	        repositorioHorariosDisponibles.save(horarioDisponible);
	    	} catch (Exception e) {
	    		throw new MiExcepcion("Error al conectar con el servidor " + e);
	    	}
	    }
	        		   
	    public boolean turnoMenorA24Horas(LocalDateTime fechaSeleecionada, LocalDateTime fechaActual) {
		   //Con el objeto duration podemos calcular la diferencia de horas entre dos fechas
		   Duration difereciaDeHorasEntreLasFechas = Duration.between(fechaActual, fechaSeleecionada);
		   //Pasamos el resultados a horas y devolvemos el booleano true si es menos a 24 horas
		   return difereciaDeHorasEntreLasFechas.toHours() < 24;
	   }
	   
			 
	    public LocalDate pasarFechaStringToLocalDate(String fecha) throws MiExcepcion {
			//Recibimos la fecha como un string y la pasamos a Date y luego a LocalDate para guardarla en la base de datos.
				LocalDate fechaUsuario = null;
				Date fechaFormateada = null;
				SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
				try {
					fechaFormateada = formato.parse(fecha);
					fechaUsuario =  fechaFormateada.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
				} catch (ParseException e) {
					throw new MiExcepcion("Error al parsear la fecha a LocalDate");
				}
				return fechaUsuario;
	    }
				
		 
		 public LocalDateTime pasarFechaStringToLocalDateTime(String fecha) throws MiExcepcion{
			 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			 LocalDateTime fechaProporcionada = LocalDateTime.parse(fecha, dateFormatter);
			 return fechaProporcionada;
		 }
				
		
		 public boolean esFinDeSemana(LocalDate fecha) {
			 DayOfWeek dayOfWeek = fecha.getDayOfWeek();
		        return dayOfWeek == DayOfWeek.SUNDAY;
		    }
		 
		 public boolean fechaYaPaso(LocalDate fecha) {
		        return fecha.isBefore(LocalDate.now());
		    }
			
			
		
}


