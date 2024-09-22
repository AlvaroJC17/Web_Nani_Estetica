package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.TRUE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.HorariosDisponibles;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Tratamiento;
import com.proyecto_integrador_3.Estetica.Entidades.Turnos;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.DiasDeLaSemana;
import com.proyecto_integrador_3.Estetica.Enums.Especialidad;
import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.Enums.TipoDeEspecialidad;
import com.proyecto_integrador_3.Estetica.Enums.TratamientoEnum;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioHorariosDisponibles;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioProfesional;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioTurnos;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;

import jakarta.transaction.Transactional;

@Service
public class ServicioProfesional {
	
	@Autowired
	public RepositorioUsuario repositorioUsuario;
	
	@Autowired
	public RepositorioProfesional repositorioProfesional;
	
	@Autowired
	public RepositorioPersona repositorioPersona;
	
	@Autowired
	public RepositorioCliente repositorioCliente;
	
	@Autowired
	public RepositorioHorariosDisponibles repositorioHorariosDisponibles;
	
	@Autowired
	public RepositorioTurnos repositorioTurnos;

	

	

	public Optional <Profesional> buscarProfesional(String idProfesional){
		return repositorioProfesional.findById(idProfesional);
	}
	
	public Optional <Persona> buscarDatosProfesionalPorId(String id){
		return repositorioPersona.buscarPersonaPorId(id);
	}
	
	
		public Boolean validarProvincia(String provincia) throws MiExcepcion {
			if (provincia == null || provincia.isEmpty() || provincia.equals("Seleccione provincia")) {
				return false;
			}else {
				return true;
			}
				
		}
		
		
   public void actualizarTratamientos(String idProfesional, String tratamientos) throws MiExcepcion {
			
			//Validamos que la variable con las horas laborales no esta vacía
			if (tratamientos.isEmpty() || tratamientos == null) {
				throw new MiExcepcion("La lista de tratamientos no puede estar vacía");
			}
			
			
			//Obtenemos el string de tratamientos con sus precios, los dividimos y los usamos para crear
			//un objeto Tratamiento y luego creamos una lista de objetos Tratamientos
			List<String> tratamientosList = Arrays.asList(tratamientos.split(",")); // acá separamos los tratamientos
			
		    List<Tratamiento> listaTratamientos = new ArrayList<>(); //Lista donde guardaremos los objetos Tratamiento
	
			// 2. Separar cada tratamiento de su costo
		        for (String tratamientoCosto : tratamientosList) {
		        	String[] partes = tratamientoCosto.split(" - \\$"); //Acá separamos el tratamiento del costo, los signos \\ sirven para escapar el simbolo $
		        	
		        	 // Validar que el tratamiento tenga el formato correcto, que el nombre o el costo no este en blanco, ademas que conste de dos partes
		        	// nombre y costo, si el string es menor a dos partes tambien tira la excepcion
		            if (partes.length < 2 || partes[0].trim().isEmpty() || partes[1].trim().isEmpty()) {
		                throw new MiExcepcion("El tratamiento \"" + tratamientoCosto + "\" no tiene un formato válido.");
		            }
		            
		            String nombreTratamiento = null; // Aqui guardaremos el nombre del tratamiento
		            long costoTratamiento = 0; // Aqui guardaremos el costo del tratamiento
		            try {
		            	nombreTratamiento = partes[0]; // Acá separamos el tratamiento que corresponde al indice [0] del array y lo guardamos en una variable
		            	costoTratamiento = Long.parseLong(partes[1]); //Acá separamos el costo que corresponde al indice [1] del array, lo parseamos a un long y lo guardamos en una variable.	
					} catch (Exception e) {
						throw new MiExcepcion("Error al parsear el costo del tratamiento.");
					}
		            
		            //Usamos la lista de todos los enum de tratamientos y mediante un condicional, comparamos si el nombre de alguno de los tratamientos que
		            //tiene el profesional coincide con con la lista de tratamientos enum, si hay coincidencia cambiamos el nombre del tratamiento por el
		            //nombre de ese tratamiento enum. En pocas palabras con este for cambiamos el formato del nombre del tratamiento al formato del nombre 
		            //del tratamiento en enum.
		           for (TratamientoEnum tratamiento : TratamientoEnum.values()) {
					if (tratamiento.getDisplayName().equals(nombreTratamiento)) {
						nombreTratamiento = tratamiento.name();
					}
				}
		            
		            // Validar que el tratamiento tenga un costo válido. Que no sea cero o numeros negativos
		            if (costoTratamiento <= 0) {
		                throw new MiExcepcion("Los tratamientos deben tener un costo mayor a 0");
		            }
		            
		            // Verifica si el número está dentro de los límites de las variables Long
		            if (costoTratamiento <= Long.MIN_VALUE && costoTratamiento >= Long.MAX_VALUE) {
		            	 throw new MiExcepcion("El costo del tratamiento excede el limite del formato permitido.");
		            }
		               
		            //Pasamos el nombre del tratamiento a un tipo TratamientoEnum
		            TratamientoEnum nuevoTratamienoEnum = null;
		            nuevoTratamienoEnum = TratamientoEnum.valueOf(nombreTratamiento.toUpperCase().trim());
		            
		            //Creamos un objeto Tratamiento con las dos variables anteriores, que son el tratamiento de tipo tratamientoEnum y el costo
		            //y lo guardamos en una lista de tipo Tratamientos
		            listaTratamientos.add(new Tratamiento(nuevoTratamienoEnum, costoTratamiento));
		        }	
		            
			//Buscamos al profesional
			Optional<Profesional> buscarProfesional = repositorioProfesional.findById(idProfesional);
			if (buscarProfesional.isPresent()) {
				Profesional actualizarProfesional = buscarProfesional.get();
				
				List<Tratamiento> tratamientosActuales = actualizarProfesional.getTratamientos(); // Obtener la lista actual
				
				for (Tratamiento nuevaListaTratamientos : listaTratamientos) {
				    boolean existe = false;
				    for (Tratamiento tratamientoExistente : tratamientosActuales) {
				        if (tratamientoExistente.getNombreTratamientos().equals(nuevaListaTratamientos.getNombreTratamientos())) {
				            tratamientoExistente.setCostoTratamiento(nuevaListaTratamientos.getCostoTratamiento()); // Actualizar el costo
				            existe = true;
				            break;
				        }
				    }
				    if (!existe) {
				        tratamientosActuales.add(nuevaListaTratamientos); // Agregar tratamiento si no existe
				    }
				}
				actualizarProfesional.setTratamientos(tratamientosActuales); // Actualizar la lista
				
				try {
				repositorioProfesional.save(actualizarProfesional); // Guardar
				} catch (Exception e) {
					throw new MiExcepcion("Error al guardar la actualizacion de tratamientos en el servidor");
				}
				
			}else {
				throw new MiExcepcion("No se encontro profesional para actualizar los tratamientos");
			}
		}

		
   public void actualizarHorasLaborales(String idProfesional, String horasLaborales) throws MiExcepcion {
	    
	    // Validar horas laborales
	    if (horasLaborales == null || horasLaborales.isEmpty()) {
	        throw new MiExcepcion("La lista de horas laborales no puede estar vacía");
	    }
	    
	    // Separar las horas laborales y eliminar espacios en blanco
	    List<String> horasLaboralesList = Arrays.stream(horasLaborales.split(","))
	                                            .map(String::trim)
	                                            .collect(Collectors.toList());
	    
	    //buscamos los horarios disponibles del profesional
	    List<HorariosDisponibles> buscarHorariosProfesional = repositorioHorariosDisponibles.findAllByProfesionalId(idProfesional);
	    
	    //Creamos la lista donde vamos a guardar la lista modificada de horarios disponibles, con esta lista vamos a actualizar la base de datos
	    List<HorariosDisponibles> listaActualizada = new ArrayList<>();
	    
	    // Buscamos al profesional
	    Optional<Profesional> buscarProfesional = repositorioProfesional.findById(idProfesional);
	    if (buscarProfesional.isPresent()) {
	        Profesional actualizarProfesional = buscarProfesional.get();
	        actualizarProfesional.setHorariosLaborales(horasLaboralesList);
	        
	        //Validamos que la lista de horarios disponibles del profesional no esta vacia
	        if (!buscarHorariosProfesional.isEmpty()) {
	            LocalDate fechaActual = LocalDate.now();
	            
	            //Recorremos la lista de horarios disponibles del profesional
	            for (HorariosDisponibles horariosDisponibles : buscarHorariosProfesional) {
	            	
	            	//Usamos el siguiente bloque de codigo para obtener la fecha correspondiente a las listas de horarios disponibles y parsearlas a LocalDate
	                LocalDate fechaHorarios = null;
	                try {
	                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	                    fechaHorarios = LocalDate.parse(horariosDisponibles.getFecha(), formatter);
	                } catch (DateTimeParseException e) {
	                    throw new MiExcepcion("Error al parsear la fecha a LocalDate: " + e.getMessage());
	                }
	                
	                //validamos que la fecha de la lista de horarios disponibles sea despues de la fecha actual
	                //no me interesa modificar las listas de horarios disponibles viejas
	                if (fechaHorarios.isAfter(fechaActual)) {
	                	
	                	//Buscamos los turnos asociados al profesional que coincida con la fecha de las listas de horarios disponibles
	                    List<Turnos> buscarHoraTurnos = repositorioTurnos.findByProfesionalIdAndFecha(idProfesional, fechaHorarios);
	                    
	                    
	                    List<String> horariosFiltrados; //Creamos una variable de tipo List<String>
	                    if (!buscarHoraTurnos.isEmpty()) { //Validamos que la lista de turnos no este vacia
	                    	
	                    	// creamos una nueva lista y le asiganamos la lista de horarios del profesional. Esto con la finalidad de no modificar la lista original
	                    	//sino una nueva
	                        horariosFiltrados = new ArrayList<>(horasLaboralesList); 
	                        for (Turnos turno : buscarHoraTurnos) { // Recorremos la lista de turnos
	                            horariosFiltrados.remove(turno.getHorario()); // Usamos la nueva lista para remover aquellas fechas que ya esten asiganadas a un turno
	                        }
	                    } else {
	                        // Si no hay turnos, usar la lista completa de horas laborales (Lista original sin modificaciones)
	                        horariosFiltrados = new ArrayList<>(horasLaboralesList);
	                    }
	                    
	                    // Actualizar la lista de horarios disponibles solo con los horarios filtrados
	                    horariosDisponibles.setHorarios(horariosFiltrados);
	                    
	                    //Agregamos la lista de horarios disponibles a lista objeto de tipo HorariosDisponibles
	                    listaActualizada.add(horariosDisponibles);
	                }
	            }
	        }
	        
	        // Actualizamos los horarios disponibles del profesional con la lista actualizada de tipo HorariosDisponibles
	        actualizarProfesional.setHorariosDisponibles(listaActualizada);
	        
	        try {
	        	//Guardamos la lista actualizada en la base de datos para sobreescribir la ya existente
	            repositorioProfesional.save(actualizarProfesional);
	        } catch (Exception e) {
	            throw new MiExcepcion("Error al guardar la actualización de horarios laborales en el servidor");
	        }
	        
	    } else {
	        throw new MiExcepcion("No se encontró profesional para actualizar las horas laborales");
	    }
	}


