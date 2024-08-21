package com.proyecto_integrador_3.Estetica.Controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Turnos;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.EstadoDelTurno;
import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioHorario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioProfesional;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioTurnos;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

@Controller
public class ControladorTurnos {
	
	@Autowired
	ServicioHorario servicioHorario;
	
	@Autowired
	ServicioUsuario servicioUsuario;
	
	@Autowired
	ServicioTurnos servicioTurnos;
	
	@Autowired
	ServicioProfesional servicioProfesional;
	
	//Controlador para visualizar turnos por fecha, asi como deshabilitar fechas y horarios por profesional
	@GetMapping("/turnosPorFecha")
	public String turnosPorFecha(
			@RequestParam String idProfesional,
			@RequestParam String emailProfesional,
			@RequestParam (required = false) String fechaTurno,
			Model model) throws MiExcepcion {
		
		System.out.println("Fecha: " + fechaTurno);
		LocalDate fechaDelTurno = null;
		
		  if (fechaTurno != null && !fechaTurno.trim().isEmpty()) {
		        LocalDate fechaTurnoLocalDate = servicioHorario.pasarFechaStringToLocalDate(fechaTurno);
		        fechaDelTurno = fechaTurnoLocalDate;
		    } else {
		        fechaDelTurno = LocalDate.now();
		    }
		
		//Buscamos los horarios del profesional
		List<String> horariosLaboralesProfesional = null;
		Optional <Profesional> buscarHorariosProfesional = servicioProfesional.buscarProfesional(idProfesional);
		if (buscarHorariosProfesional.isPresent()) {
			Profesional horariosLaborales = buscarHorariosProfesional.get();
			horariosLaboralesProfesional = horariosLaborales.getHorariosLaborales();
		}
		
		//Buscamos los turnos por id profesional y fecha
		List <Turnos> turnosPorFecha = servicioTurnos.buscarTurnosPorProfesionalIdAndFecha(idProfesional, fechaDelTurno);
		
	    // Mapear los horarios laborales con los turnos correspondientes
	    Map<String, List<Turnos>> turnosPorHorarioLaboral = horariosLaboralesProfesional.stream()
	        .collect(Collectors.toMap(
	            horario -> horario,
	            horario -> turnosPorFecha.stream()
	                .filter(turno -> turno.getHorario().equals(horario))
	                .collect(Collectors.toList())
	        ));
		
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(emailProfesional);
		model.addAttribute("fechaSeleccionada", fechaDelTurno);
		model.addAttribute("turnosDisponiblesPorFecha", turnosPorFecha);
		model.addAttribute("turnosPorHorarioLaboral", turnosPorHorarioLaboral);
		model.addAttribute("horariosLaborales", horariosLaboralesProfesional);
		model.addAttribute("datosProfesional", datosProfesional);
		return"/pagina_profesional/turnosPorFecha";
		
	}
	
	//Controlador para visualizar el historico de turnos de un profesional
	@GetMapping("/historicoDeTurnos")
	public String historicoDeTurnos(
			@RequestParam String idProfesional,
			@RequestParam String emailProfesional,
			Model model) {
		
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(emailProfesional);
		List<Turnos> turnosDelProfesional = servicioTurnos.buscarTurnosPorProfesionalId(idProfesional);
		
		model.addAttribute("datosProfesional", datosProfesional);
		model.addAttribute("turnosDelProfesional", turnosDelProfesional);
		return"/pagina_profesional/historicoDeTurnos";
	}
	
