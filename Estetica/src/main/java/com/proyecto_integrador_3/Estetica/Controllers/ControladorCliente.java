package com.proyecto_integrador_3.Estetica.Controllers;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.Enums.Horarios;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioTurnos;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCliente;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioHorario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioPersona;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioProfesional;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioTurnos;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;
import com.proyecto_integrador_3.Estetica.Entidades.Turnos;

import jakarta.persistence.EntityManager;

@Controller
//@RequestMapping(")
public class ControladorCliente {

	@Autowired
	public RepositorioCliente repositorioCliente;
	
	@Autowired
	public RepositorioPersona repositorioPersona;
	
	@Autowired
	public RepositorioUsuario repositorioUsuario;
	
	@Autowired
	public RepositorioTurnos repositorioTurnos;
	
	@Autowired
	public ServicioCliente servicioCliente;
	
	@Autowired
	public ServicioProfesional servicioProfesional;
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	@Autowired
	public ServicioPersona servicioPersona;
	
	@Autowired
	public ServicioHorario servicioHorario;
	
	@Autowired
	public ServicioTurnos servicioTurnos;
	
//	@GetMapping("tratamientosApagado")
//	public String tratamientosApagado(
//			@RequestParam(name="idCliente") String idCliente,
//			@RequestParam(name="email") String email,
//			@RequestParam(name="error", required = false) String error,
//			@RequestParam(name="identificador", required = false) String identificador,
//			Model modelo) {
//	
//		
//		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
//			
//				//estos son modelos en general
//				modelo.addAttribute("datosCliente", datosCliente);
//				modelo.addAttribute("idCliente", idCliente);
//				modelo.addAttribute("email", email);
//				modelo.addAttribute("provincias", Provincias.values());
//				modelo.addAttribute("identificador", identificador);
//				
//				if (error == null || error.isEmpty()) {
//				switch (identificador) {
//				case "tratamientoFacial":
//					return "/pagina_cliente/reservaDeTurnoClienteFacial";
//				case "tratamientoCorporal":
//					return "/pagina_cliente/reservaDeTurnoClienteCorporal";
//				case "tratamientoEstetico":
//					return "/pagina_cliente/reservaDeTurnoClienteEstetico";
//				}
//					
//			}else {
//				modelo.addAttribute("error", error); // estos son modelos que se suman a los generales si entra en el else
//				modelo.addAttribute("showModalError", true);
//				switch (identificador) {
//				case "tratamientoFacial":
//					return "/pagina_cliente/reservaDeTurnoClienteFacial";
//				case "tratamientoCorporal":
//					return "/pagina_cliente/reservaDeTurnoClienteCorporal";
//				case "tratamientoEstetico":	
//					return "/pagina_cliente/reservaDeTurnoClienteEstetico";
//				}				
//			}
//				return "";
//	}
	
	
	
