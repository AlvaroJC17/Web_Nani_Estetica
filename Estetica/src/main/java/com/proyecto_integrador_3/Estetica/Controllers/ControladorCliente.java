package com.proyecto_integrador_3.Estetica.Controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.EmailUsuarios;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Tratamiento;
import com.proyecto_integrador_3.Estetica.Entidades.Turnos;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.DiasDeLaSemana;
import com.proyecto_integrador_3.Estetica.Enums.Especialidad;
import com.proyecto_integrador_3.Estetica.Enums.EstadoDelTurno;
import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.Enums.TipoDeEspecialidad;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioProfesional;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioTurnos;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCliente;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioEmail;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioHorario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioProfesional;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioTurnos;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

@Controller
//@RequestMapping(")
public class ControladorCliente {

	@Autowired
	public RepositorioCliente repositorioCliente;
	
	@Autowired
	public RepositorioTurnos repositorioTurnos;
	
	@Autowired
	public RepositorioPersona repositorioPersona;
	
	@Autowired
	public RepositorioProfesional repositorioProfesional;
	
	@Autowired
	public ServicioCliente servicioCliente;
	
	@Autowired
	public ServicioProfesional servicioProfesional;
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	@Autowired
	public ServicioHorario servicioHorario;
	
	@Autowired
	public ServicioTurnos servicioTurnos;
	
	@Autowired
	public ServicioEmail servicioEmail;
	
	
	//Muestra las multas del cliente con todo sus datos
	@GetMapping("/multas")
	public String multas(
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String idCliente,
			ModelMap model) throws MiExcepcion {
		    
		
		List<Turnos> turnosConMulta = repositorioTurnos.findByClienteIdAndActivoFalseAndMultaTrue(idCliente);
		int totalCostoMultas = 0;
		 Map<String, String> turnosConNombreProfesional = new HashMap<>(); //Creamos una lista donde se van a asociar el id del turno con el nombre del profesional
		for (Turnos turnos : turnosConMulta) {
			try {
				int costoDeMultas = Integer.parseInt(turnos.getCostoMulta());
				totalCostoMultas += costoDeMultas;
			} catch (Exception e) {
				System.err.println("No se puede convertir int, el string tiene un formato no valido: " + turnos.getCostoMulta());
			}
				
			String idProfesional = turnos.getProfesional().getId();
			//Buscamos el nombre del profesional asociado a ese turno para pasarlo al email de confirmacion
			Optional <Persona> datosProfesional = repositorioPersona.findById(idProfesional);
	    	String nombreDelProfesional = null;
	    	String apellidoDelProfesional = null;
	    	String nombreCompletoProfesional = null;
			if (datosProfesional.isPresent()) {
				Persona nombreProfesional = datosProfesional.get();
				nombreDelProfesional = nombreProfesional.getNombre();
				apellidoDelProfesional = nombreProfesional.getApellido();
				nombreCompletoProfesional = apellidoDelProfesional + " " + nombreDelProfesional;
			}
			
			 if (nombreCompletoProfesional != null) {
		            // Agregamos el nombre del profesional al mapa junto al id del turno iterado
		            turnosConNombreProfesional.put(turnos.getId(), nombreCompletoProfesional);
			 }
		}
		//Buscamos los datos del usuario
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("turnosConMulta", turnosConMulta);
		model.addAttribute("totalCostoMultas", totalCostoMultas);
		model.addAttribute("turnosConNombreProfesional", turnosConNombreProfesional);
		model.addAttribute("datosCliente", datosCliente);
		return "/pagina_cliente/multas";
	}
	
	
	/*Este metodo deriva a la pagina de tratamientos con los valores de mail y id del cliente*/
	@GetMapping("/tratamientos")
	public String tratamientos(
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String idCliente,
			ModelMap model) {
		
		//Antes de mostrar los tratamientos disponibles, verificamos que no hay turno antiguos
		//no asistidos, de ser afirmativo, le colocamos una multa al turno
		try {
			servicioTurnos.actualizarTurnosAntiguos(email);
		} catch (MiExcepcion e) {
			System.out.println("Error en el portal de tratamientos");
			e.printStackTrace();
		}
		
		Boolean tieneMultas = false;
		Optional<Cliente> obtenerDatosDeMultas = repositorioCliente.findClienteById(idCliente);
		if (obtenerDatosDeMultas.isPresent()) {
			Cliente multasDelCliente = obtenerDatosDeMultas.get();
			 tieneMultas = multasDelCliente.getMulta();
		}
		
		//si el resultado de multa es true, entonces redirigimos a la pagina del mensaje de pago
		if (tieneMultas) {
			return "redirect:/multas?email=" + email + "&idCliente=" + idCliente; 
		}else {
			List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
			model.addAttribute("datosCliente", datosCliente);
			return "/pagina_cliente/tratamientos";
		}
	}
			
	@GetMapping("/buscarPorEspecialidadAndProvincia")
	public String buscarPorEspecialidadAndProvincia(
			@RequestParam(required = false) String emailCliente,
			@RequestParam(required = false) String idCliente,
			@RequestParam(required = false) String identificador,
			@RequestParam String provincia,
			Model model) throws MiExcepcion {
		
		Boolean isEspecialidadDisabled = false; // Booleano para habilitar o deshabilitar el input de especialidades
		Boolean isProfesionalDisabled = false; //Booleano para habilitar o deshabilitar el input de profesional
		Boolean isFechaDisabled = false; //Booleano para habilitar o deshabilitar el input de fecha
		Boolean isHorarioDisabled = false; //Booleano para habilitar o deshabilitar el input de horario
		Boolean isTratamientoDisabled = false; //Booleano para habilitar o deshabilitar el input y los botones de tratamientos, asi como los botones de enviar y cancelar
		
		//validamos que se seleccione una provincia
		if (!servicioProfesional.validarProvincia(provincia)) {
			String error ="<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"+
					"<span class='fs-6'>Es necesario que seleccione la provincia donde vive para poder ver que"
					+ " profesionales estan disponibles.</span>";
			return servicioProfesional.manejoDeErroresControladorProfesional(identificador, error, model);
		}
				
		
		//pasamos la provincia a enum provincia
		Provincias nuevoRolProvincia = null;
		nuevoRolProvincia = Provincias.valueOf(provincia);
	
		//Buscamos todos los datos del cliente y lo pasamos al html, sirve para visualizar la pagina y pasar los datos del cliente entre controladores
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailCliente);

		//Pasamos los datos a la vista
		model.addAttribute("datosCliente", datosCliente);
		model.addAttribute("identificador", identificador);
		model.addAttribute("provinciaString", provincia);
		model.addAttribute("isEspecialidadDisabled",isEspecialidadDisabled);
		model.addAttribute("isProfesionalDisabled",isProfesionalDisabled);
		model.addAttribute("isFechaDisabled ", isFechaDisabled);
		model.addAttribute("isHorarioDisabled", isHorarioDisabled);
		model.addAttribute("isTratamientoDisabled", isTratamientoDisabled);
		model.addAttribute("provinciaSeleccionada", nuevoRolProvincia.getDisplayName()); //getDisplayName Muestra los nombre personalizados de los enum y no los nombres en Mayusculas
		model.addAttribute("provincias", Provincias.values()); // pasamos el array de enums al formulario para desplegar la lista de select
		