	//Controlador para cancelar o confirmar asistencia a los turnos por parte de un profesional
	@PostMapping("/cancelarOrConfimarTurnoProfesional")
	public String cancelarOrConfimarTurnoProfesional(
			@RequestParam String emailProfesional,
			@RequestParam String fecha,
			@RequestParam String horario,
			@RequestParam String idTurno,
			@RequestParam String idProfesional,
			@RequestParam String accionDelBoton,
			Model model) throws MiExcepcion {
			
		switch (accionDelBoton) {
		case "confirmarTurno":
			
			//Sirve para pasar el estado de un turno de activo a inactivo, usando el id del turno como parametro
			servicioTurnos.actualizarEstadoDelTurno(idTurno, Rol.PROFESIONAL, EstadoDelTurno.ASISTIDO);
			
			return "redirect:/misturnosProfesional?email=" + emailProfesional + "&idProfesional=" + idProfesional;
			
		case "cancelarTurno":
			//Sirve para pasar el estado de un turno de activo a inactivo, usando el id del turno como parametro
			servicioTurnos.actualizarEstadoDelTurno(idTurno, Rol.PROFESIONAL, EstadoDelTurno.CANCELADO);
			
			//Como el turno es cancelado por un profesional, no regresamos el horario a la lista
			servicioHorario.agregarHorarioDisponible(fecha, horario, idProfesional, Rol.PROFESIONAL);
			
			return "redirect:/misturnosProfesional?email=" + emailProfesional + "&idProfesional=" + idProfesional;
		}
		return"/pagina_profesional/misturnosProfesional";
	}
				
	//Controlador para visualizar los turnos de un profesional filtrados por fecha
	@GetMapping("/misturnosProfesional")
		public String misturnosProfesional(
				@RequestParam String email,
				@RequestParam String idProfesional,
				Model model) {
		
			
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(email);
		
		//Obtenemos los turnos pedientes filtrados por fecha
		
		List<Turnos> turnosActivos = servicioTurnos.ObetenerTurnosPorEstadoAndActivoAndMultaAndIdProfesional(EstadoDelTurno.PENDIENTE, true, false, idProfesional);
		// Obtener la fecha actual y la fecha límite (7 días después)
		LocalDate fechaActual = LocalDate.now();
		LocalDate fechaLimite = fechaActual.plusDays(7);
		// Filtrar la lista para incluir solo los turnos dentro del rango de fechas
		List<Turnos> turnosFiltradosPorFechaPendientes = turnosActivos.stream()
		    .filter(turno -> !turno.getFecha().isBefore(fechaActual) && !turno.getFecha().isAfter(fechaLimite))
		    .collect(Collectors.toList());
		
		//Obetenemos los turnos asistidos filtrados por fecha
		
		List<Turnos> turnosAsistidos = servicioTurnos.ObetenerTurnosPorEstadoAndActivoAndMultaAndIdProfesional(EstadoDelTurno.ASISTIDO, false, false, idProfesional);
		// Obtener la fecha actual y la fecha límite (7 días antes)
		LocalDate fechaActualAsistidos = LocalDate.now();
		LocalDate fechaInicioAsistidos = fechaActualAsistidos.minusDays(7);
		// Filtrar la lista para incluir solo los turnos dentro del rango de fechas
		List<Turnos> turnosFiltradosPorFechaAsistidos = turnosAsistidos.stream()
		    .filter(turno -> !turno.getFecha().isBefore(fechaInicioAsistidos) && !turno.getFecha().isAfter(fechaActualAsistidos))
		    .collect(Collectors.toList());
		
		//Obtenemos los turnos cancelados sin multa por fecha
		
		List<Turnos> turnosCancelados = servicioTurnos.ObetenerTurnosPorEstadoAndActivoAndMultaAndIdProfesional(EstadoDelTurno.CANCELADO, false, false, idProfesional);
		// Obtener la fecha actual y la fecha límite (7 días antes)
				LocalDate fechaActualCancelados = LocalDate.now();
				LocalDate fechaInicioCancelados = fechaActualCancelados.minusDays(7);
				// Filtrar la lista para incluir solo los turnos dentro del rango de fechas
				List<Turnos> turnosFiltradosPorFechaCancelados = turnosCancelados.stream()
				    .filter(turno -> !turno.getFecha().isBefore(fechaInicioCancelados) && !turno.getFecha().isAfter(fechaActualCancelados))
				    .collect(Collectors.toList());
		
		//Obtenemos los turnos cancelados con multas y filtrados por fecha
				
		List<Turnos> turnosConMulta = servicioTurnos.ObetenerTurnosPorEstadoAndActivoAndMultaAndIdProfesional(EstadoDelTurno.CANCELADO, false, true, idProfesional);
		// Obtener la fecha actual y la fecha límite (7 días antes)
		LocalDate fechaActualMultas = LocalDate.now();
		LocalDate fechaInicioMultas = fechaActualMultas.minusDays(7);
		// Filtrar la lista para incluir solo los turnos dentro del rango de fechas
		List<Turnos> turnosFiltradosPorFechaMultas = turnosConMulta.stream()
		    .filter(turno -> !turno.getFecha().isBefore(fechaInicioMultas) && !turno.getFecha().isAfter(fechaActualMultas))
		    .collect(Collectors.toList());
		
		
		//Pasamos a string las fechas de inicio y final de los turnos proximos a atender
		String fechaActualProximosTurnos = servicioHorario.pasarFechasLocalDateToString(fechaActual);
		String fechaLimiteProximosTurnos = servicioHorario.pasarFechasLocalDateToString(fechaLimite);
		
		//Pasamos a string las fechas de inicio y final de los turnos anteriores a 7 dias
		String fechaActualTurnosAnteriores = servicioHorario.pasarFechasLocalDateToString(fechaActualAsistidos);
		String fechaLimiteTurnosAnteriores = servicioHorario.pasarFechasLocalDateToString(fechaInicioAsistidos);
		
		//Pasamos a string las fechas de inicio y final de los turnos cancelados
		
		model.addAttribute("datosProfesional", datosProfesional);
		model.addAttribute("turnosActivos", turnosFiltradosPorFechaPendientes);
        model.addAttribute("turnosAsistidos", turnosFiltradosPorFechaAsistidos);
        model.addAttribute("turnosConMulta", turnosFiltradosPorFechaMultas);
        model.addAttribute("turnosCancelados", turnosFiltradosPorFechaCancelados);
        model.addAttribute("fechaActualProximosTurnos", fechaActualProximosTurnos);
		model.addAttribute("fechaLimiteProximosTurnos", fechaLimiteProximosTurnos);
		model.addAttribute("fechaActualTurnosAnteriores", fechaActualTurnosAnteriores);
		model.addAttribute("fechaLimiteTurnosAnteriores", fechaLimiteTurnosAnteriores);
		return"/pagina_profesional/misturnosProfesional";
		}
	
		
	