	/*Este metodo deriva a la pagina de tratamientos con los valores de mail y id del cliente*/
	@GetMapping("/tratamientos")
	public String tratamientos(
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "idCliente", required = false) String idCliente,
			ModelMap model) {
		
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosCliente", datosCliente);
		return "/pagina_cliente/tratamientos";
	}
	
	@GetMapping("/buscarProfesionalPorProvincias")
	public String buscarProfesionalPorProvincias(
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "idCliente", required = false) String idCliente,
			@RequestParam(name="identificador", required = false) String identificador,
			@RequestParam(name="provincia") String provincia,
			ModelMap model) throws MiExcepcion {
		
	
		
		//pasamos la provincia a enum provincia
		Provincias nuevaProvincia = null;
		nuevaProvincia = Provincias.valueOf(provincia);
		
		
		//Buscamos todos los datos del cliente y lo pasamos al html, sirve para visualizar la pagina y pasar los datos del cliente entre controladores
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		Boolean isDisabled = false;
		model.addAttribute("datosCliente", datosCliente);
		model.addAttribute("identificador", identificador);
		model.addAttribute("provinciaString", provincia);
		model.addAttribute("isDisabled ", isDisabled);
		model.addAttribute("provinciaSeleccionada", nuevaProvincia.getDisplayName());
		model.addAttribute("provincias", Provincias.values()); // pasamos el array de enums al formulario para desplegar la lista de select
		
		
		String error = null;
		//validamos que se seleccione una provincia
		if (!servicioProfesional.validarProvincia(provincia)) {
			isDisabled = false;
			error = "Debe seleccionar un provincia";
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
		
		//Buscamos los profesionales por provincia
		List<Persona> profesionalesPorProvincia = servicioProfesional.buscarProfesionaByRolAndProvincis(Rol.PROFESIONAL, nuevaProvincia);
		
		//si no hay problema con las validaciones anteriores, buscamos los profesionales
		//y pasamos el nombre de la provincia y las agregamos a la vista.
		model.addAttribute("Profesionales", profesionalesPorProvincia); // pasamos el array de objetos tipo persona que tengan rol de profesional
		
		//Validamos que si la lista de profesionales esta vacia para esa provincia, mandamos un
		//error con un mensaje
		if (profesionalesPorProvincia.isEmpty()) {
			error = "No hay profesionales disponibles para la provincia seleccionada";
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
				
				servicioHorario.EliminarHorarioViejos(idProfesional, fechaSeleccionada);
				
				//Pasamos la fecha seleccionada a localDate para poder trabajar con los dias y dias de la semana
				LocalDate fechaSeleccionadaLocalDate = servicioCliente.pasarFechaStringToLocalDate(fechaSeleccionada);
				
				//Definimos la fecha maxima que se puede usar para sacar turnos
				//fechaMaxima toma la fecha actual del sistema y le da un meximo de dos meses hacia adelante
				LocalDate fechaMaxima = LocalDate.now().plusMonths(2);
				
		
				/*Verifica si existe la fecha asociada a ese id profesional en la base de datos.
				 * Si existe, nos devuelve la lista de horarios disponibles en la base de datos asociados a esa fecha
				 * Si no existe, nos devuelve una lista de horarios pre establecida */
				List<String> crearyObtenerHorariosDisponibles = servicioHorario.crearyObtenerHorariosDisponibles(fechaSeleccionada, idProfesional);
				
				//Guarda en la base de datos la lista de horarios disponibles pertenecientes a la fecha y el id del profesional que le pasamos
				servicioHorario.guardarHorariosDisponibles(fechaSeleccionada, crearyObtenerHorariosDisponibles, idProfesional);
				
				//Obetenemos los horarios del profesional por fecha y id del profesional, puede devolver una lista con horarios disponibles
				//o puede devolver una lista vacia si ya al profesional le solicitaron todos los horarios de esa fecha
				List <String> ObtenerHorariosDisponibles = servicioHorario.obtenerHorariosDisponiblesPorProfesionalYFecha(idProfesional, fechaSeleccionada);
				
		
				//pasamos la provincia a enum provincia
				Provincias nuevaProvincia = null;
				nuevaProvincia = Provincias.valueOf(provinciaString);
				
				//Buscamos los profesionales por provincia
				List<Persona> profesionalesPorProvincia = servicioProfesional.buscarProfesionaByRolAndProvincis(Rol.PROFESIONAL, nuevaProvincia);
		
				//Buscamos los datos del profesional
				List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailCliente);
				
				//Pasamos todos los datos necesarios a la vista
				Boolean isDisabled = null;
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
				if (servicioCliente.fechaYaPaso(fechaSeleccionadaLocalDate)) {
					String error = "No se puede seleccionar un fecha pasada";
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
				
	   //Metodo que recibe una fecha tipo LocalDate y devuelve true si es fin de semana
				if (servicioCliente.esFinDeSemana(fechaSeleccionadaLocalDate)) {
					String error = "No trabajamos los fines de semanas";
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
				
				//Definimos una fecha maxima de dos meses a partir de la fecha actual
				//Con esto limitamos al usuario a que no pueda solicitar turnos mas alla de dos meses en adelante de la fecha actual
				if (fechaSeleccionadaLocalDate.isAfter(fechaMaxima)) {
					String error = "Lo sentimos, todavia no hay turnos habilitados para esta fecha";
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
					String error = "No hay turnos disponibles para la fecha seleccionada";
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
					String error = "Solo se permite tener un maximo de tres turnos activos,"
							+ "si necesita modificar un turno o seleccionar uno nuevo puede"
							+ "ir al apartado de mis turnos y cancerlar alguno de los turnos activos";
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
				modelo.addAttribute("isDisabled",isDisabled);
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
				servicioCliente.guardarTurno(idCliente, nombreDelProfesional, fechaSeleccionada,
						provinciaString, idProfesional, facial, espalda, pulido, dermaplaning,
						exfoliacion, lifting, perfilado, laminado, hydralips, microneedling, horario,
						emailCliente);
				
				 // Primero buscamos y obtenemos una lista de los horarios disponibles por fecha e idprofesional
				//luego removemos de la lista obtenida el horario seleccionado por el usuario.
				List <String> horariosDisponibles = servicioHorario.obtenerHorariosDisponiblesPorProfesionalYFecha(idProfesional, fechaSeleccionada);
				horariosDisponibles.remove(horario);
                
                //Actualizamos y guardamos en la base de datos la lista con el horario removido por el usuario.
				servicioHorario.actualizarHorariosDisponibles(fechaSeleccionada, horariosDisponibles, idProfesional);
                
                //cuando el usuario selecciona un nuevo turno, eliminar el turno mas antiguo
                //que tenga estado inactivo
                servicioTurnos.eliminarTurnoMasAntiguoNoActivo(emailCliente);
                System.out.println("Turno mas antiguo eliminado con exito");
                
                //Obtenemos una lista de turnos del usuario a traves de su id
                List<Turnos> tunosDisponibles = servicioTurnos.buscarTurnoPorClienteId(idCliente);
                
                //Se dispara el modal de exito y se redirecciona a la pagina de mis turnos
                String exito = "Gracias por seleccionar un turno";
                model.addAttribute("exito", exito);
                model.addAttribute("datosTurno", tunosDisponibles);
                model.addAttribute("emailCliente", emailCliente);
                model.addAttribute("datosCliente", datosCliente);
                model.addAttribute("showModalExito", true);
                return "/pagina_cliente/misturnos";
              
			} catch (MiExcepcion e) {
				// si hay algun error en alguna validacion, se dispara este catch y se pasan de vuelta todos estos
				//valores para que recargue la misma pagina del formulario con un mensaje de error y con todos los
				//array de provincia, profesional y horarios
				
				String error = e.getMessage();
				Boolean isDisabled = true;
				model.addAttribute("error", error);
				model.addAttribute("datosCliente", datosCliente);
				model.addAttribute("identificador", identificador);
				model.addAttribute("provincias", Provincias.values());
				model.addAttribute("emailCliente", emailCliente);
				model.addAttribute("fechaSeleccionada", fechaSeleccionada);
				model.addAttribute("idCliente", idCliente);
				model.addAttribute("idProfesional", idProfesional);
				model.addAttribute("nombreDelProfesional", nombreDelProfesional);
				model.addAttribute("isDisabled", isDisabled);
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
			System.out.println("Valor de isDisabled: " +  isDisabled);
			model.addAttribute("identificador", identificador);
			model.addAttribute("isDisabled", isDisabled);  // Mandamos el valor del booleano a la vista para determinar si mostrados el input de la fecha habilitado o no
			return "/pagina_cliente/reservaDeTurnoClienteFacial";
			
		}else if(usoDeFormulario && tratamiento.equals("corporal")) {
			identificador = "tratamientoCorporal";
			isDisabled = false;
			model.addAttribute("identificador", identificador);
			model.addAttribute("isDisabled", isDisabled);
			return "/pagina_cliente/reservaDeTurnoClienteCorporal";
			
			//Si el boolean usoDeFormulario es false, entonces dirige al usuario a llenar el formulario de preguntas
		}else if(tratamiento.equals("estetico")){
			identificador = "tratamientoEstetico";
			isDisabled = false;
			model.addAttribute("identificador", identificador);
			model.addAttribute("isDisabled", isDisabled);
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
			ocupacionAnterior = datosAnteriorCliente.getOcupacion();
			domicilioAnterior = datosAnteriorCliente.getDomicilio();
			sexoAnterior = datosAnteriorCliente.getSexo();
			nuevoSexo = sexoAnterior.toString();
			telefonoAnterior = datosAnteriorCliente.getTelefono();
		}
		
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email); //Buscamos todos los datos pertenecientes al cliente despues de haber sido actualizados en la base de datos y los mostramos en el campo correspondiente
		//Teniendo el valos de los datos guardados y los que envian al presionar guardar en el formualario podemos comparar si se hiz alguna modificaicon
				//de los datos, si presiona guardar y no se modifico nada, recargar la misma pagina y no muestra ningun mensaje
		if (ocupacionAnterior.equals(ocupacion) && emailAnterior.equals(email) && domicilioAnterior.equals(domicilio) && nuevoSexo.equals(sexo) && telefonoAnterior.equals(telefono)) {
			model.addAttribute("emial", email);
			model.addAttribute("datosCliente",datosCliente);
			return "/pagina_cliente/misdatosCliente";
			
		}
		
		try {
			//este metodo verifica valida el mail y los nuevos datos del cliente y los remplaza en la base de datos
			servicioCliente.modificarCliente(idCliente, ocupacion, email, emailAnterior, domicilio, sexo, telefono );
			List <Usuario> datosClienteActualizados = servicioUsuario.buscarPorEmail(email);
			String exito = "Datos actualizados correctamente";
			model.addAttribute("datosCliente",datosClienteActualizados);
			model.addAttribute("exito",exito);
			model.addAttribute("showModalExito", true);
			return "/pagina_cliente/misdatosCliente";
			
		} catch (MiExcepcion e) {
			String error = e.getMessage(); // en la exepcion e.getmessage obtenenos el valor de la exepcion personalizada que se de y la enviamos al controlador de misdatosCliente para ser monstrada en pantalla
			List <Usuario> datosClienteAnterior = servicioUsuario.buscarPorEmail(emailAnterior); //Buscamos los datos ateriores a la excepcion y los mostramos en caso de que haya un error por parte del usuario
			model.addAttribute("datosCliente",datosClienteAnterior);
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