		//Metodo para actualizar los dias laborales del tratamiento
		public void actualizarDiasLaborales(String idProfesional, String diasLaborales) throws MiExcepcion {
			
			//Validamos que la variable con los dias laborales no esta vacía
			if (diasLaborales.isEmpty() || diasLaborales == null) {
				throw new MiExcepcion("La lista de días laborales no puede estar vacía");
			}
			
			//Obtenemos el string de diasDeLaSemana, lo dividimos y lo pasamos a una lista de enum DiasDeLaSemana
			List<DiasDeLaSemana> listaDiasDeLaSemana = new ArrayList<>();
			List<String> diasDeLaSemanaList = Arrays.asList(diasLaborales.split(","));
			for (String diasDeLaSemana : diasDeLaSemanaList) {
				DiasDeLaSemana nuevoDiasDeLaSemana = null;
				nuevoDiasDeLaSemana = DiasDeLaSemana.valueOf(diasDeLaSemana.toUpperCase());
				listaDiasDeLaSemana.add(nuevoDiasDeLaSemana);
			}
			
			//Buscamos al profesional
			Optional<Profesional> buscarProfesional = repositorioProfesional.findById(idProfesional);
			if (buscarProfesional.isPresent()) {
				Profesional actualizarProfesional = buscarProfesional.get();
				actualizarProfesional.setDiasDeLaSemana(listaDiasDeLaSemana);
				try {
				repositorioProfesional.save(actualizarProfesional);	
				} catch (Exception e) {
					throw new MiExcepcion("Error al guardar la actualizacion de días laboras en el servidor");
				}
				
			}else {
				throw new MiExcepcion("No se encontro profesional para actualizar días laborales");
			}
		}
			
			
		