	//Controlador para visualizar turnos del cliente
	@GetMapping("/misturnos")
	public String misturnos(
			@RequestParam String email,
			Model model) throws MiExcepcion {
		
		try {
		//cuando el usuario ingrese a turnos se verifica si algun turno tiene fecha anterior
		//a la actual y si eso es afirmativo, entonces pasa el turno a cencelado.
		servicioTurnos.actualizarTurnosAntiguos(email);
		
		//Elimina los turnos mas antiguo cuando la lista es mayor a 3 y no tienen multas
		//servicioTurnos.eliminarTurnoMasAntiguoNoActivo(email);
		
		//datos del cliente y los pasa a la vista, sirve para renderizar la vista
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		
		//busca todos los turnos disponinbles del usuario y los pasa a la vista
		//List<Turnos> turnosDisponibles = servicioTurnos.obtenerTurnosPorEmail(email);
		
		//Obtenemos los tunos asistidos, turnos activos, turnos cancelados y turnos con multa.
		List<Turnos> turnosAsistidos = servicioTurnos.obetnerTurnosPorEstadoAndActivoAndMultaAndEmailCliente(EstadoDelTurno.ASISTIDO, false, false, email);
		List<Turnos> turnosActivos = servicioTurnos.obetnerTurnosPorEstadoAndActivoAndMultaAndEmailCliente(EstadoDelTurno.PENDIENTE, true, false, email);
		List<Turnos> turnosCancelados = servicioTurnos.obetnerTurnosPorEstadoAndActivoAndMultaAndEmailCliente(EstadoDelTurno.CANCELADO, false, false, email);
		List<Turnos> turnosConMulta = servicioTurnos.obetnerTurnosPorEstadoAndActivoAndMultaAndEmailCliente(EstadoDelTurno.CANCELADO, false, true, email);
		
		
		//Filtramos las listas anteriores para que solo muestren los ultimos tres registros guardados
		List<Turnos> obtenerUltimosTresRegistrosActivos = servicioTurnos.obtenerUltimosTresRegistros(turnosActivos);
		List<Turnos> obtenerUltimosTresRegistrosAsistidos = servicioTurnos.obtenerUltimosTresRegistros(turnosAsistidos);
		List<Turnos> obtenerUltimosTresRegistrosConMulta = servicioTurnos.obtenerUltimosTresRegistros(turnosConMulta);
		List<Turnos> obtenerUltimosTresRegistrosCancelados = servicioTurnos.obtenerUltimosTresRegistros(turnosCancelados);
		
		
		 model.addAttribute("email", email);
		 model.addAttribute("datosCliente", datosCliente);
		 model.addAttribute("turnosActivos", obtenerUltimosTresRegistrosActivos);
         model.addAttribute("turnosAsistidos", obtenerUltimosTresRegistrosAsistidos);
         model.addAttribute("turnosConMulta", obtenerUltimosTresRegistrosConMulta);
         model.addAttribute("turnosCancelados", obtenerUltimosTresRegistrosCancelados);
         return "/pagina_cliente/misturnos";	
		
			
		} catch (Exception e) {
			List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
			List<Turnos> turnosDisponibles = servicioTurnos.obtenerTurnosPorEmail(email);
			String error = e.getMessage();
			model.addAttribute("error", error);
			model.addAttribute("showModalError", true);
			model.addAttribute("email", email);
			model.addAttribute("datosCliente", datosCliente);
			model.addAttribute("datosTurno", turnosDisponibles);
			return "/pagina_cliente/misturnos";	
		}
	}
		
			