		//Buscamos los profesionales disponibles en esa provincia
		List<Profesional> profesionalesDisponiblesEnLaProvincia = repositorioProfesional.findByProvincia(nuevoRolProvincia);
		
		//Validamos que existan profesionales disponibles en la provincia seleccionada, sino tiramos un error
		if (profesionalesDisponiblesEnLaProvincia.isEmpty()) {
			String error ="<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"+
			"<span class='fs-6'>No hay profesionales disponibles para la provincia seleccionada.</span>";
			return servicioProfesional.manejoDeErroresControladorProfesional(identificador, error, model);
		}

		//Usamos el servicio de listarEspecialidadesDisponibles para crear una lista de especialidades segun el identificador seleccionado
		List<TipoDeEspecialidad> especialidadDisponibles = servicioProfesional.listarEspecialidadesDisponibles(identificador);
		
			
		//Validamos que si la lista de profesionales esta vacia para esa provincia, especialidad y rol
		//error con un mensaje
		if (especialidadDisponibles.isEmpty()) {
			String error = "<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"
					+"<span class='fs-6'>Lamentamos informarle que no hay profesionales disponibles para la provincia seleccionada.</span>";
			return servicioProfesional.manejoDeErroresControladorProfesional(identificador, error, model);
		}
		
		//Si todo lo anterior sale bien pasamos la lista de profesionales encontrados a la vista
		isEspecialidadDisabled = true;
		model.addAttribute("isEspecialidadDisabled", isEspecialidadDisabled); //habilitamos el select de especialidades
		model.addAttribute("tipoEspecialidad", especialidadDisponibles); // pasamos la lista de enum tipoDeEspecialidades a la vista
		