		//Metodo para buscar todos los datos del profesional en la base de datos
		public Profesional obetenerDatosProfesional(String idProfesional) throws MiExcepcion {
			Optional<Profesional> buscarProfesional = repositorioProfesional.findById(idProfesional);
			if (buscarProfesional.isPresent()) {
				Profesional datosDelProfesional = buscarProfesional.get();
				return datosDelProfesional;
			}else {
				throw new MiExcepcion("No se encontro al profesional");
			}
		}
		
	    
	    //este me sirve
	    public List<Persona> buscarPacienteByRolAndDni(String dni, Rol rol){
	    	return repositorioPersona.buscarPacientePorRolYDni(rol, dni);
	    }
	    
	    public List<Persona> buscarProfesionaByRolAndProvincis(Rol rol, Provincias provincias){
	    	return repositorioPersona.buscarNombreApellidoPorRolYProvincia(rol, provincias);
	    }
	    
	    public List<Persona> buscarProfesionaByRolAndProvinciasYActivo(Rol rol, Provincias provincias, Boolean activo){
	    	return repositorioPersona.buscarNombreApellidoPorRolProvinciaYActivo(rol, provincias, activo);
	    }
	    
	
	    //Este me sirve
		public List <Persona> buscarPacienteByRolAndEmail2(String email, Rol rol){
	    	return repositorioPersona.buscarPacientePorRolYEmail(rol, email);
	    }
		
