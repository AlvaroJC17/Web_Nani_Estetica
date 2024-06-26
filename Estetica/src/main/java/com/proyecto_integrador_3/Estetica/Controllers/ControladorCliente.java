package com.proyecto_integrador_3.Estetica.Controllers;

import static java.lang.Boolean.TRUE;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Turnos;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioTurnos;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCliente;
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
	public ServicioCliente servicioCliente;
	
	@Autowired
	public ServicioProfesional servicioProfesional;
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	@Autowired
	public ServicioHorario servicioHorario;
	
	@Autowired
	public ServicioTurnos servicioTurnos;
	
	
	@GetMapping("/multas")
	public String multas(
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "idCliente", required = false) String idCliente,
			ModelMap model) {
		
		 Optional <Turnos> bucarDatosTurno = repositorioTurnos.findByClienteIdAndMulta(idCliente, TRUE);
 		
		 LocalDate fecha = null;
		 String horario = null;
		 String costoMulta = null;
		 
		    if (bucarDatosTurno.isPresent()) {
				Turnos datosTurno = bucarDatosTurno.get();
				fecha = datosTurno.getFecha();
				horario = datosTurno.getHorario();
				costoMulta = datosTurno.getCostoMulta();
		    }
				
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("fecha", fecha);
		model.addAttribute("horario", horario);
		model.addAttribute("costoMulta", costoMulta);
		model.addAttribute("datosCliente", datosCliente);
		return "/pagina_cliente/multas";
	}
	
	
	/*Este metodo deriva a la pagina de tratamientos con los valores de mail y id del cliente*/
	@GetMapping("/tratamientos")
	public String tratamientos(
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "idCliente", required = false) String idCliente,
			ModelMap model) {
		
		//Antes de mostrar los tratamientos disponibles, verificamos que no hay turno antiguos
		//no asistodos, de ser afirmativo, le colocamos una multa al turno
		servicioTurnos.actualizarTurnosAntiguos(email);
		
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
			
		
		
	
	@GetMapping("/buscarProfesionalPorProvincias")
	public String buscarProfesionalPorProvincias(
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "idCliente", required = false) String idCliente,
			@RequestParam(name="identificador", required = false) String identificador,
			@RequestParam(name="provincia") String provincia,
			ModelMap model) throws MiExcepcion {
		
		//pasamos la provincia a enum provincia
		Provincias nuevoRolProvincia = null;
		nuevoRolProvincia = Provincias.valueOf(provincia);
	
		//Buscamos todos los datos del cliente y lo pasamos al html, sirve para visualizar la pagina y pasar los datos del cliente entre controladores
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		Boolean isDisabled = false; //Booleano para habilitar o deshabilitar el input de fecha
		model.addAttribute("datosCliente", datosCliente);
		model.addAttribute("identificador", identificador);
		model.addAttribute("provinciaString", provincia);
		model.addAttribute("isDisabled ", isDisabled);
		model.addAttribute("provinciaSeleccionada", nuevoRolProvincia.getDisplayName()); //getDisplayName Muestra los nombre personalizados de los enum y no los nombres en Mayusculas
		model.addAttribute("provincias", Provincias.values()); // pasamos el array de enums al formulario para desplegar la lista de select
		
		String error = null;
		//validamos que se seleccione una provincia
		if (!servicioProfesional.validarProvincia(provincia)) {
			isDisabled = false;
			error ="<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"+
			"<span class='fs-6'>Es necesario que seleccione la provincia donde vive para poder ver que"
			+ " profesionales estan disponibles.</span>";
			model.addAttribute("error", error);
			model.addAttribute("isDisabled", isDisabled);
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
		
		//Hacemos un busqueda de usuarios por rol profesional, provincia y que esten activos
		List<Persona> profesionalesPorProvincia = servicioProfesional.buscarProfesionaByRolAndProvinciasYActivo(Rol.PROFESIONAL, nuevoRolProvincia, true);
		
		//si no hay problema con las validaciones anteriores, buscamos los profesionales
		//y pasamos el nombre de la provincia y las agregamos a la vista.
		model.addAttribute("Profesionales", profesionalesPorProvincia); // pasamos el array de objetos tipo persona que tengan rol de profesional
		
		//Validamos que si la lista de profesionales esta vacia para esa provincia, mandamos un
		//error con un mensaje
		if (profesionalesPorProvincia.isEmpty()) {
			error = "<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"
					+"<span class='fs-6'>Lamentamos informarle que no hay profesionales disponibles para la provincia seleccionada.</span>";
			isDisabled = false;
			model.addAttribute("error", error);
			model.addAttribute("isDisabled", isDisabled);
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
		
	
	

//	@GetMapping("/reservaDeTurnoCliente")
//	public String reservaDeTurnoCliente(
//			@RequestParam(name = "email", required = false) String email,
//			@RequestParam(name = "idCliente", required = false) String idCliente,
//			@RequestParam(name = "fecha", required = false) String fecha,
//			@RequestParam(name="identificador", required = false) String identificador,
//			@RequestParam(name="provincia") String provincia,
//			ModelMap model) throws MiExcepcion {
//		
//
//		
//		
//		
//		
//			//Buscamos el nombre y apellido en la tabla persona mediante el rol PROFESIONAL
//			model.addAttribute("datosCliente", datosCliente);
//			model.addAttribute("identificador", identificador);
//			model.addAttribute("provincias", Provincias.values()); // pasamos el array de enums al formulario para desplegar la lista de select
//			model.addAttribute("Profesional", Profesional); // pasamos el array de objetos tipo persona que tengan rol de profesional
////			model.addAttribute("horarios", horariosDisponibles);
//	        model.addAttribute("fecha", fecha); // Pasar la fecha seleccionada al modelo
//			model.addAttribute("Turnos", new Turnos()); // creamos una instancia de Turnos para pasarla al formulario y cargarlo con todos los campos del formulario
//			
//			//Si pasa todos los filtros anteriores, usa el identificador y redireciona a la pagina
//			switch (identificador) {
//			case "tratamientoFacial":
//				return "/pagina_cliente/reservaDeTurnoClienteFacial";
//			case "tratamientoCorporal":
//				return "/pagina_cliente/reservaDeTurnoClienteCorporal";
//			case "tratamientoEstetico":
//				return "/pagina_cliente/reservaDeTurnoClienteEstetico";
//			}
//			
//			return "";
//	}
	
	@PostMapping("/buscarProfesional")
	public String buscarProfesional(
			@RequestParam(name ="idProfesional", required = false) String idProfesional,
			@RequestParam(name ="idCliente", required = false) String idCliente,
			@RequestParam(name ="emailCliente", required = false) String emailCliente,
			@RequestParam(name ="identificador", required = false) String identificador,
			@RequestParam(name ="provinciaString", required = false) String provinciaString,
			Model modelo) throws MiExcepcion {	
		
		
		//pasamos la provincia a enum provincia
		Provincias nuevaProvincia = null;
		nuevaProvincia = Provincias.valueOf(provinciaString);
		
		//Buscamos el nombre y el apellido del profesional mediante el id
		String nombreProfesiona = null;
		String apellidoProfesiona = null;
		Optional<Persona> datosProfesional = servicioProfesional.buscarDatosProfesionalPorId(idProfesional);
		if (datosProfesional.isPresent()) {
			Persona nuevoProfesional = datosProfesional.get();
			nombreProfesiona = nuevoProfesional.getNombre();
			apellidoProfesiona = nuevoProfesional.getApellido();
		}
		
		// Armamos el nombre completo del profesional concatenando el nombre y el apellido
		String nombreCompletoProfesional = nombreProfesiona + " " + apellidoProfesiona;
		
		//Buscamos los profesionales por provincia
		List<Persona> profesionalesPorProvincia = servicioProfesional.buscarProfesionaByRolAndProvincis(Rol.PROFESIONAL, nuevaProvincia);

		//Buscamos los datos del profesional
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailCliente);
		
		Boolean isDisabled = true;
		modelo.addAttribute("identificador", identificador);
		modelo.addAttribute("datosCliente", datosCliente);
		modelo.addAttribute("Profesionales", profesionalesPorProvincia);
		modelo.addAttribute("nombreDelProfesional", nombreCompletoProfesional);
		modelo.addAttribute("provincias", Provincias.values());
		modelo.addAttribute("provinciaSeleccionada", nuevaProvincia.getDisplayName());
		modelo.addAttribute("provinciaString", provinciaString);
		modelo.addAttribute("idProfesional", idProfesional);
		modelo.addAttribute("isDisabled", isDisabled);
		
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
			@RequestParam(name ="idProfesional", required = false) String idProfesional,
			@RequestParam(name ="idCliente", required = false) String idCliente,
			@RequestParam(name ="emailCliente", required = false) String emailCliente,
			@RequestParam(name ="identificador", required = false) String identificador,
			@RequestParam(name ="provinciaString", required = false) String provinciaString,
			@RequestParam(name ="fechaSeleccionada", required = false) String fechaSeleccionada,
			@RequestParam(name ="nombreDelProfesional", required = false) String nombreDelProfesional,
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
				
				//Guarda en la base de datos la lista de horarios disponibles pertenecientes a la fecha y el id del profesional que le pasamos
				servicioHorario.guardarHorariosDisponibles(fechaSeleccionada, crearyObtenerHorariosDisponibles, idProfesional);
				
				//Solo si la fecha seleccionada es igual a la fecha actual, entonces se entra en este metodo
				if (fechaSeleccionadaLocalDate.equals(fechaActual)) {
					//Cada vez que se seleccione una fecha, se verifica los horarios pasados en comparacion con la hora actual
					//y los borra, esto sirve para impedir que se seleccionen horarios de horas que ya pasaron
					servicioHorario.EliminarHorarioViejos(idProfesional, fechaSeleccionada);
				}
				
				//Obetenemos los horarios del profesional por fecha y id del profesional, puede devolver una lista con horarios disponibles
				//o puede devolver una lista vacia si ya al profesional le solicitaron todos los horarios de esa fecha
				List <String> ObtenerHorariosDisponibles = servicioHorario.obtenerHorariosDisponiblesPorProfesionalYFecha(idProfesional, fechaSeleccionada);
				//Despues de obtener los horarios, los ordenamos por fecha, para que siempre se muestren los horarios en orden de menor a mayor
				Collections.sort(ObtenerHorariosDisponibles);
		
				//pasamos la provincia a enum provincia
				Provincias nuevaProvincia = null;
				nuevaProvincia = Provincias.valueOf(provinciaString);
				
				//Buscamos los profesionales por provincia
				List<Persona> profesionalesPorProvincia = servicioProfesional.buscarProfesionaByRolAndProvincis(Rol.PROFESIONAL, nuevaProvincia);
		
				//Buscamos los datos del profesional
				List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailCliente);
				
				//Pasamos todos los datos necesarios a la vista
				Boolean isDisabled = null;
				Boolean isHorarioDisabled = null;
				modelo.addAttribute("datosCliente", datosCliente);
				modelo.addAttribute("identificador", identificador);
				modelo.addAttribute("fechaSeleccionada", fechaSeleccionada);
				modelo.addAttribute("nombreDelProfesional", nombreDelProfesional);
				modelo.addAttribute("provinciaSeleccionada", nuevaProvincia.getDisplayName());
				modelo.addAttribute("provinciaString", provinciaString);
				modelo.addAttribute("Profesionales", profesionalesPorProvincia);
				modelo.addAttribute("idProfesional", idProfesional);
				modelo.addAttribute("provincias", Provincias.values());
			
	    //Metodo que recibe una fecha tipo LocalDate y devuelve true si la fecha es anterior a la actual
				if (servicioHorario.fechaYaPaso(fechaSeleccionadaLocalDate)) {
					String error = "<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"
							+ "<span fs-6'>No se puede seleccionar una fecha pasada, por favor verifique la fecha"
							+ " en la que desea seleccionar el turno.</span>";
					isDisabled = true;
					modelo.addAttribute("error", error);
					modelo.addAttribute("showModalError", true);
					modelo.addAttribute("isDisabled", isDisabled);
					if (identificador.equals("tratamientoFacial")) {
						return "/pagina_cliente/reservaDeTurnoClienteFacial";
					}else if(identificador.equals("tratamientoCorporal")) {
						return "/pagina_cliente/reservaDeTurnoClienteCorporal";
					}else if(identificador.equals("tratamientoEstetico")) {
						return "/pagina_cliente/reservaDeTurnoClienteEstetico";
					}
						
				}
				
//	   //Metodo que recibe una fecha tipo LocalDate y devuelve true si es fin de semana
//				if (servicioCliente.esFinDeSemana(fechaSeleccionadaLocalDate)) {
//					String error = "<span class='fw-bold fs-5'>Horarios de Atención:</span><br><br>" +
//			                 "<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>" +
//			                 "<span class='fs-6'>Queremos informarle que nuestro horario de atención es de lunes a viernes de 9:00 a 18:00 y los sábados de 9:00 a 15:00.<br><br>" +
//			                 "Agradecemos su comprensión y le pedimos disculpas por cualquier inconveniente que esto pueda causar."
//			                 + " Para más información puede comunicarse con nosotros por cualquiera de nuestros canales digitales.</span>";
//					isDisabled = true;
//					modelo.addAttribute("error", error);
//					modelo.addAttribute("showModalError", true);
//					modelo.addAttribute("isDisabled", isDisabled);
//					if (identificador.equals("tratamientoFacial")) {
//						return "/pagina_cliente/reservaDeTurnoClienteFacial";
//					}else if(identificador.equals("tratamientoCorporal")) {
//						return "/pagina_cliente/reservaDeTurnoClienteCorporal";
//					}else if(identificador.equals("tratamientoEstetico")) {
//						return "/pagina_cliente/reservaDeTurnoClienteEstetico";
//					}
//				}
				
				//Definimos una fecha maxima de dos meses a partir de la fecha actual
				//Con esto limitamos al usuario a que no pueda solicitar turnos mas alla de dos meses en adelante de la fecha actual
				if (fechaSeleccionadaLocalDate.isAfter(fechaMaxima)) {
					String error = "<span class='fs-6 fw-bold'>Estimado Cliente,</span><br><br>"
							+ "<span class='fs-6'>Todavía no tenemos turnos habilitados para esta fecha, por favor tenga en cuenta que solo es posible solicitar turnos con un máximo de 30 días a partir"
							+ " de la fecha actual. Esta política nos permite gestionar y organizar nuestros recursos de manera"
							+ " eficiente, asegurando un mejor servicio para todos nuestros clientes.<br><br>"
							+ "Le agradecemos su comprensión y colaboración. Si tiene alguna duda o necesita asistencia, no dude en contactarnos.</span>";
					modelo.addAttribute("error", error);
					isDisabled = true;
					modelo.addAttribute("showModalError", true);
					modelo.addAttribute("isDisabled", isDisabled);
					if (identificador.equals("tratamientoFacial")) {
						return "/pagina_cliente/reservaDeTurnoClienteFacial";
					}else if(identificador.equals("tratamientoCorporal")) {
						return "/pagina_cliente/reservaDeTurnoClienteCorporal";
					}else if(identificador.equals("tratamientoEstetico")) {
						return "/pagina_cliente/reservaDeTurnoClienteEstetico";
					}
				}
				
				//Si la lista de horariosDisponibles esta vacia, devuelve el mensaje de error.
				if (ObtenerHorariosDisponibles.isEmpty()) {
					String error = "<span class='fs-6 fw-bold'>Estimado Cliente,</span><br><br>"
							+ "<span class='fs-6'>Lamentamos informarle que no hay turnos disponibles para la fecha que ha"
							+ " seleccionado. Le sugerimos intentar en otro momento o seleccionar una fecha diferente para su turno.<br><br>"
							+ "Agradecemos su comprensión y le pedimos disculpas por cualquier inconveniente que esto pueda ocasionar.</span>";
					isDisabled = true;
					modelo.addAttribute("error", error);
					modelo.addAttribute("showModalError", true);
					modelo.addAttribute("isDisabled", isDisabled);
					if (identificador.equals("tratamientoFacial")) {
						return "/pagina_cliente/reservaDeTurnoClienteFacial";
					}else if(identificador.equals("tratamientoCorporal")) {
						return "/pagina_cliente/reservaDeTurnoClienteCorporal";
					}else if(identificador.equals("tratamientoEstetico")) {
						return "/pagina_cliente/reservaDeTurnoClienteEstetico";
					}
				}
				
				
				//Limitamos a que el usuario solo pueda tener un maximi de tres activos
				if (servicioTurnos.checkActiveTurnos(emailCliente)) {
					String error = "<span class='fs-6 fw-bold'>Estimado cliente,</span><br><br>"
							+ "<span class='fs-6'>Solo se permite tener un máximo de tres turnos activos."
							+ " Si necesita modificar un turno o seleccionar uno nuevo, puede"
							+ " ir al apartado de \"Mis turnos\" y cancerlar alguno de los turnos activos.</span>";
					isDisabled = true;
					modelo.addAttribute("error", error);
					modelo.addAttribute("showModalError", true);
					modelo.addAttribute("isDisabled", isDisabled);
					if (identificador.equals("tratamientoFacial")) {
						return "/pagina_cliente/reservaDeTurnoClienteFacial";
					}else if(identificador.equals("tratamientoCorporal")) {
						return "/pagina_cliente/reservaDeTurnoClienteCorporal";
					}else if(identificador.equals("tratamientoEstetico")) {
						return "/pagina_cliente/reservaDeTurnoClienteEstetico";
					}
				}
				
				
				//Solo si todo sale bien, pasamos los turnos disponibles a la vista
				isDisabled = true;
				isHorarioDisabled = true;
				modelo.addAttribute("isDisabled",isDisabled);
				modelo.addAttribute("isHorarioDisabled", isHorarioDisabled);
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
		
		
		

	
	@PostMapping("/confimarTurnoCliente")
	public String confimarTurnoCliente(
			@ModelAttribute Turnos turnos, //Se recibe un objeto de tipo Turnos que viene con todo los valores de los campos del formulario
			@RequestParam(name = "emailCliente", required = false) String emailCliente,
			@RequestParam(name = "action") String action, // variable para identificar si el usuario presiona el boton aceptar o cancelar
			@RequestParam(name = "identificador", required = false) String identificador,
			@RequestParam(name = "idCliente", required = false) String idCliente,
			@RequestParam(name = "nombreDelProfesional", required = false) String nombreDelProfesional, // se recibe los valores de nombre y apellido del array de profesionales
			@RequestParam(name = "idProfesional", required = false) String idProfesional,
			@RequestParam(name = "fechaSeleccionada", required = false) String fechaSeleccionada,
			@RequestParam(name = "horario", required = false) String horario,
			@RequestParam(name = "provinciaString", required = false) String provinciaString,
			@RequestParam(name = "facial", required = false) String facial,
			@RequestParam(name = "espalda", required = false) String espalda,
			@RequestParam(name = "pulido", required = false) String pulido,
			@RequestParam(name = "dermaplaning", required = false) String dermaplaning,
			@RequestParam(name = "exfoliacion", required = false) String exfoliacion,
			@RequestParam(name = "lifting", required = false) String lifting,
			@RequestParam(name = "perfilado", required = false) String perfilado,
			@RequestParam(name = "laminado", required = false) String laminado,
			@RequestParam(name = "hydralips", required = false) String hydralips,
			@RequestParam(name = "microneedling", required = false) String microneedling,
			ModelMap model) throws MiExcepcion {
		
		//usamos un switch que depende si el usuario le da al boton de aceptar o cancelar del formulario
		switch (action) {
			
		case "aceptar":
			List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailCliente);
			try {
				//Servicio para guardar el turno facial en la base de datos
				servicioTurnos.guardarTurno(idCliente, nombreDelProfesional, fechaSeleccionada, provinciaString,
						idProfesional, facial, espalda, pulido, dermaplaning, exfoliacion, lifting, perfilado, laminado,
						hydralips, microneedling, horario, emailCliente);
				
				
				 // Primero buscamos y obtenemos una lista de los horarios disponibles por fecha e idprofesional
				//luego removemos de la lista obtenida el horario seleccionado por el usuario.
				List <String> horariosDisponibles = servicioHorario.obtenerHorariosDisponiblesPorProfesionalYFecha(idProfesional, fechaSeleccionada);
				horariosDisponibles.remove(horario);
                
                //Actualizamos y guardamos en la base de datos la lista con el horario removido por el usuario.
				servicioHorario.actualizarHorariosDisponibles(fechaSeleccionada, horariosDisponibles, idProfesional);
                
                //cuando el usuario selecciona un nuevo turno, eliminar el turno mas antiguo
                //que tenga estado inactivo y no tenga multas
                servicioTurnos.eliminarTurnoMasAntiguoNoActivo(emailCliente);
                
                
                //Obtenemos una lista de turnos del usuario a traves de su id
                List<Turnos> tunosDisponibles = servicioTurnos.buscarTurnoPorClienteId(idCliente);
                
                
                //Se dispara el modal de exito y se redirecciona a la pagina de mis turnos
                String exito = "<span class='fs-6 fw-bold'>Estimado Cliente,</span><br><br>"
                		+ "<span class='fs-6'>Gracias por seleccionar un turno con nosotros. Nos complace poder atenderle.<br><br>"
                		+ "Es importante informarle que reservar un turno <span class='fw-bold'> no garantiza el precio del mismo</span>, ya que los precios pueden"
                		+ " variar antes de la fecha seleccionada del turno. En caso de algún aumento, le será notificado con antelación.<br><br>"
                		+ "Agradecemos su comprensión y quedamos a su disposición para cualquier consulta.<br><br>"
                		+ "Atentamente,<br><br>"
                		+ "Nani Estetica</span>";
                model.addAttribute("exito", exito);
                model.addAttribute("datosTurno", tunosDisponibles);
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
				
				String error = e.getMessage();
				model.addAttribute("error", error);
				model.addAttribute("datosCliente", datosCliente);
				model.addAttribute("identificador", identificador);
				model.addAttribute("provincias", Provincias.values());
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
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "idCliente") String idCliente,
			@RequestParam(name = "tratamiento") String tratamiento,
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
			@RequestParam(name="email") String email,
			Model modelo) {
		
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		modelo.addAttribute("email", email);
		modelo.addAttribute("datosCliente", datosCliente);
	return "/pagina_cliente/misconsultas";	
	}
	
	
	
	@GetMapping("/cambiarContrasenaCliente")
	public String cambiarContrasenaCliente(
			@RequestParam(name = "email") String email,
			@RequestParam(name = "exito", required = false) String exito,
			@RequestParam(name = "error", required = false) String error,
			ModelMap model) {
		
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		
		model.addAttribute("datosCliente", datosCliente);
		model.addAttribute("exito", exito);
		model.addAttribute("error", error);
		return "/pagina_cliente/cambiarContrasenaCliente";
	}
		
	
	
	@GetMapping("/completarDatosCliente")
	public String completarDatos(
			@RequestParam(name = "email") String emailUsuario,
			@RequestParam(name = "error", required = false) String error,
			ModelMap modelo) {
		
		modelo.addAttribute("emailUsuario", emailUsuario);
		modelo.addAttribute("error", error);
		return "/pagina_cliente/completarDatosCliente";
	}
	
	
	
	//Devuelve la pagina homeCLiente con los datos del usuario que le pasemos por mmail
	@GetMapping("/homeCliente")
	public String homeCliente(
			@RequestParam(name = "email") String email, Model model) {
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosCliente", datosCliente);
		return "/pagina_cliente/homeCliente";	
		
	}
	
	
	
	//Muestra todos los datos personales de la persona en la pagina misdatosCliente
	@GetMapping("/misdatosCliente")
	public String misdatosCliente(
			@RequestParam(name="email", required = false) String email,
			Model model) { // el valor del parametro email viene del html homeCliente
		
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosCliente", datosCliente);
		model.addAttribute("sexos", Sexo.values());
	return "/pagina_cliente/misdatosCliente";	
	}
	
	
	@PostMapping("guardarDatosCliente")
	public String guardarDatosCliente(
			@RequestParam(name = "nombre", required = false) String nombre,
			@RequestParam(name = "apellido", required = false) String apellido,
			@RequestParam(name = "numeroDoc", required = false) String dni,
			@RequestParam(name = "sexo", required = false) String sexo,
			@RequestParam(name = "telefono", required = false) String telefono,
			@RequestParam(name = "direccion", required = false) String direccion,
			@RequestParam(name = "ocupacion", required = false) String ocupacion,
			@RequestParam(name = "emailUsuario", required = false) String emailUsuario, //Esta valor viene del input oculto de la hoja completarDatos, que a su vez viene del meotodo Login en ControladorPagina
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
			@RequestParam(name="idCliente") String idCliente, //este atributo es enviado en un input oculto de la pag misdatosCliente
			@RequestParam(name="ocupacion", required = false) String ocupacion, // Este y los demas atributos los puse como no requeridos para poder personalizar las excepciones
		    @RequestParam(name="email", required = false) String email,
			@RequestParam(name="domicilio", required = false) String domicilio,
			@RequestParam(name="sexo", required = false) String sexo,
			@RequestParam(name="telefono", required = false) String telefono,
			ModelMap model) throws MiExcepcion {
		
		System.out.println("ID cliente: " + idCliente);
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
			
	
	@PostMapping("actualizarContrasenaCliente")
	public String actualizarContrasenaCliente(
			@RequestParam(name = "emailCliente") String emailCliente, //Esta variable viene de un input oculto de la pag cambiarContrasenaCliente
			@RequestParam(name = "idCliente") String idCliente, //Esta variable viene de un input oculto de la pag de la pag cambiarContrasenaCliente
			@RequestParam(name = "oldPass") String oldPass, //A partir de estas viene del formulario
			@RequestParam(name = "newPass") String newPass,
			@RequestParam(name = "repeatNewPass") String repeatNewPass,
			ModelMap model) throws MiExcepcion {
		
		String error = null;
		try {
			servicioUsuario.modificarContrasena(idCliente,oldPass, newPass, repeatNewPass);
			return "/index";
		} catch (Exception e) {
			error = e.getMessage();
			return "redirect:/cambiarContrasenaCliente?email=" + emailCliente + "&error=" + error;
		}
		
	}
			
			
			
	
			
				
		
		
		
		
		
}