		//Si todo va bien entonces mandamos todas la info anterior a estas vistas
		if (identificador.equals("tratamientoFacial")) {
			return "/pagina_cliente/reservaDeTurnoClienteFacial";
		}else if(identificador.equals("tratamientoCorporal")) {
			return "/pagina_cliente/reservaDeTurnoClienteCorporal";
		}else if(identificador.equals("tratamientoEstetico")) {
			return "/pagina_cliente/reservaDeTurnoClienteEstetico";
		}else {
			return "tratamientos";
		}
	}
	
	@PostMapping("/buscarProfesionalesPorEspecialidadAndProvincia")
	public String buscarProfesionalesPorEspecialidadAndProvincia(
			@RequestParam String emailCliente,
			@RequestParam String idCliente,
			@RequestParam String identificador,
			@RequestParam String provinciaString,
			@RequestParam String tipoEspecialidad,
			Model model) throws MiExcepcion {
		
			    //pasamos la provincia a enum provincia
				Provincias nuevoRolProvincia = null;
				nuevoRolProvincia = Provincias.valueOf(provinciaString);
				
				//pasamos la tipoEspecialidad a enum TipoDeEspecialidad
		        TipoDeEspecialidad nuevoRolTipoDeEspecialidad = null;
		        nuevoRolTipoDeEspecialidad = TipoDeEspecialidad.valueOf(tipoEspecialidad);
		        
		      //Buscamos todos los datos del cliente y lo pasamos al html, sirve para visualizar la pagina y pasar los datos del cliente entre controladores
				List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailCliente);
				Boolean isEspecialidadDisabled = false; // Booleano para habilitar o deshabilitar el input de especialidades
				Boolean isProfesionalDisabled = false; //Booleano para habilitar o deshabilitar el input de profesional
				Boolean isFechaDisabled = false; //Booleano para habilitar o deshabilitar el input de fecha
				Boolean isHorarioDisabled = false; //Booleano para habilitar o deshabilitar el input de horario
				Boolean isTratamientoDisabled = false; //Booleano para habilitar o deshabilitar el input y los botones de tratamientos, asi como los botones de enviar y cancelar
				model.addAttribute("datosCliente", datosCliente);
				model.addAttribute("identificador", identificador);
				model.addAttribute("provinciaString", provinciaString);
				model.addAttribute("especialidad", tipoEspecialidad);
				model.addAttribute("isEspecialidadDisabled",isEspecialidadDisabled);
				model.addAttribute("isProfesionalDisabled",isProfesionalDisabled);
				model.addAttribute("isFechaDisabled ", isFechaDisabled);
				model.addAttribute("isHorarioDisabled", isHorarioDisabled);
				model.addAttribute("isTratamientoDisabled", isTratamientoDisabled);
				model.addAttribute("provincias", Provincias.values()); // pasamos el array de enums al formulario para desplegar la lista de select
		        
				
				//Creamos una lista de TipoDeEspecialidades vacia para guardar el resultado del filtrado
				List<TipoDeEspecialidad> especialidadDisponibles = servicioProfesional.listarEspecialidadesDisponibles(identificador);
		        
		     
				//Buscamos los profesionales según el tipo de especialidad que se selecciono, la provincia, el rol y que este activo
		        List<Profesional> profesionalesPorTipoDeEspecialidad = repositorioProfesional.findByRolAndProvinciaAndTipoEspecialidadAndActivo(Rol.PROFESIONAL, nuevoRolProvincia, nuevoRolTipoDeEspecialidad, true);
		        
		        if (profesionalesPorTipoDeEspecialidad.isEmpty()) {
		        	String error = "<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"
							+"<span class='fs-6'>Lamentamos informarle que no hay profesionales disponibles con la especialidad seleccionada.</span>";
					return servicioProfesional.manejoDeErroresControladorProfesional(identificador, error, model);
				}
					
				//Si todo lo anterior sale bien pasamos la lista de profesionales encontrados a la vista
				isEspecialidadDisabled = true;
				isProfesionalDisabled = true;
				model.addAttribute("isEspecialidadDisabled", isEspecialidadDisabled); //habilitamos el select de especialidades
				model.addAttribute("isProfesionalDisabled", isProfesionalDisabled);
				model.addAttribute("tipoEspecialidad", especialidadDisponibles); // pasamos la lista de enum tipoDeEspecialidades a la vista
				model.addAttribute("Profesionales", profesionalesPorTipoDeEspecialidad); // pasamos la lista de enum tipoDeEspecialidades a la vista
				model.addAttribute("provinciaSeleccionada", nuevoRolProvincia.getDisplayName()); //getDisplayName Muestra los nombre personalizados de los enum y no los nombres en Mayusculas
				model.addAttribute("tipoDeEspecialidadSeleccionada", nuevoRolTipoDeEspecialidad.getDisplayName());
		   
		        if (identificador.equals("tratamientoFacial")) {
					return "/pagina_cliente/reservaDeTurnoClienteFacial";
				}else if(identificador.equals("tratamientoCorporal")) {
					return "/pagina_cliente/reservaDeTurnoClienteCorporal";
				}else if(identificador.equals("tratamientoEstetico")) {
					return "/pagina_cliente/reservaDeTurnoClienteEstetico";
				}
				return"";
	}
				
	@PostMapping("/buscarProfesional")
	public String buscarProfesional(
			@RequestParam String idProfesional,
			@RequestParam String idCliente,
			@RequestParam String emailCliente,
			@RequestParam String identificador,
			@RequestParam String provinciaString,
			@RequestParam (name ="especialidad") String tipoEspecialidad,
			Model modelo) throws MiExcepcion {	
		
		
		//pasamos la provincia a enum provincia
		Provincias nuevoRolProvincia = null;
		nuevoRolProvincia = Provincias.valueOf(provinciaString);
		
		//pasamos la tipoEspecialidad a enum TipoDeEspecialidad
        TipoDeEspecialidad nuevoRolTipoDeEspecialidad = null;
        nuevoRolTipoDeEspecialidad = TipoDeEspecialidad.valueOf(tipoEspecialidad);
		
		//Buscamos el nombre y el apellido del profesional mediante el id
		String nombreCompletoProfesional = null;
		Optional<Persona> datosProfesional = servicioProfesional.buscarDatosProfesionalPorId(idProfesional);
		if (datosProfesional.isPresent()) {
			Persona nuevoProfesional = datosProfesional.get();
			// Armamos el nombre completo del profesional concatenando el nombre y el apellido
			nombreCompletoProfesional = nuevoProfesional.getApellido() + " " + nuevoProfesional.getNombre();
		}
		
		//Creamos una lista de TipoDeEspecialidades vacia para guardar el resultado del filtrado
		List<TipoDeEspecialidad> especialidadDisponibles = servicioProfesional.listarEspecialidadesDisponibles(identificador);
		
		//Buscamos los profesionales según el tipo de especialidad que se selecciono, la provincia, el rol y que este activo
		List<Profesional> profesionalesPorTipoDeEspecialidad = repositorioProfesional.findByRolAndProvinciaAndTipoEspecialidadAndActivo(Rol.PROFESIONAL, nuevoRolProvincia, nuevoRolTipoDeEspecialidad, true);

		
		//Buscamos los datos del cliente
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailCliente);
		Boolean isEspecialidadDisabled = true;
		Boolean isProfesionalDisabled = true; //Mantenemos el input del profesional activo
		Boolean isFechaDisabled = true; // habilitamos el input de la fecha
		Boolean isHorarioDisabled = false; //Mantenemos deshabilitado
		Boolean isTratamientoDisabled = false; //Mantenemos deshabilitado
		modelo.addAttribute("identificador", identificador);
		modelo.addAttribute("datosCliente", datosCliente);
		modelo.addAttribute("Profesionales", profesionalesPorTipoDeEspecialidad);
		modelo.addAttribute("tipoEspecialidad", especialidadDisponibles); // pasamos la lista de enum tipoDeEspecialidades a la vista
		modelo.addAttribute("profesionalSeleccionado", nombreCompletoProfesional);
		modelo.addAttribute("nombreDelProfesional", nombreCompletoProfesional);
		modelo.addAttribute("provincias", Provincias.values());
		modelo.addAttribute("provinciaSeleccionada", nuevoRolProvincia.getDisplayName());
		modelo.addAttribute("tipoDeEspecialidadSeleccionada", nuevoRolTipoDeEspecialidad.getDisplayName());
		modelo.addAttribute("provinciaString", provinciaString);
		modelo.addAttribute("idProfesional", idProfesional);
		modelo.addAttribute("especialidad", tipoEspecialidad);
		modelo.addAttribute("isEspecialidadDisabled", isEspecialidadDisabled); //habilitamos el select de especialidades
		modelo.addAttribute("isProfesionalDisabled", isProfesionalDisabled);
		modelo.addAttribute("isFechaDisabled", isFechaDisabled);
		modelo.addAttribute("isHorarioDisabled", isHorarioDisabled);
		modelo.addAttribute("isTratamientoDisabled", isTratamientoDisabled);
		
		if (identificador.equals("tratamientoFacial")) {
			return "/pagina_cliente/reservaDeTurnoClienteFacial";
		}else if(identificador.equals("tratamientoCorporal")) {
			return "/pagina_cliente/reservaDeTurnoClienteCorporal";
		}else if(identificador.equals("tratamientoEstetico")) {
			return "/pagina_cliente/reservaDeTurnoClienteEstetico";
		}
		return"";
	}
	
	
	@PostMapping("/seleccionDeFecha")
	public String seleccionDeFecha(
			@RequestParam String idProfesional,
			@RequestParam String idCliente,
			@RequestParam String emailCliente,
			@RequestParam String identificador,
			@RequestParam String provinciaString,
			@RequestParam String fechaSeleccionada,
			@RequestParam String nombreDelProfesional,
			@RequestParam (name ="especialidad") String tipoEspecialidad,
			Model modelo) throws MiExcepcion{
		 
		
				//Si el usuario presiona buscar sin seleccionar una fecha, por defecto toma la fecha actual como valor
				if (fechaSeleccionada.isEmpty() || fechaSeleccionada == null) {
					fechaSeleccionada = LocalDate.now().toString(); // Fecha por defecto es la fecha actual
				}
				
				//Pasamos la fecha seleccionada a localDate para poder trabajar con los dias y dias de la semana
				LocalDate fechaSeleccionadaLocalDate = servicioHorario.pasarFechaStringToLocalDate(fechaSeleccionada);
				
				//Buscamos la fecha actual del sistema
				LocalDate fechaActual = LocalDate.now();
				
				//Definimos la fecha maxima que se puede usar para sacar turnos
				//fechaMaxima toma la fecha actual del sistema y le da un meximo de dos meses hacia adelante
				LocalDate fechaMaxima = LocalDate.now().plusMonths(1);
				
				/*Verifica si existe la fecha asociada a ese id profesional en la base de datos.
				 * Si existe, nos devuelve la lista de horarios disponibles en la base de datos asociados a esa fecha
				 * Si no existe, nos devuelve una lista de horarios pre establecida */
				List<String> crearyObtenerHorariosDisponibles = servicioHorario.crearyObtenerHorariosDisponibles(fechaSeleccionada, idProfesional);
				
				for (String string : crearyObtenerHorariosDisponibles) {
					System.out.println("Se crearon los horarios para la fecha:" + string);
				}
				
				List<String> eliminarHorariosDeshabilitados = servicioHorario.eliminarHorasDisponiblesConHorasDeshabilitadas(crearyObtenerHorariosDisponibles, fechaSeleccionada, idProfesional);
				
				//Guarda en la base de datos la lista de horarios disponibles pertenecientes a la fecha y el id del profesional que le pasamos, si la
				//lista ya existe, entonces la actualiza en la base de datos
				servicioHorario.guardarHorariosDisponibles(fechaSeleccionada, eliminarHorariosDeshabilitados, idProfesional);
				
				//Solo si la fecha seleccionada es igual a la fecha actual, entonces se entra en este metodo
				if (fechaSeleccionadaLocalDate.equals(fechaActual)) {
					//Cada vez que se seleccione una fecha, se verifica los horarios pasados en comparacion con la hora actual
					//y los borra, esto sirve para impedir que se seleccionen horarios de horas que ya pasaron
					servicioHorario.EliminarHorarioViejos(idProfesional, fechaSeleccionada);
				}
				
				//Obetenemos los horarios del profesional por fecha y id del profesional, puede devolver una lista con horarios disponibles
				//o puede devolver una lista vacia si ya al profesional le solicitaron todos los horarios de esa fecha
				List <String> ObtenerHorariosDisponibles = servicioHorario.obtenerHorariosDisponiblesPorProfesionalYFecha(idProfesional, fechaSeleccionada);
				Collections.sort(ObtenerHorariosDisponibles); //ordenamos la lista por horas de menor a mayor
		
				//pasamos la provincia a enum provincia
				Provincias nuevaProvincia = null;
				nuevaProvincia = Provincias.valueOf(provinciaString);
				
				//pasamos la tipoEspecialidad a enum TipoDeEspecialidad
		        TipoDeEspecialidad nuevoRolTipoDeEspecialidad = null;
		        nuevoRolTipoDeEspecialidad = TipoDeEspecialidad.valueOf(tipoEspecialidad);
				
		        //Buscamos los profesionales según el tipo de especialidad que se selecciono, la provincia, el rol y que este activo
				List<Profesional> profesionalesPorTipoDeEspecialidad = repositorioProfesional.findByRolAndProvinciaAndTipoEspecialidadAndActivo(Rol.PROFESIONAL, nuevaProvincia, nuevoRolTipoDeEspecialidad, true);
				
				//Variables para habilitar o deshabilitar los select y los input de la pagina
				Boolean isEspecialidadDisabled = true; //habilitamos el select de especialidades
				Boolean isProfesionalDisabled = true; //Mantenemos habilitado
				Boolean isFechaDisabled = true; //mantenemos habilitado
				Boolean isHorarioDisabled = false; //Mantenemos deshabilitado
				Boolean isTratamientoDisabled = false; //Mantenemos deshabilitado
				
				
				//Buscamos los datos del usuario
				List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailCliente);
				modelo.addAttribute("datosCliente", datosCliente);
				modelo.addAttribute("identificador", identificador);
				modelo.addAttribute("provincias", Provincias.values());
				modelo.addAttribute("Profesionales", profesionalesPorTipoDeEspecialidad);
				
			
				//Metodo que recibe una fecha tipo LocalDate y devuelve true si la fecha es anterior a la actual
				if (servicioHorario.fechaYaPaso(fechaSeleccionadaLocalDate)) {
					String error = "<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"
							+ "<span fs-6'>No se puede seleccionar una fecha pasada, por favor verifique la fecha"
							+ " en la que desea seleccionar el turno.</span>";
					return servicioProfesional.manejoDeErroresControladorProfesional(identificador, error, modelo);
				}
						
				
				//Metodo que recibe una fecha tipo LocalDate y devuelve true si es fin de semana
				if (servicioHorario.esFinDeSemana(fechaSeleccionadaLocalDate)) {
					String error = "<span class='fw-bold fs-5'>Horarios de Atención:</span><br><br>" +
			                 "<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>" +
			                 "<span class='fs-6'>Queremos informarle que nuestro horario de atención es de lunes a viernes de 9:00 a 18:00 y los sábados de 9:00 a 15:00.<br><br>" +
			                 "Agradecemos su comprensión y le pedimos disculpas por cualquier inconveniente que esto pueda causar."
			                 + " Para más información puede comunicarse con nosotros por cualquiera de nuestros canales digitales.</span>";
					return servicioProfesional.manejoDeErroresControladorProfesional(identificador, error, modelo);
				}
				
				//Metodo que compara la fecha seleccionada con las fechas deshabilitadas del profesional, devuelve true si hay alguna coincidencia
				if (servicioHorario.compararFechaConFechaDeshabilitada(fechaSeleccionadaLocalDate, idProfesional)) {
					String error = "<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"
							+ "<span fs-6'>La fecha seleccionada no esta habilitada para seleccionar turnos.</span>";
					return servicioProfesional.manejoDeErroresControladorProfesional(identificador, error, modelo);
				}
				
				//Definimos una fecha maxima de dos meses a partir de la fecha actual
				//Con esto limitamos al usuario a que no pueda solicitar turnos mas alla de dos meses en adelante de la fecha actual
				if (fechaSeleccionadaLocalDate.isAfter(fechaMaxima)) {
					String error = "<span class='fs-6 fw-bold'>Estimado Cliente,</span><br><br>"
							+ "<span class='fs-6'>Todavía no tenemos turnos habilitados para esta fecha, por favor tenga en cuenta que solo es posible solicitar turnos con un máximo de 30 días a partir"
							+ " de la fecha actual. Esta política nos permite gestionar y organizar nuestros recursos de manera"
							+ " eficiente, asegurando un mejor servicio para todos nuestros clientes.<br><br>"
							+ "Le agradecemos su comprensión y colaboración. Si tiene alguna duda o necesita asistencia, no dude en contactarnos.</span>";
					return servicioProfesional.manejoDeErroresControladorProfesional(identificador, error, modelo);
				}
				
				//Si la lista de horariosDisponibles esta vacia, devuelve el mensaje de error.
				if (ObtenerHorariosDisponibles.isEmpty()) {
					String error = "<span class='fs-6 fw-bold'>Estimado Cliente,</span><br><br>"
							+ "<span class='fs-6'>Lamentamos informarle que no hay turnos disponibles para la fecha que ha"
							+ " seleccionado. Le sugerimos intentar en otro momento o seleccionar una fecha diferente para su turno.<br><br>"
							+ "Agradecemos su comprensión y le pedimos disculpas por cualquier inconveniente que esto pueda ocasionar.</span>";
					return servicioProfesional.manejoDeErroresControladorProfesional(identificador, error, modelo);
				}
					
				
				//Metodo para validar que la fecha seleccionada por el usuario corresponda a la
				//fecha laboral del profesional
				if (servicioHorario.diasLaborales(fechaSeleccionada, idProfesional)) {
					List<DiasDeLaSemana> diasLaborales = null;
					String nombreCompletoProfesional = null;
					List<String> horariosLaborales = null;
					
		    		Optional<Profesional> buscarDiasDeLaSemana = repositorioProfesional.findById(idProfesional);	
		    		if (buscarDiasDeLaSemana.isPresent()) {
						Profesional diasSeleccionados = buscarDiasDeLaSemana.get();
						diasLaborales = diasSeleccionados.getDiasDeLaSemana();
						nombreCompletoProfesional = diasSeleccionados.getApellido() + " " + diasSeleccionados.getNombre();
						horariosLaborales = diasSeleccionados.getHorariosLaborales();
					}
		    		
					String error = "<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"
							+ "<span fs-6'>Lo sentimos, pero el profesional </span> " + "<span class='fw-bold'>"+nombreCompletoProfesional.toUpperCase()+"</span>" + " <span fs-6'>solo presta"
									+ " servicios los días:</span><br><br>" + "<span class='fw-bold'>"+diasLaborales+"</span>" + "<br><br><span fs-6'> en los horarios:</span><br><br>" + "<span class='fw-bold'>"+horariosLaborales+"</span><br><br>" + ""
											+ " Lamentamos las molestias ocasionadas. Si tiene alguna duda o necesita asistencia, no dude en contactarnos.";
					return servicioProfesional.manejoDeErroresControladorProfesional(identificador, error, modelo);
				}
							
					
				//Limitamos a que el usuario solo pueda tener un maximi de tres activos
				if (servicioTurnos.checkActiveTurnos(emailCliente)) {
					String error = "<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"
							+ "<span class='fs-6'>Solo se permite tener un máximo de tres turnos activos."
							+ " Si necesita modificar un turno o seleccionar uno nuevo, puede"
							+ " ir al apartado de \"Mis turnos\" y cancerlar alguno de los turnos activos.</span>";
					return servicioProfesional.manejoDeErroresControladorProfesional(identificador, error, modelo);
				}
				
				
				//Filtramos la lista de especialidades según el identificador seleccionado
				List<TipoDeEspecialidad> especialidadDisponibles = servicioProfesional.listarEspecialidadesDisponibles(identificador);
		
				
				//Pasamos todos los datos necesarios a la vista
				modelo.addAttribute("fechaSeleccionada", fechaSeleccionada);
				modelo.addAttribute("tipoEspecialidad", especialidadDisponibles); // pasamos la lista de enum tipoDeEspecialidades a la vista
				modelo.addAttribute("especialidad", tipoEspecialidad);
				modelo.addAttribute("nombreDelProfesional", nombreDelProfesional);
				modelo.addAttribute("profesionalSeleccionado", nombreDelProfesional);
				modelo.addAttribute("provinciaSeleccionada", nuevaProvincia.getDisplayName());
				modelo.addAttribute("tipoDeEspecialidadSeleccionada", nuevoRolTipoDeEspecialidad.getDisplayName());
				modelo.addAttribute("provinciaString", provinciaString);
				modelo.addAttribute("idProfesional", idProfesional);
				
				
				//Solo si todo sale bien, pasamos los turnos disponibles a la vista
				isEspecialidadDisabled = true;
				isProfesionalDisabled = true; //mantenemos habilitado
				isFechaDisabled = true; // mantenemos habilitado
				isHorarioDisabled = true; //habilitamos el input de horario
				isTratamientoDisabled = false; //mantenemos deshabilitado
				modelo.addAttribute("isEspecialidadDisabled", isEspecialidadDisabled); //habilitamos el select de especialidades
				modelo.addAttribute("isProfesionalDisabled", isProfesionalDisabled);
				modelo.addAttribute("isFechaDisabled",isFechaDisabled);
				modelo.addAttribute("isHorarioDisabled", isHorarioDisabled);
				modelo.addAttribute("isTratamientoDisabled", isTratamientoDisabled);
				modelo.addAttribute("horarios", ObtenerHorariosDisponibles);
				
				//Seleccionamos cual vista devolver en base al identificador que viene por parametro
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
		
		
	@PostMapping("/seleccionDeHorario")
	public String seleccionDeHorario(
			@RequestParam(required = false) String idProfesional,
			@RequestParam(required = false) String idCliente,
			@RequestParam(required = false) String emailCliente,
			@RequestParam(required = false) String identificador,
			@RequestParam(required = false) String provinciaString,
			@RequestParam(required = false) String fechaSeleccionada,
			@RequestParam(required = false) String nombreDelProfesional,
			@RequestParam (name ="especialidad") String tipoEspecialidad,
			@RequestParam(required = false) String horario,
			ModelMap modelo) throws Exception {
		
		
		//pasamos la provincia a enum provincia
		Provincias nuevaProvincia = null;
		nuevaProvincia = Provincias.valueOf(provinciaString);
		
		//pasamos la tipoEspecialidad a enum TipoDeEspecialidad
        TipoDeEspecialidad nuevoRolTipoDeEspecialidad = null;
        nuevoRolTipoDeEspecialidad = TipoDeEspecialidad.valueOf(tipoEspecialidad);
		
    	List<Profesional> profesionalesPorTipoDeEspecialidad = null;
        //Buscamos los profesionales según el tipo de especialidad que se selecciono, la provincia, el rol y que este activo
		profesionalesPorTipoDeEspecialidad = repositorioProfesional.findByRolAndProvinciaAndTipoEspecialidadAndActivo(Rol.PROFESIONAL, nuevaProvincia, nuevoRolTipoDeEspecialidad, true);
		
		//Creamos una lista de TipoDeEspecialidades vacia para guardar el resultado del filtrado
		List<TipoDeEspecialidad> especialidadDisponibles = servicioProfesional.listarEspecialidadesDisponibles(identificador);
		
		//Buscamos los enum de tratamientos y lo filtramos por el tipo de especialidad seleccionado por el cliente
		List<Tratamiento> tratamientosProfesional = servicioProfesional.buscarTratamitosPorProfesional(idProfesional);
		
		//Obetenemos los horarios del profesional por fecha y id del profesional, puede devolver una lista con horarios disponibles
		//o puede devolver una lista vacia si ya al profesional le solicitaron todos los horarios de esa fecha
		List <String> ObtenerHorariosDisponibles = servicioHorario.obtenerHorariosDisponiblesPorProfesionalYFecha(idProfesional, fechaSeleccionada);
		Collections.sort(ObtenerHorariosDisponibles); //ordenamos la lista por horas de menor a mayor
		
		// Determinamos la palabra clave según el identificador
        final String palabraClave = identificador.equals("tratamientoFacial") ? "FACIAL" :
                                    identificador.equals("tratamientoCorporal") ? "CORPORAL" :
                                    identificador.equals("tratamientoEstetico") ? "ESTETICO" :
                                    null;
		
        
        // Filtramos los tratamientos
            List<Tratamiento> tratamientosFiltrados = tratamientosProfesional.stream()
                .filter(tratamiento -> tratamiento.getNombreTratamientos().toString().contains(palabraClave))
                .collect(Collectors.toList());
            
           
		//Buscamos los datos del profesional
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailCliente);
		
		//Pasamos todos los datos necesarios a la vista
		Boolean isEspecialidadDisabled = true;
		Boolean isProfesionalDisabled = true;
		Boolean isFechaDisabled = true;
		Boolean isHorarioDisabled = true;
		Boolean isTratamientoDisabled = true; //habilitamos la seleccion de tratamientos
		modelo.addAttribute("datosCliente", datosCliente);
		modelo.addAttribute("identificador", identificador);
		modelo.addAttribute("fechaSeleccionada", fechaSeleccionada);
		modelo.addAttribute("nombreDelProfesional", nombreDelProfesional);
		modelo.addAttribute("profesionalSeleccionado", nombreDelProfesional);
		modelo.addAttribute("especialidad", tipoEspecialidad);
		modelo.addAttribute("tipoEspecialidad", especialidadDisponibles); // pasamos la lista de enum tipoDeEspecialidades a la vista
		modelo.addAttribute("provinciaSeleccionada", nuevaProvincia.getDisplayName());
		modelo.addAttribute("tipoDeEspecialidadSeleccionada", nuevoRolTipoDeEspecialidad.getDisplayName());
		modelo.addAttribute("provinciaString", provinciaString);
		modelo.addAttribute("Profesionales", profesionalesPorTipoDeEspecialidad);
		modelo.addAttribute("idProfesional", idProfesional);
		modelo.addAttribute("provincias", Provincias.values());
		modelo.addAttribute("horarioSeleccionado", horario);
		modelo.addAttribute("horarios", ObtenerHorariosDisponibles);
		modelo.addAttribute("tratamientos", tratamientosFiltrados);
		modelo.addAttribute("isEspecialidadDisabled", isEspecialidadDisabled); //habilitamos el select de especialidades
		modelo.addAttribute("isProfesionalDisabled", isProfesionalDisabled);
		modelo.addAttribute("isFechaDisabled",isFechaDisabled);
		modelo.addAttribute("isHorarioDisabled", isHorarioDisabled);
		modelo.addAttribute("isTratamientoDisabled", isTratamientoDisabled);
	
		
		//Seleccionamos cual vista devolver en base al identificador que viene por parametro
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
		

	
	@PostMapping("/confimarTurnoCliente")
	public String confimarTurnoCliente(
			//@ModelAttribute Turnos turnos, //Se recibe un objeto de tipo Turnos que viene con todo los valores de los campos del formulario
			@RequestParam(required = false) String emailCliente,
			@RequestParam String action, // variable para identificar si el usuario presiona el boton aceptar o cancelar
			@RequestParam(required = false) String identificador,
			@RequestParam(required = false) String idCliente,
			@RequestParam(required = false) String nombreDelProfesional, // se recibe los valores de nombre y apellido del array de profesionales
			@RequestParam(required = false) String idProfesional,
			@RequestParam(required = false) String fechaSeleccionada,
			@RequestParam(name = "horarioSeleccionado", required = false) String horario,
			@RequestParam(required = false) String tratamientosSeleccionados, // esta variable viene con un string de los id de los tratamientos seleccionados
			@RequestParam(required = false) String provinciaString,
			@RequestParam (name ="especialidad") String tipoEspecialidad,
			ModelMap model) throws MiExcepcion {
		
		System.out.println("Action: " + action);
		
		//usamos un switch que depende si el usuario le da al boton de aceptar o cancelar del formulario
		switch (action) {
			
		case "aceptar":
			
			//pasamos la tipoEspecialidad a enum TipoDeEspecialidad
			TipoDeEspecialidad nuevoRolTipoDeEspecialidad = null;
			nuevoRolTipoDeEspecialidad = TipoDeEspecialidad.valueOf(tipoEspecialidad);
			
			//pasamos la provincia a enum provincia
			Provincias nuevaProvincia = null;
			nuevaProvincia = Provincias.valueOf(provinciaString);

			//Buscamos los profesionales según el tipo de especialidad que se selecciono, la provincia, el rol y que este activo
	        List<Profesional> profesionalesPorTipoDeEspecialidad = repositorioProfesional.findByRolAndProvinciaAndTipoEspecialidadAndActivo(Rol.PROFESIONAL, nuevaProvincia, nuevoRolTipoDeEspecialidad, true);
			
	        //Creamos una lista de TipoDeEspecialidades vacia para guardar el resultado del filtrado
	        List<TipoDeEspecialidad> especialidadDisponibles = servicioProfesional.listarEspecialidadesDisponibles(identificador);
			
			
			List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailCliente);
			Boolean envioDeMailExitoso = false; //Si el servicio de enviar el email funciona correctamente lo cambiamos a true
			Boolean turnoGuaradoExitoso = false; // si el turno se guarda exitosamente lo cambiamos a true
			try {
				
				//Servicio para guardar el turno  en la base de datos, el servicio devulve un objeto de tipo turno
				Turnos turnoGuardado = servicioTurnos.guardarTurno(idCliente, nombreDelProfesional, fechaSeleccionada, provinciaString,
						idProfesional, tratamientosSeleccionados, horario, emailCliente);
				turnoGuaradoExitoso = true; //Cambi a true si es exitoso el guardado del turno
				
				
				//Despues de generar todo el proceso del turno se envia un mail de confirmacion
				//al cliente, para esto debemos instancias un objeto EmailUsuario y pasarle toda
				//la info necesario que debe llevar el correo
				EmailUsuarios confirmarPorEmailTurno = new EmailUsuarios();
				confirmarPorEmailTurno.setAsunto("Nani estética - CONFIMACIÓN DE TURNO");
				confirmarPorEmailTurno.setDestinatario("alvarocortesia@gmail.com");
				
				//plantilla html para enviar el email con la confirmacion del turno
				String plantillaHTML = "emailConfirmacionDeTurno";
				
				//Este boolean sirve para indicar dentro del metodo si se agregar a la plantilla los valos de multa y costo de multa a la plantilla
				Boolean multa = false;
				
				//Servicion que contiene el metodo encargado de enviar el mail, recibe como parametro
				//un objeto EmailUsuario
				servicioEmail.enviarConfirmacionOCancelacionTurno(confirmarPorEmailTurno, turnoGuardado, plantillaHTML, multa);
				envioDeMailExitoso = true; // Si el servicio enviarConfirmacionOCancelacionTurno es exitoso, se cambia a true
								
				 // Primero buscamos y obtenemos una lista de los horarios disponibles por fecha e idprofesional
				//luego removemos de la lista obtenida el horario seleccionado por el usuario.
				List <String> horariosDisponibles = servicioHorario.obtenerHorariosDisponiblesPorProfesionalYFecha(idProfesional, fechaSeleccionada);
				horariosDisponibles.remove(horario);
                
                //Actualizamos y guardamos en la base de datos la lista con el horario removido por el usuario.
				servicioHorario.actualizarHorariosDisponibles(fechaSeleccionada, horariosDisponibles, idProfesional);
                
                //cuando el usuario selecciona un nuevo turno, eliminar el turno mas antiguo
                //que tenga estado inactivo y no tenga multas
               // servicioTurnos.eliminarTurnoMasAntiguoNoActivo(emailCliente);
                
                //Obtenemos una lista de turnos del usuario a traves de su id
               //List<Turnos> tunosDisponibles = servicioTurnos.buscarTurnoPorClienteId(idCliente);
				
				//Obtenemos los tunos asistidos, turnos activos, turnos cancelados y turnos con multa.
				List<Turnos> turnosActivos = servicioTurnos.obetnerTurnosPorEstadoAndActivoAndMultaAndEmailCliente(EstadoDelTurno.PENDIENTE, true, false, emailCliente);
				List<Turnos> turnosAsistidos = servicioTurnos.obetnerTurnosPorEstadoAndActivoAndMultaAndEmailCliente(EstadoDelTurno.ASISTIDO, false, false, emailCliente);
				List<Turnos> turnosConMulta = servicioTurnos.obetnerTurnosPorEstadoAndActivoAndMultaAndEmailCliente(EstadoDelTurno.CANCELADO, false, true, emailCliente);
				List<Turnos> turnosCancelados = servicioTurnos.obetnerTurnosPorEstadoAndActivoAndMultaAndEmailCliente(EstadoDelTurno.CANCELADO, false, false, emailCliente);
				
				//Filtramos las listas anteriores para que solo muestren los ultimos tres registros guardados
				List<Turnos> obtenerUltimosTresRegistrosAsistidos = servicioTurnos.obtenerUltimosTresRegistros(turnosAsistidos);
				List<Turnos> obtenerUltimosTresRegistrosActivos = servicioTurnos.obtenerUltimosTresRegistros(turnosActivos);
				List<Turnos> obtenerUltimosTresRegistrosConMulta = servicioTurnos.obtenerUltimosTresRegistros(turnosConMulta);
				List<Turnos> obtenerUltimosTresRegistrosCancelados = servicioTurnos.obtenerUltimosTresRegistros(turnosCancelados);
				
                
                
                //Se dispara el modal de exito y se redirecciona a la pagina de mis turnos
                String exito = "<span class='fs-6 fw-bold'>Estimado Cliente,</span><br><br>"
                		+ "<span class='fs-6'>Gracias por seleccionar un turno con nosotros. Nos complace poder atenderle.<br><br>"
                		+ "Es importante informarle que reservar un turno <span class='fw-bold'> no garantiza el precio del mismo</span>, ya que los precios pueden"
                		+ " variar antes de la fecha seleccionada del turno. En caso de algún aumento, le será notificado con antelación.<br><br>"
                		+ "Agradecemos su comprensión y quedamos a su disposición para cualquier consulta.<br><br>"
                		+ "Atentamente,<br><br>"
                		+ "Nani Estetica</span>";
                model.addAttribute("exito", exito);
                model.addAttribute("turnosAsistidos", obtenerUltimosTresRegistrosAsistidos);
                model.addAttribute("turnosActivos", obtenerUltimosTresRegistrosActivos);
                model.addAttribute("turnosConMulta", obtenerUltimosTresRegistrosConMulta);
                model.addAttribute("turnosCancelados", obtenerUltimosTresRegistrosCancelados);
                model.addAttribute("emailCliente", emailCliente);
                model.addAttribute("idProfesional", idProfesional);
                model.addAttribute("idCliente", idCliente);
                model.addAttribute("datosCliente", datosCliente);
                model.addAttribute("showModalExito", true);
                return "/pagina_cliente/misturnos";
              
			} catch (MiExcepcion e) {
				// si hay algun error en alguna validacion, se dispara este catch y se pasan de vuelta todos estos
				//valores para que recargue la misma pagina del formulario con un mensaje de error y con todos los
				//array de provincia, profesional y horarios
				if (turnoGuaradoExitoso && !envioDeMailExitoso && horario != null) {
					List <String> horariosDisponibles = servicioHorario.obtenerHorariosDisponiblesPorProfesionalYFecha(idProfesional, fechaSeleccionada);
					horariosDisponibles.remove(horario);
					servicioHorario.actualizarHorariosDisponibles(fechaSeleccionada, horariosDisponibles, idProfesional);
				}
				String error = e.getMessage();
				model.addAttribute("error", error);
				model.addAttribute("datosCliente", datosCliente);
				model.addAttribute("identificador", identificador);
				model.addAttribute("provincias", Provincias.values());
				model.addAttribute("tipoEspecialidad", especialidadDisponibles); // pasamos la lista de enum tipoDeEspecialidades a la vista
				model.addAttribute("Profesionales", profesionalesPorTipoDeEspecialidad);
				model.addAttribute("emailCliente", emailCliente);
				model.addAttribute("idCliente", idCliente);
				model.addAttribute("idProfesional", idProfesional);
				model.addAttribute("provinciaString", provinciaString);
				model.addAttribute("showModalError", true);
				switch (identificador) {
				case "tratamientoFacial":
					return "/pagina_cliente/reservaDeTurnoClienteFacial";
				
				case "tratamientoCorporal":
					return "/pagina_cliente/reservaDeTurnoClienteCorporal";
					
				case "tratamientoEstetico":
					return "/pagina_cliente/reservaDeTurnoClienteEstetico";
				}
			}
				
		case "cancelar":
			// si el usuario le da a cancelar, entonces entra en este case y lo redirecciona al metodo
			// de tratamientos y este a su vez se encarga de mostrar la pag de los tratamientos
			return "redirect:/tratamientos?email=" + emailCliente;
		}
		return "redirect:/tratamientos?email=" + emailCliente;
	}
	
	
	//Este es el controlador que valida si esta lleno el formulario de preguntas.
	/*Se usa postmapping por que los valores que recibe este metodo vienen de un formulario con un metodo post*/
	/*Valida si el formulario de preguntas ya se lleno y le muestra al cliente la pagina correspondiente dependiendo de su seleccion*/
	@PostMapping("/reservaDeTurnoClienteGeneral")
	public String reservaDeTurnoClienteGeneral(
			@RequestParam(required = false) String email,
			@RequestParam String idCliente,
			@RequestParam String tratamiento,
			ModelMap model) {
		
		
		/*El valor de tratamiento sirve para filtrar que pagina le vamos a mostrar
		 * al cliente para solicitar turno, puede ser facial o corporal*/
		Boolean usoDeFormulario = null;
		Optional<Cliente> formularioDeDatos = repositorioCliente.findById(idCliente);
		if (formularioDeDatos.isPresent()) {
			Cliente validarUsoFormulario = formularioDeDatos.get();
			usoDeFormulario = validarUsoFormulario.getFomularioDatos();
		}
					
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		Boolean isDisabled = null;  // esta booleano sirve para validar cuando mostramos el input de la seleccion de fechas, si es true se muestra activo si es false de muestra deshabilitado
		Boolean isHorarioDisabled = null;
		model.addAttribute("tratamiento", tratamiento);
		model.addAttribute("datosCliente", datosCliente);
		model.addAttribute("idCliente", idCliente);
		model.addAttribute("email", email);
		model.addAttribute("provincias", Provincias.values());
		
		//la variable identificador la usamos para los case de los otros metodos, asi le decimos si se selecciona tratamiento
		//facial o tratamiento corporal
		String identificador = null;
		if (usoDeFormulario && tratamiento.equals("facial")) {
			identificador = "tratamientoFacial";
			isDisabled = false;
			isHorarioDisabled = false;
			model.addAttribute("identificador", identificador);
			model.addAttribute("isDisabled", isDisabled);  // Mandamos el valor del booleano a la vista para determinar si mostrados el input de la fecha habilitado o no
			model.addAttribute("isHorarioDisabled", isHorarioDisabled);
			return "/pagina_cliente/reservaDeTurnoClienteFacial";
			
		}else if(usoDeFormulario && tratamiento.equals("corporal")) {
			identificador = "tratamientoCorporal";
			isDisabled = false;
			isHorarioDisabled = false;
			model.addAttribute("identificador", identificador);
			model.addAttribute("isDisabled", isDisabled);
			model.addAttribute("isHorarioDisabled", isHorarioDisabled);
			return "/pagina_cliente/reservaDeTurnoClienteCorporal";
			
			//Si el boolean usoDeFormulario es false, entonces dirige al usuario a llenar el formulario de preguntas
		}else if(tratamiento.equals("estetico")){
			identificador = "tratamientoEstetico";
			isDisabled = false;
			isHorarioDisabled = false;
			model.addAttribute("identificador", identificador);
			model.addAttribute("isDisabled", isDisabled);
			model.addAttribute("isHorarioDisabled", isHorarioDisabled);
			return "/pagina_cliente/reservaDeTurnoClienteEstetico";
		}else {
			return "/pagina_cliente/formularioPreguntas";
		}
	}
		
			
	
			
	@GetMapping("/misconsultas")
	public String misconsultas(
			@RequestParam String email,
			@RequestParam String idCliente,
			Model modelo) {
		
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		
		List<Turnos> ultimosTurnosCliente = servicioTurnos.buscarTurnosPorClienteAndoEstadoDelTurno(idCliente, EstadoDelTurno.ASISTIDO);
		List<Turnos> tresUltimosTurnos = new ArrayList<>();
		int size = ultimosTurnosCliente.size();
		if (size >= 3) {
		    tresUltimosTurnos = ultimosTurnosCliente.subList(size - 3, size);
		} else {
		    tresUltimosTurnos = ultimosTurnosCliente; // Si hay menos de 3 elementos, tomamos todos
		}
		
		LocalDateTime fechaPrueba = LocalDateTime.now();
		
		modelo.addAttribute("email", email);
		modelo.addAttribute("datosDelTurno", tresUltimosTurnos);
		modelo.addAttribute("fechaPrueba", fechaPrueba);
		modelo.addAttribute("datosCliente", datosCliente);
	return "/pagina_cliente/misconsultas";	
	}
	
	
	@GetMapping("/completarDatosCliente")
	public String completarDatos(
			@RequestParam(name = "email") String emailUsuario,
			@RequestParam(required = false) String error,
			ModelMap modelo) {
		
		modelo.addAttribute("emailUsuario", emailUsuario);
		modelo.addAttribute("error", error);
		return "/pagina_cliente/completarDatosCliente";
	}
	
	
	
	//Devuelve la pagina homeCLiente con los datos del usuario que le pasemos por mmail
	@GetMapping("/homeCliente")
	public String homeCliente(
			@RequestParam String email, Model model) {
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosCliente", datosCliente);
		return "/pagina_cliente/homeCliente";	
		
	}
	
	
	
	//Muestra todos los datos personales de la persona en la pagina misdatosCliente
	@GetMapping("/misdatosCliente")
	public String misdatosCliente(
			@RequestParam(required = false) String email,
			Model model) { // el valor del parametro email viene del html homeCliente
		
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosCliente", datosCliente);
		model.addAttribute("sexos", Sexo.values());
	return "/pagina_cliente/misdatosCliente";	
	}
	
	
	@PostMapping("guardarDatosCliente")
	public String guardarDatosCliente(
			@RequestParam(required = false) String nombre,
			@RequestParam(required = false) String apellido,
			@RequestParam(name = "numeroDoc", required = false) String dni,
			@RequestParam(required = false) String sexo,
			@RequestParam(required = false) String telefono,
			@RequestParam(required = false) String direccion,
			@RequestParam(required = false) String ocupacion,
			@RequestParam(required = false) String emailUsuario, //Esta valor viene del input oculto de la hoja completarDatos, que a su vez viene del meotodo Login en ControladorPagina
			ModelMap model
			) throws MiExcepcion {
		
		try {
			
			//Guardamos los datos del formulario que lleno el nuevo cliente
			servicioCliente.registrarCliente(emailUsuario, dni, nombre, apellido, ocupacion, direccion, telefono, sexo);
			return "redirect:/homeCliente?email=" + emailUsuario; //redirecionamos al metodo homeCliente enviando la varibale mail
				
		} catch (MiExcepcion e) {
			String error = e.getMessage();
			model.addAttribute("emailUsuario", emailUsuario);
			model.addAttribute("nombre", nombre);
			model.addAttribute("apellido", apellido);
			model.addAttribute("numeroDoc", dni);
            model.addAttribute("sexo", sexo);
            model.addAttribute("telefono", telefono);
            model.addAttribute("direccion", direccion);
            model.addAttribute("ocupacion", ocupacion);
            model.addAttribute("error", error);
            model.addAttribute("showModalError", true);
			return "pagina_cliente/completarDatosCliente";
			
		}
	}
	
	@PostMapping("/actualizarDatosCliente")
	public String actualizarDatosCliente(
			@RequestParam String idCliente, //este atributo es enviado en un input oculto de la pag misdatosCliente
			@RequestParam(required = false) String ocupacion, // Este y los demas atributos los puse como no requeridos para poder personalizar las excepciones
		    @RequestParam(required = false) String email,
			@RequestParam(required = false) String domicilio,
			@RequestParam(required = false) String sexo,
			@RequestParam(required = false) String telefono,
			ModelMap model) throws MiExcepcion {
		
		//Buscamos mediante el id el mail anterior del cliente y lo guardamos en la variable emailAnterior por si acaso deja el campo de email vacio o coloca un email no valido
		// entonces usamos este mail anterior para poder pasarlo al controlador de misdatosClientes y poder visualizar los datos del cliente
		// Tambien buscamos los valores previamente guardados en la base de datos para poder compararlos con los nuevos
		String ocupacionAnterior = null;
		String emailAnterior = null;
		String domicilioAnterior = null;
		Sexo sexoAnterior = null;
		String nuevoSexo = null;
		String telefonoAnterior = null;
		
		Optional<Cliente> identificarCliente = repositorioCliente.findById(idCliente);
		if (identificarCliente.isPresent()) {
			Cliente datosAnteriorCliente = identificarCliente.get();
			emailAnterior = datosAnteriorCliente.getEmail();
			ocupacionAnterior = datosAnteriorCliente.getOcupacion().toUpperCase();
			domicilioAnterior = datosAnteriorCliente.getDomicilio().toUpperCase();
			sexoAnterior = datosAnteriorCliente.getSexo();
			nuevoSexo = sexoAnterior.toString().toUpperCase();
			telefonoAnterior = datosAnteriorCliente.getTelefono().toUpperCase();
		}
		
		//Cuando el cliente le da guardar a los datos sin modificar nada el sexo viene con valor vacioa
		//por eso armamos este condicional para manejar el error y asignarle un valor a sexo
		//que es el valor que tiene guardado en cliente en la base
		if (sexo == null || sexo.isEmpty()) {
			sexo = nuevoSexo;
		}
		
		if (ocupacionAnterior == null || ocupacionAnterior.isEmpty()) {
			ocupacionAnterior = ocupacion;
		}
		
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email); //Buscamos todos los datos pertenecientes al cliente despues de haber sido actualizados en la base de datos y los mostramos en el campo correspondiente
		//Teniendo el valor de los datos guardados y los que envian al presionar guardar en el formualario podemos comparar si se hiz alguna modificaicon
				//de los datos, si presiona guardar y no se modifico nada, recargar la misma pagina y no muestra ningun mensaje
		if (ocupacionAnterior.equals(ocupacion.toUpperCase()) && domicilioAnterior.equals(domicilio.toUpperCase()) && nuevoSexo.equals(sexo.toUpperCase()) && telefonoAnterior.equals(telefono.toUpperCase())) {
			model.addAttribute("emial", email);
			model.addAttribute("sexos", Sexo.values());
			model.addAttribute("datosCliente",datosCliente);
			return "/pagina_cliente/misdatosCliente";
			
		}
		
		try {
			//este metodo verifica valida el mail y los nuevos datos del cliente y los remplaza en la base de datos
			servicioCliente.modificarCliente(idCliente, ocupacion, email, emailAnterior, domicilio, sexo, telefono );
			List <Usuario> datosClienteActualizados = servicioUsuario.buscarPorEmail(email);
			String exito = "<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"
							+"<span class='fs-6'>Sus datos han sido actualizados de forma exitosa</span>";
			model.addAttribute("datosCliente",datosClienteActualizados);
			model.addAttribute("exito",exito);
			model.addAttribute("sexos", Sexo.values());
			model.addAttribute("showModalExito", true);
			return "/pagina_cliente/misdatosCliente";
			
		} catch (MiExcepcion e) {
			String error = e.getMessage(); // en la exepcion e.getmessage obtenenos el valor de la exepcion personalizada que se de y la enviamos al controlador de misdatosCliente para ser monstrada en pantalla
			List <Usuario> datosClienteAnterior = servicioUsuario.buscarPorEmail(emailAnterior); //Buscamos los datos ateriores a la excepcion y los mostramos en caso de que haya un error por parte del usuario
			model.addAttribute("datosCliente",datosClienteAnterior);
			model.addAttribute("sexos", Sexo.values());
			model.addAttribute("error",error);
			model.addAttribute("showModalError", true);
			return "/pagina_cliente/misdatosCliente";
		}
	}
					
}