		// este me sirve
		public List <Persona> buscarPacienteByRolAndNombre(String nombre, Rol rol){
	    	return repositorioPersona.buscarPacientePorRolYNombre(rol, nombre);
	    }
		
		public List<Profesional> listarTodos() {
	        return repositorioProfesional.findAll();
	    }
		
		 public Profesional buscarProfesionalPorId(String id) {
		        Optional<Profesional> optionalProfesional = repositorioProfesional.findById(id);
		        return optionalProfesional.orElseThrow(() -> new RuntimeException("Profesional no encontrado con el ID: " + id));
		    }

	    public Profesional obtenerPorId(String id) {
	        return repositorioProfesional.findById(id).orElse(null);
	    }
		
	    public String manejoDeErroresControladorProfesional(String identificador, String error, Model model) {
	    	Boolean isEspecialidadDisabled = false;
			Boolean isProfesionalDisabled = false;
			Boolean isFechaDisabled = false;
			Boolean isHorarioDisabled = false;
			Boolean isTratamientoDisabled = false;
			
			model.addAttribute("error", error);
			model.addAttribute("isFechaDisabled", isFechaDisabled);
			model.addAttribute("isEspecialidadDisabled",isEspecialidadDisabled);
			model.addAttribute("isProfesionalDisabled",isProfesionalDisabled);
			model.addAttribute("isHorarioDisabled" ,isHorarioDisabled);
			model.addAttribute("isTratamientoDisabled" ,isTratamientoDisabled);
			model.addAttribute("showModalError", true);
			switch (identificador) {
			case "tratamientoFacial":
				return "/pagina_cliente/reservaDeTurnoClienteFacial";
			case "tratamientoCorporal":
				return "/pagina_cliente/reservaDeTurnoClienteCorporal";
			case "tratamientoEstetico":	
				return "/pagina_cliente/reservaDeTurnoClienteEstetico";
			}			
	    	return"";
	    }
	
	    public String manejoDeErroresSeleccionDeFecha(
	    		String identificador,
	    		String error,
	    		Model modelo) {
	    	
	    	Boolean isProfesionalDisabled = false;
			Boolean isEspecialidadDisabled = false;
			Boolean isFechaDisabled = false;
	    	
	    	modelo.addAttribute("error", error);
			modelo.addAttribute("showModalError", true);
			modelo.addAttribute("isProfesionalDisabled", isProfesionalDisabled);
			modelo.addAttribute("isEspecialidadDisabled", isEspecialidadDisabled);
			modelo.addAttribute("isFechaDisabled", isFechaDisabled);
			if (identificador.equals("tratamientoFacial")) {
				return "/pagina_cliente/reservaDeTurnoClienteFacial";
			}else if(identificador.equals("tratamientoCorporal")) {
				return "/pagina_cliente/reservaDeTurnoClienteCorporal";
			}else if(identificador.equals("tratamientoEstetico")) {
				return "/pagina_cliente/reservaDeTurnoClienteEstetico";
			}else {
				return "";
			}
	    	
	    }
		
		
	    public List<TipoDeEspecialidad> listarEspecialidadesDisponibles(String identificador){
		List<TipoDeEspecialidad> especialidadDisponibles = null;
		// Se filtra la lista de enum de tipo especialidades segun el identificador seleccionado y se guarda en la lista vacía
				if (identificador.equals("tratamientoFacial")) {
					 List<TipoDeEspecialidad> especialidadesConFacial = Arrays.stream(TipoDeEspecialidad.values())
					            .filter(especialidad -> especialidad.name().contains("FACIAL"))
					            .collect(Collectors.toList());
					 especialidadDisponibles = especialidadesConFacial;
					 
				}else if(identificador.equals("tratamientoCorporal")) {
					List<TipoDeEspecialidad> especialidadesCorporal = Arrays.stream(TipoDeEspecialidad.values())
				            .filter(especialidad -> especialidad.name().contains("CORPORAL"))
				            .collect(Collectors.toList());
				 especialidadDisponibles = especialidadesCorporal;
				 
				}else if(identificador.equals("tratamientoEstetico")) {
					List<TipoDeEspecialidad> especialidadesEstetico = Arrays.stream(TipoDeEspecialidad.values())
				            .filter(especialidad -> especialidad.name().contains("ESTETICO"))
				            .collect(Collectors.toList());
				 especialidadDisponibles = especialidadesEstetico;
				}else {
					//Si por alguna razon el identificador esta vacio o es diferente a las opciones indicadas, devuelve una lista vacía
					especialidadDisponibles = Collections.emptyList();
				}
				
		return especialidadDisponibles;
	}	
	    

