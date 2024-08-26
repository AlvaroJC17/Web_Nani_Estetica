package com.proyecto_integrador_3.Estetica.Servicios;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.FechaHorarioDeshabilitado;
import com.proyecto_integrador_3.Estetica.Entidades.HorariosDisponibles;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Enums.DiasDeLaSemana;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioHorariosDisponibles;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioProfesional;

import jakarta.transaction.Transactional;

@Service
public class ServicioHorario {
	
	 @Autowired
	 private RepositorioHorariosDisponibles repositorioHorariosDisponibles;
	 
	 @Autowired
	 private ServicioFechaHorarioDeshabilitado servicioFechaHorarioDeshabilitado;
	 
	 @Autowired
	 private RepositorioProfesional repositorioProfesional;
	 
	 @Autowired
	 private ServicioProfesional servicioProfesional;
	
	
	 //busca la lista de horarios guardados en la base de datos pertenecientes a la fecha y el profesional que le pasamos por parametro
	 //si la fecha no existe en la base de datos, entonces crea una lista de horarios para esa fecha y para ese profesional
	 //Si existe, entonces devuelve la lista de horarios pertenecientes a esa fecha y profesional
	@Transactional
	 public List<String> crearyObtenerHorariosDisponibles(String fecha, String idProfesional) throws MiExcepcion {
		
		 //Buscamos los horarios que le profesional registro y lo guardamos en la variable horariosDisponibles
		 Optional<Profesional> obtenerHorariosLaborales = repositorioProfesional.findById(idProfesional);
		 List<String> horariosDisponinbles = new ArrayList<>();
		 if (obtenerHorariosLaborales.isPresent()) {
			Profesional profesional = obtenerHorariosLaborales.get();
			horariosDisponinbles = profesional.getHorariosLaborales();
		}
	
		 try {
			 //Verificamos que el profesional seleccionado tenga horarios en la fecha seleccionada
			 List<HorariosDisponibles> horarios = repositorioHorariosDisponibles.findHorariosByProfesionalIdAndFecha(idProfesional, fecha);
			
			 //sino tiene horarios, creamos una lista nueva de horarios con los que selecciono el profesional
		    if (horarios.isEmpty()) {
		        return horariosDisponinbles;
		    } else {
		    	//Si tiene horarios, los obtenemos
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
				
		    //Buscamos si hay horarios para la fecha seleccionada, si exitenm los actualizamos con la lista de horarios proporcionada
		    	if (optionalHorarios.isPresent()) {
		    		// Si ya existe, actualizar los horarios existentes con los nuevos horarios
		    		HorariosDisponibles horariosDisponibles = optionalHorarios.get();
		    		horariosDisponibles.getHorarios().clear(); // Limpiar la colección antes de añadir nuevos elementos
		            horariosDisponibles.getHorarios().addAll(horarios); // Añadir los nuevos horarios
		            repositorioHorariosDisponibles.save(horariosDisponibles);
		    	} else {
		    		// Si no existe, crear un nuevo objeto HorariosDisponibles con la lista de horarios proporcionada y se guarda en la base de datos
		    		HorariosDisponibles nuevoHorariosDisponibles = new HorariosDisponibles();
		    		nuevoHorariosDisponibles.setFecha(fecha);
		    		nuevoHorariosDisponibles.setHorarios(new ArrayList<>(horarios)); //Acá es importante generar un nuevo array para guardar la lista de horarios, sino jpa me borra la lista original de la base de datos
		    		nuevoHorariosDisponibles.setProfesional(new Profesional(idProfesional));
		    		repositorioHorariosDisponibles.save(nuevoHorariosDisponibles);
		    	}
		    } catch (Exception e) {
		    	throw new MiExcepcion("Error al conectar con el servidor" + e);
		    }
	 }
			
		
		
		// Obtener horarios disponibles para un profesional en una fecha específica.
		//Si encuentra una lista de horarios por idprofesional y fecha pero esta vacia, devuelve una lista vacia
		//SI la lista no esta vacia, devuelve los horarios disponibles de esa lista
	 @Transactional
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
		@Transactional
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
		//SI el horario y fecha es pasada por un rol profesional entonces borra la el horario de esa fecha
	    @Transactional
	    public void agregarOrBorraHorarioDisponible(String fecha, String horario, String idProfesional, Rol rol) throws MiExcepcion {
	    				
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
	            if (!horarios.contains(horario) && rol == Rol.CLIENTE) {
	                horarios.add(horario);
	            }else if(rol == Rol.PROFESIONAL) {
	            	horarios.remove(horario);
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
	    
	   
		//Este metodo le da formato a una fech	 
	    public LocalDate pasarFechaStringToLocalDate(String fecha) throws MiExcepcion {
			//Recibimos la fecha como un string y la pasamos a Date y luego a LocalDate para guardarla en la base de datos.
				LocalDate fechaUsuario = null;
				Date fechaFormateada = null;
				SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
				try {
					fechaFormateada = formato.parse(fecha);
					fechaUsuario =  fechaFormateada.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
				} catch (ParseException e) {
					throw new MiExcepcion("Error al parsear la fecha a LocalDate: " + e.getMessage() + " " + "error: " + e.getCause());
				}
				return fechaUsuario;
	    }
	    
	    
	    @Transactional
	    public Boolean diasLaborales(String fecha, String idProfesional) throws MiExcepcion {
	    	try {
	    		LocalDate fechaLocalDate = pasarFechaStringToLocalDate(fecha);
	    		DayOfWeek diaDeLaSemanaSeleccionado = fechaLocalDate.getDayOfWeek();
	    			
	    		try {
	    		List<DiasDeLaSemana> diasLaborales = null;
	    		Optional<Profesional> buscarDiasDeLaSemana = repositorioProfesional.findById(idProfesional);	
	    		if (buscarDiasDeLaSemana.isPresent()) {
					Profesional diasSeleccionados = buscarDiasDeLaSemana.get();
					diasLaborales = diasSeleccionados.getDiasDeLaSemana();
				}	
	    		
	    		for (DiasDeLaSemana diasDeLaSemana : diasLaborales) {
					int numeroDelaSemana= Integer.parseInt(diasDeLaSemana.getDisplayName());
					if (diaDeLaSemanaSeleccionado == DayOfWeek.of(numeroDelaSemana)) {
						return false;
					}
					continue;
				}
	    		
	    		} catch (Exception e) {
	    			System.out.println("Error al conectar con el servidor dias de la semana " + e.getMessage());
	    		}
	    		
	    	} catch (Exception f) {
	    		System.out.println("Error al parsear la fecha");
	    		f.printStackTrace();
	    	}
	    	
	    	return true;
	    }
			
	    
		 public boolean esFinDeSemana(LocalDate fecha) {
			 DayOfWeek dayOfWeek = fecha.getDayOfWeek();
		        return dayOfWeek == DayOfWeek.SUNDAY;
		    }
		 
		 public boolean fechaYaPaso(LocalDate fecha) {
		        return fecha.isBefore(LocalDate.now());
		    }
		 
		 public Optional <HorariosDisponibles> buscarHorariosDisponiblesPorProfesionalAndFechaOptional(String idProfesional, String fecha){
			 return repositorioHorariosDisponibles.findOptionalHorariosByProfesionalIdAndFecha(idProfesional, fecha);
		 }
		 
		   //Pasamos fechas String a localDateTime con el formato yyyy-MM-dd HH:mm 
		 public LocalDateTime pasarFechaStringToLocalDateTime(String fecha) throws MiExcepcion{
			 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			 LocalDateTime fechaProporcionada = LocalDateTime.parse(fecha, dateFormatter);
			 return fechaProporcionada;
		 }
		 
		 
		 
		 //Pasamos fechas LocalDateTime a String con el formato dd/MM/yyyy
		 public String pasarFechasDeLocalDateTimeToString(LocalDateTime fechaDateTime) {
			 LocalDate fecha = fechaDateTime.toLocalDate();
			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			 String fechaFormateada = fecha.format(formatter);
			 return fechaFormateada;
		 }
		 
		 
		 
		//Pasamos las fechas LocalDate a String con el formato dd/MM/yyyy 
		 public String pasarFechasLocalDateToString(LocalDate fechaDate) {
			 // Definir un formato para la fecha
			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			 // Convertir LocalDate a String
			 String fechaString = fechaDate.format(formatter);
			 return fechaString;
		 }
		 
		 
		 
		//Pasamos las fechas LocalDate  a string con el formato yyyy-MM-dd
		 public String pasarFechasLocalDateToStringOtroFormato(LocalDate fechaDate) {
			 // Definir un formato para la fecha
			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			 // Convertir LocalDate a String
			 String fechaString = fechaDate.format(formatter);
			 return fechaString;
		 }
		 
		 //pasamos fechas String a LocalDate con el formato dd/MM/yyyy
		 public LocalDate fechaStringToLocalDate(String fecha) throws MiExcepcion {
			 
			 String fechaSinEspacios = fecha.trim();
			 
			 try {
				 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				 return LocalDate.parse(fechaSinEspacios, formatter);
			 } catch (DateTimeParseException e) {
				 throw new MiExcepcion("Error al parsear la fecha a LocalDate: " + e.getMessage());
			 }
		 }
		 
		 
		 
		 //Pasamos las fechas de string a localDate con el formato yyyy-MM-dd
		 public LocalDate pasarFechaStringToLocalDateOtroFormato(String fecha) throws MiExcepcion {
			 String fechaSinEspacios = fecha.trim();
		    	
		    	try {
		            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		            return LocalDate.parse(fechaSinEspacios, formatter);
		        } catch (DateTimeParseException e) {
		            throw new MiExcepcion("Error al parsear la fecha a LocalDate: " + e.getMessage());
		        }
		    }
		 
		 
			    public  String cambiarFormatoFechaStringToLocalDate(String fecha) {
			        // Definir el formato original de la fecha
			        DateTimeFormatter formatoOriginal = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			        // Definir el nuevo formato de la fecha
			        DateTimeFormatter nuevoFormato = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			        try {
			            // Parsear la fecha del formato original a un objeto LocalDate
			            LocalDate fechaLocalDate = LocalDate.parse(fecha, formatoOriginal);
			            // Formatear la fecha al nuevo formato
			            return fechaLocalDate.format(nuevoFormato);
			        } catch (DateTimeParseException e) {
			            // Manejar la excepción si la fecha no es válida
			            System.out.println("Fecha inválida: " + e.getMessage());
			            return null;
			        }
			    }
		 
		 
		 
		 //Listamos los horarios disponibles (que son los horarios que se generan cuando un cliente selecciona una fecha)
		 public List<HorariosDisponibles> buscarHorariosPorIdProfesionalAndFecha(String idProfesional, String fecha){
			 return repositorioHorariosDisponibles.findHorariosByProfesionalIdAndFecha(idProfesional, fecha);
		 }   
		 
		 
			 
		 //Validamos con una expresion regular que si la fecha tiene el formato dd/MM/yyy, si correcto devuelve true sino false
		 public Boolean validarFormatoDeFecha(String fecha) {
			 String regex = "^(0[1-9]|[12][0-9]|3[01])[-\\/](0[1-9]|1[0-2])[-\\/](\\d{4})$";
		        Pattern pattern = Pattern.compile(regex);
		        Matcher matcher = pattern.matcher(fecha);
		        return matcher.matches();
		 }
		 
		 
		 
		 //Metodo para validar si la fecha seleccionada se encuentra dentro de la lista de fechas deshabilitadas
		 public Boolean compararFechaConFechaDeshabilitada (LocalDate fecha, String idProfesional) {
			 
			String fechaString = pasarFechasLocalDateToStringOtroFormato(fecha);
			
			 List<String> fechasDeshabilitadas = null;
			 
				Optional <Profesional> buscarFechasDeshabilitadasProfesional = servicioProfesional.buscarProfesional(idProfesional);
				if (buscarFechasDeshabilitadasProfesional.isPresent()) {
					Profesional fechaNoHabilitadas = buscarFechasDeshabilitadasProfesional.get();
					fechasDeshabilitadas = fechaNoHabilitadas.getFechasDeshabilitadas();
					
					for (String fechasInactivas : fechasDeshabilitadas) {
						if (fechaString.equals(fechasInactivas)) {
							return true;
						}
					}
				}
				
				return false;
		 }
		 
		 
		 
		 //Metodo para comparar las horas de la fecha seleccionada con la lista de horas deshabilitadas
		 public List<String> eliminarHorasDisponiblesConHorasDeshabilitadas(List<String> horariosDisponibles, String fecha, String idProfesional) throws MiExcepcion {
			  
			 // Si los horarios disponibles vienen vacios, devolvemos la misma lista vacia
			    if (horariosDisponibles.isEmpty()) {
			        return horariosDisponibles;
			    }
			 
			 //Buscamos los horarios deshabilitados por fecha y profesional
			 List<FechaHorarioDeshabilitado> obtenerFechaHorarioDeshabilitadosPorProfesionalYFecha = servicioFechaHorarioDeshabilitado.buscarHorariosDeshabilitadosPorIdProfesionalAndFecha(idProfesional, fecha);

			 //Si no se encuentran horarios deshabilitados para esa fecha y profesional, devolvemos la misma lista de horarios disponibles
			 if (obtenerFechaHorarioDeshabilitadosPorProfesionalYFecha.isEmpty()) {
				return horariosDisponibles;
			}
			 
			// Extraer los horarios deshabilitados de cada objeto FechaHorarioDeshabilitado
			List<String> horariosDeshabilitados = new ArrayList<>();
			for (FechaHorarioDeshabilitado fechaHorarioDeshabilitado : obtenerFechaHorarioDeshabilitadosPorProfesionalYFecha) {
			    horariosDeshabilitados.addAll(fechaHorarioDeshabilitado.getHorariosDeshabilitados());
			}

			// Remover de la lista de horarios disponibles aquellos que estén en la lista de horarios deshabilitados
			horariosDisponibles.removeAll(horariosDeshabilitados);
				
			//Buscamos la lista de horarios para esa fecha y profesional, la editamos y la guardamos
			Optional<HorariosDisponibles> listaHorariosActualizado = buscarHorariosDisponiblesPorProfesionalAndFechaOptional(idProfesional, fecha);
				if (listaHorariosActualizado.isPresent()) {
					HorariosDisponibles horarios = listaHorariosActualizado.get();
					horarios.setHorarios(horariosDisponibles);
					repositorioHorariosDisponibles.save(horarios);
					
					//Buscamos la lista nuevamente despues de ser guardada en la base de datos y la retornamos
					List<String> horariosDisponiblesActualizados = obtenerHorariosDisponiblesPorProfesionalYFecha(idProfesional, fecha);
					return horariosDisponiblesActualizados;
					
				}else {
					// Si hay algun error devolvemos un mensaje de error y una lista vacia
					System.out.println("Error al encontrar la lista de horarios");
					return new ArrayList<>();
				}
			 
		 }
				
				
}