	@PostMapping("/cancelarTurno")
	public String cancelarTurnos(
			@RequestParam String emailCliente,
			@RequestParam String idTurno,
			@RequestParam String fecha,
			@RequestParam String horario,
			@RequestParam String idProfesional,
			@RequestParam String idCliente,
			Model model
			) throws MiExcepcion {
		
		String fechaConHora = fecha + " " + horario;
		LocalDateTime fechaSeleccionadaLocalDateTime = servicioHorario.pasarFechaStringToLocalDateTime(fechaConHora);
		LocalDateTime fechaActual = LocalDateTime.now();
		
		try {
		//Si hay una diferencia menor a 24 horas entre la fecha seleccionada y la fecha actual
		//entra en esta condicion
		if (servicioHorario.turnoMenorA24Horas(fechaSeleccionadaLocalDateTime, fechaActual)) {
			//Este servicio se encarga de cancelar y multar el turno y al cliente
			servicioTurnos.multarTurnoAndClienteMenosDe24Horas(emailCliente, idTurno);
			
		}else {
			//Sirve para pasar el estado de un turno de activo a inactivo, usando el id del turno como parametro
			servicioTurnos.actualizarEstadoDelTurno(idTurno, Rol.CLIENTE, EstadoDelTurno.CANCELADO);
		}
		
		//Regresa a la lista de horarios la hora seleccionada en el turno cancelado
		servicioHorario.agregarHorarioDisponible(fecha, horario, idProfesional, Rol.CLIENTE);
		
		return "redirect:/misturnos?email=" + emailCliente;
		
		} catch (Exception e) {
			List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailCliente);
			List<Turnos> turnosDisponibles = servicioTurnos.obtenerTurnosPorEmail(emailCliente);
			String error = e.getMessage();
			model.addAttribute("emailCliente", emailCliente);
			model.addAttribute("datosCliente", datosCliente);
			model.addAttribute("datosTurno", turnosDisponibles);
			model.addAttribute("error", error);
			model.addAttribute("showModalError", true);
			return "/pagina_cliente/misturnos";
		}
	}
		
		
		
	
	 @GetMapping("/turnos")
	    public String turnos(Model model) {
	        List<Profesional> profesionales = servicioProfesional.listarTodos();
	        model.addAttribute("profesionales", profesionales);
	        return "/pagina_cliente/turnos";
	    }
	 
	 
	 @GetMapping("/formularioTurnos")
		public String formularioTurnos(
				@RequestParam String email,
				@RequestParam(required = false) String error,
				ModelMap modelo) {
				
			List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);		
			modelo.addAttribute("datosCliente", datosCliente);
			modelo.addAttribute("error", error);
			return "/pagina_cliente/formularioPreguntas";
		}
		
		
		
		/*Registra y valida los input del formulario de preguntas en la base de datos.
		 * Tambien guarda los valores ingresado por el usuario, en caso de que haya algun error
		 * en los datos ingresados, se vuelva a cargar la pagina con el mensaje de error y 
		 * con los datos previamente ingresado, para que el usuario no los tenga que volver a cargar*/
		@PostMapping("/guardarFormularioTurnos")
		public String guardarFormularioTurnos(
				@RequestParam(required = false) String fuma,
				@RequestParam(required = false) String drogas,
				@RequestParam(required = false) String alcohol,
				@RequestParam(required = false) String deportes,
				@RequestParam(required = false) String ejercicios,
				@RequestParam(required = false) String medicamentos,
				@RequestParam(required = false) String nombreMedicamento,
				@RequestParam String tratamiento,
				@RequestParam(required = false) String idCliente,
				@RequestParam(required = false) String email,
				@RequestParam(required = false) String embarazo,
				@RequestParam(required = false) String amamantando,
				@RequestParam(required = false) String ciclo_menstrual,
				@RequestParam(required = false) String alteracion_hormonal,
				@RequestParam(required = false) String vitaminas,
				@RequestParam(required = false) String corticoides,
				@RequestParam(required = false) String hormonas,
				@RequestParam(required = false) String metodo_anticonceptivo,
				@RequestParam(required = false) String sufre_enfermedad,
				@RequestParam(required = false) String cual_enfermedad,
				@RequestParam(required = false) String tiroides,
				@RequestParam(required = false) String paciente_oncologica,
				@RequestParam(required = false) String fractura_facial,
				@RequestParam(required = false) String cirugia_estetica,
				@RequestParam(required = false) String indique_cirugia_estetica,
				@RequestParam(required = false) String tiene_implantes,
				@RequestParam(required = false) String marca_pasos,
				@RequestParam(required = false) String horas_sueno,
				@RequestParam(required = false) String exposicion_sol,
				@RequestParam(required = false) String protector_solar,
				@RequestParam(required = false) String reaplica_protector,
				@RequestParam(required = false) String consumo_carbohidratos,
				@RequestParam(name="tratamientosFacialesAnteriores", required = false) String tratamientos_faciales_anteriores,
				@RequestParam(required = false) String resultados_tratamiento_anterior,
				@RequestParam(required = false) String cuidado_de_piel,
				@RequestParam(required = false) String motivo_consulta,
				@RequestParam(required = false) String notas_profesional,
				Model model) throws MiExcepcion{
		
			
			try {
				servicioTurnos.formularioTurnos(idCliente, email, fuma, drogas, alcohol, deportes, ejercicios,
						medicamentos, nombreMedicamento, embarazo, amamantando, ciclo_menstrual, alteracion_hormonal,
						vitaminas, corticoides, hormonas, metodo_anticonceptivo, sufre_enfermedad,
						cual_enfermedad, tiroides, paciente_oncologica, fractura_facial, cirugia_estetica, 
						indique_cirugia_estetica, tiene_implantes, marca_pasos, horas_sueno, exposicion_sol,
						protector_solar, reaplica_protector, consumo_carbohidratos, tratamientos_faciales_anteriores,
						resultados_tratamiento_anterior, cuidado_de_piel, motivo_consulta, notas_profesional);
				
				//Pasamos los datos  a la visa de 
				List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
				model.addAttribute("datosCliente", datosCliente);
				model.addAttribute("tratamiento", tratamiento);
				model.addAttribute("email", email);
				model.addAttribute("idCliente", idCliente);
				model.addAttribute("provincias", Provincias.values());
				
				String identificador;
				if (tratamiento.equals("facial")) {
					identificador = "tratamientoFacial";
					model.addAttribute("identificador", identificador);
					return "/pagina_cliente/reservaDeTurnoClienteFacial";
				}else if(tratamiento.equals("corporal")){
					identificador = "tratamientoCorporal";
					model.addAttribute("identificador", identificador);
					return "/pagina_cliente/reservaDeTurnoClienteCorporal";
				}else if(tratamiento.equals("estetico")){
					identificador = "tratamientoEstetico";
					model.addAttribute("identificador", identificador);
					return "/pagina_cliente/reservaDeTurnoClienteEstetico";
				}else {
					return "";
				}
					
				/*En esta excepcion cargamos los datos que ya el usuario ha ingresado arriba, en caso
				 * de que haya algun error de validacion, se vuelvan a carlos los datos seleccionados
				 * y no tenga de ingresarlos nuevamente*/
			} catch (MiExcepcion e) {
				List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
				String error = e.getMessage();
				model.addAttribute("datosCliente", datosCliente);
				model.addAttribute("tratamiento", tratamiento);
	            model.addAttribute("fuma", fuma);
	            model.addAttribute("drogas", drogas);
	            model.addAttribute("alcohol", alcohol);
	            model.addAttribute("deportes", deportes);
	            model.addAttribute("ejercicios", ejercicios);
	            model.addAttribute("medicamentos", medicamentos);
	            model.addAttribute("nombreMedicamento", nombreMedicamento);
	            model.addAttribute("embarazo", embarazo);
	            model.addAttribute("amamantando", amamantando);
	            model.addAttribute("ciclo_menstrual", ciclo_menstrual);
	            model.addAttribute("alteracion_hormonal", alteracion_hormonal);
	            model.addAttribute("vitaminas", vitaminas);
	            model.addAttribute("corticoides", corticoides);
	            model.addAttribute("hormonas", hormonas);
	            model.addAttribute("metodo_anticonceptivo", metodo_anticonceptivo);
	            model.addAttribute("sufre_enfermedad", sufre_enfermedad);
	            model.addAttribute("cual_enfermedad", cual_enfermedad);
	            model.addAttribute("tiroides", tiroides);
	            model.addAttribute("paciente_oncologica", paciente_oncologica);
	            model.addAttribute("fractura_facial", fractura_facial);
	            model.addAttribute("cirugia_estetica", cirugia_estetica);
	            model.addAttribute("indique_cirugia_estetica", indique_cirugia_estetica);
	            model.addAttribute("tiene_implantes", tiene_implantes);
	            model.addAttribute("marca_pasos", marca_pasos);
	            model.addAttribute("horas_sueno", horas_sueno);
	            model.addAttribute("exposicion_sol", exposicion_sol);
	            model.addAttribute("protector_solar", protector_solar);
	            model.addAttribute("reaplica_protector", reaplica_protector);
	            model.addAttribute("consumo_carbohidratos", consumo_carbohidratos);
	            model.addAttribute("tratamientosFacialesAnteriores", tratamientos_faciales_anteriores);
	            model.addAttribute("resultados_tratamiento_anterior", resultados_tratamiento_anterior);
	            model.addAttribute("cuidado_de_piel", cuidado_de_piel);
	            model.addAttribute("motivo_consulta", motivo_consulta);
	            model.addAttribute("error", error);
	            model.addAttribute("showModal", true);
				return "/pagina_cliente/formularioPreguntas";
			}
			
		}

}
	
	
	
	
	
	
	