	@Transactional
	public void registrarProfesional(String email, String matricula, String provincia,
			String direccion, String telefono, String sexo, String especialidadModoEnum, String tipoEspecialidadModoEnum,
			String DiasDeLaSemanaSeleccionados, String horariosSeleccionados, String tratamientosSeleccionados ) throws MiExcepcion {
		
		   //Validamos todos los datos ingresados por el usuario antes de crear al profesional
		validarTratamientosAndEspecialidadesProfesional(especialidadModoEnum, tipoEspecialidadModoEnum, DiasDeLaSemanaSeleccionados, horariosSeleccionados, tratamientosSeleccionados);
		
		//Obtenemos el string de especialidades y lo pasamos a enum
		Especialidad nuevoEspecialidad = null;
		nuevoEspecialidad = Especialidad.valueOf(especialidadModoEnum);
		
		//Pasamos el string tipoDeEspecialidad a enum
		TipoDeEspecialidad nuevoTipoDeEspecialidad = null;
		nuevoTipoDeEspecialidad = TipoDeEspecialidad.valueOf(tipoEspecialidadModoEnum);
		
		
		//Obtenemos el string de diasDeLaSemana, lo dividimos y lo pasamos a una lista de enum DiasDeLaSemana
		List<DiasDeLaSemana> listaDiasDeLaSemana = new ArrayList<>();
		List<String> diasDeLaSemanaList = Arrays.asList(DiasDeLaSemanaSeleccionados.split(","));
		for (String diasDeLaSemana : diasDeLaSemanaList) {
			DiasDeLaSemana nuevoDiasDeLaSemana = null;
			nuevoDiasDeLaSemana = DiasDeLaSemana.valueOf(diasDeLaSemana.toUpperCase());
			listaDiasDeLaSemana.add(nuevoDiasDeLaSemana);
		}
		
		//Obtenemos el string de horarios, lo dividimos y guardamos en una lista de String
		List<String> horariosList = Arrays.asList(horariosSeleccionados.split(","));
		
		
		//Obtenemos el string de tratamientos con sus precios, los dividimos y los usamos para crear
		//un objeto Tratamiento y luego creamos una lista de objetos Tratamientos
		List<String> tratamientosList = Arrays.asList(tratamientosSeleccionados.split(",")); // acá separamos los tratamientos
	        // 2. Separar cada tratamiento de su costo
	        List<Tratamiento> listaTratamientos = new ArrayList<>(); //Lista donde guardaremos los objetos Tratamiento
	        for (String tratamientoCosto : tratamientosList) {
	        	String[] partes = tratamientoCosto.split(" - \\$"); //Acá separamos el tratamiento del costo
	        	
	        	 // Validar que el tratamiento tenga el formato correcto
	            if (partes.length < 2 || partes[0].trim().isEmpty() || partes[1].trim().isEmpty()) {
	                throw new MiExcepcion("Error: El tratamiento \"" + tratamientoCosto + "\" no tiene un formato válido.");
	            }
	            
	            String nombreTratamiento = null;
	            long costoTratamiento = 0;
	            try {
	            	nombreTratamiento = partes[0]; // Acá separamos el tratamiento que corresponde al indice [0] del array y lo guardamos en una variable
	            	costoTratamiento = Long.parseLong(partes[1]); //Acá separamos el costo que corresponde al indice [1] del array, lo parseamos a un long y lo guardamos en una variable.	
				} catch (Exception e) {
					throw new MiExcepcion("Error al parsear el costo del tratamiento.");
				}
	            
	            // Validar que el tratamiento tenga un costo válido
	            if (costoTratamiento <= 0) {
	            	TratamientoEnum tratamiento = null;
	            	tratamiento = TratamientoEnum.valueOf(partes[0]);
	                throw new MiExcepcion("El tratamiento \"" + tratamiento.getDisplayName() + "\" tiene un costo inválido.");
	            }
	            
	            // Verifica si el número está dentro de los límites de Long
	            if (costoTratamiento <= Long.MIN_VALUE && costoTratamiento >= Long.MAX_VALUE) {
	            	 throw new MiExcepcion("El costo del tratamiento excede el limite del formato permitido.");
	            }
	            
	            //Pasamos la nombre del tratamiento a un tipo Enum
	            TratamientoEnum nuevoTratamienoEnum = null;
	            nuevoTratamienoEnum = TratamientoEnum.valueOf(nombreTratamiento.toUpperCase());
	            //Creamos un objeto Tratamiento con las dos variables anteriores y lo guardamos en una lista de tipo Tratamientos
	            listaTratamientos.add(new Tratamiento(nuevoTratamienoEnum, costoTratamiento));
	        }
	        
				
		try {
		Optional <Usuario> datosUsuario = repositorioUsuario.buscarPorEmailOptional(email);
		Optional <Persona> datosPersona = repositorioPersona.buscarPorEmailOptional(email);
		
		//Buscamos los datos de nombre, apellido, fechaNacimiento y dni de la persona y los guardamos como nuevo profesional, asi la persona no los tiene que ingresar de nuevo en el formulario
		if (datosUsuario.isPresent() && datosPersona.isPresent()) {
			Usuario datosDelUsuario = datosUsuario.get();
			Persona datosPersonalesPersona = datosPersona.get();
			
			//Pasamos el sexo de String a un objeto de tipo Date
			Sexo nuevoSexo = null;
			nuevoSexo = Sexo.valueOf(sexo.toUpperCase());
			
			Provincias Nuevaprovincia = null;
			Nuevaprovincia = Provincias.valueOf(provincia.toUpperCase());
			
			Profesional nuevo_profesional = new Profesional();
			nuevo_profesional.setEmail(email.trim());
			nuevo_profesional.setContrasena(datosDelUsuario.getContrasena());
			nuevo_profesional.setRol(datosDelUsuario.getRol());
			nuevo_profesional.setActivo(datosDelUsuario.getActivo());
			nuevo_profesional.setValidacionForm(TRUE);
			nuevo_profesional.setEmailValidado(datosDelUsuario.getEmailValidado());
			nuevo_profesional.setFechaCreacion(datosDelUsuario.getFechaCreacion());
			nuevo_profesional.setDni(datosPersonalesPersona.getDni());
			nuevo_profesional.setNombre(datosPersonalesPersona.getNombre());
			nuevo_profesional.setApellido(datosPersonalesPersona.getApellido());
			nuevo_profesional.setMatricula(matricula.trim());
			nuevo_profesional.setEspecialidad(nuevoEspecialidad);
			nuevo_profesional.setTipoEspecialidad(nuevoTipoDeEspecialidad);
			nuevo_profesional.setDiasDeLaSemana(listaDiasDeLaSemana);
			nuevo_profesional.setHorariosLaborales(horariosList);
			nuevo_profesional.setTratamientos(listaTratamientos);
			nuevo_profesional.setTelefono(telefono.trim());
			nuevo_profesional.setProvincia(Nuevaprovincia);
			nuevo_profesional.setDomicilio(direccion.trim());
			nuevo_profesional.setFechaNacimiento(datosDelUsuario.getFechaNacimiento());
			nuevo_profesional.setSexo(nuevoSexo);
			repositorioProfesional.save(nuevo_profesional);
			repositorioUsuario.delete(datosDelUsuario);
		}
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con el servidor " + e);
		}
	}
	
	
	
	@Transactional
	public void modificarProfesional(String idAdmin, String email, String emailAnterior, String domicilio, String sexo, String telefono) throws MiExcepcion {
		
		verificarEmailProfesional(email, emailAnterior);
		validarActualizacionDeDatosProfesional(domicilio, sexo, telefono);
		
		try {
			Optional<Profesional> identificarProfesional = repositorioProfesional.findById(idAdmin);
			if (identificarProfesional.isPresent()) {
				Profesional profesional_actualizado = identificarProfesional.get(); // Atribuye el objeto presente a esta nueva variable
				
				Sexo nuevoSexo = null;
				nuevoSexo = Sexo.valueOf(sexo.toUpperCase());
				
				profesional_actualizado.setEmail(email);
				profesional_actualizado.setSexo(nuevoSexo);
				profesional_actualizado.setDomicilio(domicilio);
				profesional_actualizado.setTelefono(telefono);
				repositorioProfesional.save(profesional_actualizado);
			}
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con el servidor " + e);
		}
	}
	
	public List<Tratamiento> buscarTratamitosPorProfesional(String idProfesional) throws MiExcepcion{
		
		List<Tratamiento> tratamientosProfesional = null;
		try {
			Optional<Profesional> identificarProfesional = repositorioProfesional.findById(idProfesional);
			if (identificarProfesional.isPresent()) {
				Profesional datosProfesional = identificarProfesional.get(); // Atribuye el objeto presente a esta nueva variable
				tratamientosProfesional = datosProfesional.getTratamientos();
				return tratamientosProfesional;	
			}
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con el servidor " + e);
		}
		return tratamientosProfesional;
	}
	
	//Servicio para guardar las notas que le profesional le coloca al cliente
	@Transactional
	public void guardarNotasProfesional(String idCliente, String notas_profesional) throws MiExcepcion {
		
		if (notas_profesional.length() > 500)  /*Este no lleva la validacion del Seleccione porque es un textarea*/
			 throw new MiExcepcion("Ha superado el máximo de caracteres permitidos para el campo notas del profesional");
			
		try {
			Optional<Cliente> identificarCliente = repositorioCliente.findById(idCliente);
			
			String notasDelProfesional = null;
			LocalDate fechaModificacion = null;
			LocalDate fechaActual = LocalDate.now();
			if (identificarCliente.isPresent()) {
				Cliente datosCliente = identificarCliente.get();
				notasDelProfesional = datosCliente.getNotas_profesional();
				fechaModificacion = datosCliente.getFechaModificacion();
				
				
				if (notasDelProfesional == null) {
					notasDelProfesional = "";
				}
				
				if (fechaModificacion == null) {
					fechaModificacion = LocalDate.now();
				}
				
				if (fechaModificacion != fechaActual) {
					fechaModificacion = fechaActual;
				}
				
				System.out.println("Notas guardadas::: " + notasDelProfesional + "notase enviadas::::" + notas_profesional);
				
				if (notasDelProfesional.equalsIgnoreCase(notas_profesional)) {
					return;
				}
				
				datosCliente.setNotas_profesional(notas_profesional);
				datosCliente.setFechaModificacion(fechaModificacion);
				repositorioCliente.save(datosCliente);
				
				
			}else {
				throw new MiExcepcion("No se encontro el cliente");
			}
			
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con el servidor");
		}
	}
	
	
	public void validarTratamientosAndEspecialidadesProfesional(String especialidadesSeleccionadas, String tipoEspecialidadesSeleccionadas, String DiasDeLaSemanaSeleccionados,
			 String horariosSeleccionados, String tratamientosSeleccionados) throws MiExcepcion {
		
		
		 if (tipoEspecialidadesSeleccionadas.isEmpty() || tipoEspecialidadesSeleccionadas.trim().isEmpty() || tipoEspecialidadesSeleccionadas == null) {
			 throw new MiExcepcion("Debe seleccionar por lo menos un tipo de especialidad");
		}
		 
		 if (especialidadesSeleccionadas.isEmpty() || especialidadesSeleccionadas.trim().isEmpty() || especialidadesSeleccionadas == null) {
			 throw new MiExcepcion("Debe seleccionar por lo menos una especialidad");
		}
		 
		 if (tratamientosSeleccionados.isEmpty() || tratamientosSeleccionados.trim().isEmpty() || tratamientosSeleccionados == null) {
			 throw new MiExcepcion("Debe indicar por lo menos un tratamiento");
		}
		 
		 if (DiasDeLaSemanaSeleccionados.isEmpty() || DiasDeLaSemanaSeleccionados.trim().isEmpty() || DiasDeLaSemanaSeleccionados == null) {
			 throw new MiExcepcion("Debe seleccionar por lo menos un día de la semana");
		}
		 
		 if (horariosSeleccionados.isEmpty() || horariosSeleccionados.trim().isEmpty() || horariosSeleccionados == null) {
			 throw new MiExcepcion("Debe indicar por lo menos un horario");
		}
		
	}
	
	 public void validarDatosProfesional(String matricula, String sexo, String telefono, String provincia,
			 String direccion) throws MiExcepcion {
	
		 // Expresión regular para validar un telefono
		 String regex = "\\d{7,10}";
		 
		 // Compilar la expresión regular
		 Pattern pattern = Pattern.compile(regex);
		 
		 // Crear un objeto Matcher
		 Matcher matcher = pattern.matcher(telefono);
		 
		 if (!matcher.matches()) {
			 throw new MiExcepcion("<span class='fs-6 fw-bold'>Estimado usuario,</span><br><br>"
					 + "<span class='fs-6'>El telefono no cumple con el formato solicitado, por favor verifique e intente nuevamente.</span>");
		 } 
				 
		 if (provincia == null || provincia.isEmpty() || provincia.trim().isEmpty()) {
			 throw new MiExcepcion("Debe seleccionar una provincia"); 
		 } 
		 
		 if (matricula == null || matricula.isEmpty() || matricula.trim().isEmpty()) {
			 throw new MiExcepcion("La matricula no puede estar vacia");
		 }
		 
		 if (sexo == null || sexo.isEmpty() || sexo.trim().isEmpty() || sexo.equals("Seleccione")) {
			 throw new MiExcepcion("El sexo no puede estar vacio");
		 }
		 
		 if (telefono == null || telefono.toString().isEmpty() || telefono.toString().isEmpty()) {
			 throw new MiExcepcion("EL telefono no puede estar vacio");
		 }
		 if (direccion == null || direccion.isEmpty() || direccion.trim().isEmpty()) {
			 throw new MiExcepcion("La dirección no puede estar vacia");
		 }
	 }
		 
		
	 public void validarActualizacionDeDatosProfesional(String domicilio, String sexo, String telefono) throws MiExcepcion {
		 
		 // Expresión regular para validar un telefono
		 String regex = "\\d{7,10}";
		 
		 // Compilar la expresión regular
		 Pattern pattern = Pattern.compile(regex);
		 
		 // Crear un objeto Matcher
		 Matcher matcher = pattern.matcher(telefono);
		 
		 if (!matcher.matches()) {
			 throw new MiExcepcion("<span class='fs-6 fw-bold'>Estimado usuario,</span><br><br>"
					 + "<span class='fs-6'>El telefono no cumple con el formato solicitado, por favor verifique e intente nuevamente.</span>");
		 } 
		 
		 if (domicilio == null || domicilio.isEmpty() || domicilio.trim().isEmpty()) {
			 throw new MiExcepcion("La direccion no puede estar vacia");
		 }
		 
		 if (telefono == null || telefono.toString().isEmpty() || telefono.toString().isEmpty()) {
			 throw new MiExcepcion("EL telefono no puede estar vacio");
		 }
		 
		 if (sexo == null || sexo.isEmpty() || sexo.trim().isEmpty() || sexo.equals("Seleccione")) {
			 throw new MiExcepcion("El sexo no puede estar vacio");
		 }
		 
		 if (!sexo.equalsIgnoreCase("masculino") && !sexo.equalsIgnoreCase("femenino") && !sexo.equalsIgnoreCase("otro")) {
			 throw new MiExcepcion("El sexo solo puede ser masculino, femenino u otro");
		 }
		 
	 }
	 
	 public void verificarEmailProfesional(String email, String emailAnterior) throws MiExcepcion {
		 
			// Expresión regular para validar un correo electrónico
		        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

		        // Compilar la expresión regular
		        Pattern pattern = Pattern.compile(regex);

		        // Crear un objeto Matcher
		        Matcher matcher = pattern.matcher(email);
		        
		        if (email == null || email.isEmpty() || email.trim().isEmpty()) {
		            throw new MiExcepcion("El email no puede estar vacio");
		        }

		        // Verificar si la cadena cumple con la expresión regular
		        if (!matcher.matches()) {
		            throw new MiExcepcion("El correo electronico no es valido");
		        } 
		       
		        if (!email.equalsIgnoreCase(emailAnterior)) {
		        	if (repositorioUsuario.buscarPorEmailOptional(email).isPresent()) {
		        		throw new MiExcepcion("El email ingresado ya se encuentra registrado, por favor ingrese otro");
		        	}
						
		        }
		        
	 }
	 
	
}
