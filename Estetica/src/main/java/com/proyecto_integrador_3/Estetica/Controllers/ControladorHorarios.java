package com.proyecto_integrador_3.Estetica.Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Entidades.EmailUsuarios;
import com.proyecto_integrador_3.Estetica.Entidades.FechaHorarioDeshabilitado;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Turnos;
import com.proyecto_integrador_3.Estetica.Enums.EstadoDelTurno;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioFechaHorarioDeshabilitado;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioProfesional;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCliente;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioEmail;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioFechaHorarioDeshabilitado;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioHorario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioProfesional;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioTurnos;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

@Controller
public class ControladorHorarios {

	@Autowired
	ServicioHorario servicioHorario;
	
	@Autowired
	ServicioEmail servicioEmail;
	
	@Autowired
	ServicioProfesional servicioProfesional;
	
	@Autowired
	ServicioCliente servicioCliente;
	
	@Autowired
	ServicioUsuario servicioUsuario;
	
	@Autowired
	ServicioTurnos servicioTurnos;
	
	@Autowired
	ServicioFechaHorarioDeshabilitado servicioFechaHorarioDeshabilitado;
	
	@Autowired
	RepositorioProfesional repositorioProfesional;
	
	@Autowired
	RepositorioFechaHorarioDeshabilitado repositorioFechaHorarioDeshabilitado;
	

	@PostMapping("/fechasDeshabilitadas")
	public String fechasDeshabilitadas(
			@RequestParam String fechaSeleccionada,
			@RequestParam String emailProfesional,
			@RequestParam String idProfesional,
			Model model) throws MiExcepcion {
		
		String error;
		
		if (fechaSeleccionada == null || fechaSeleccionada.isEmpty()) {
			error = "Error al deshabilitar la fecha";
			return "redirect:/turnosPorFecha?emailProfesional=" + emailProfesional + "&idProfesional=" + idProfesional + "&error=" + error;
			
		}
		
		//cambiamos la fecha de formato string dd/MM/yyyy a LocalDate con formato yyyy-MM-dd
		String cambiarFormatoFecha = servicioHorario.cambiarFormatoFechaStringToLocalDate(fechaSeleccionada);
		LocalDate fechaLocalDate = servicioHorario.pasarFechaStringToLocalDateOtroFormato(cambiarFormatoFecha);
		
		//Buscamos los turnos por id profesional y fecha
		List <Turnos> turnosPorFecha = servicioTurnos.buscarTurnosPorProfesionalIdAndFecha(idProfesional, fechaLocalDate);
		
		//Si la lista no esta vacía, quiere decir que habia turnos para esta fecha, los buscamos y los cancelamos
		if (!turnosPorFecha.isEmpty()) {
				
			for (Turnos turnos : turnosPorFecha) {
				
				//Buscamos solo los turnos que esten activos
				if (turnos.getActivo()) {
					
				//Sirve para pasar el estado de un turno de activo a inactivo, usando el id del turno como parametro
				Turnos turnosCancelados = servicioTurnos.actualizarEstadoDelTurno(turnos.getId(), Rol.PROFESIONAL, EstadoDelTurno.CANCELADO);
				
				//Como el turno es cancelado por un profesional, el metodo tambien sirve para borra los horarios pasados por un rol profesional
				//Es decir el horario que se selecciono para este turno no vuelve a estar habilitado para el usuario
				servicioHorario.agregarOrBorraHorarioDisponible(turnos.getFecha().toString(), turnos.getHorario(), idProfesional, Rol.PROFESIONAL);
				
				//Despues de generar todo el proceso del turno se envia un mail de cancelación
				//al cliente, para esto debemos instancias un objeto EmailUsuario y pasarle toda
				//la info necesario que debe llevar el correo
				EmailUsuarios cancelacionPorEmailTurno = new EmailUsuarios();
				cancelacionPorEmailTurno.setAsunto("Nani estética - CANCELACIÓN DE TURNO");
				cancelacionPorEmailTurno.setDestinatario("alvarocortesia@gmail.com"); // Aqui va turnos.getCliente().getEmail;
				
				 //asignamos a la variable el nombre de la plantilla que vamos a utilizar
				 String plantillaCancelacionHTML = "emailCancelacionDeTurnoPorProfesional";
				 
				 //Este boolean sirve para indicar dentro del metodo si se agregar a la plantilla los valor de multa y costo de multa a la plantilla
				 Boolean multa = false;
				 
				 try {
					 //Llamamos al servicio para enviar el email
					 servicioEmail.enviarConfirmacionOCancelacionTurno(cancelacionPorEmailTurno, turnosCancelados, plantillaCancelacionHTML, multa);
				 } catch (Exception e) {
					 throw new MiExcepcion("Error al enviar el email de cancelacion de turno por parte de un profesional, cuando se cancela una fecha");
				 }
			}
			}
		}
		
		//Buscamos las lista de fechas que el profesional deshabilito y se guardaron en la base de datos
		List<String> fechasNoDisponibles = null;
		Optional<Profesional> buscarProfesional = servicioProfesional.buscarProfesional(idProfesional);
		if (buscarProfesional.isPresent()) {
			Profesional fechasDeshabilitadas = buscarProfesional.get();
			fechasNoDisponibles = fechasDeshabilitadas.getFechasDeshabilitadas(); //Obetenemos la lista de fechas deshabilitadas ylas guardamos en una lista fechasNoDisponibles
			
			//Comparamos la fecha seleccionada con las fechas guardadas para ver si existe alguna coincidencia
			for (String fechasGuardadas : fechasNoDisponibles) {
				if (cambiarFormatoFecha.equals(fechasGuardadas)) {
					error = "La fecha seleccionada ya se encuentra deshabilitada";
					return "redirect:/turnosPorFecha?emailProfesional=" + emailProfesional + "&idProfesional=" + idProfesional + "&error=" + error;
				}
			}
					
				//si pasa el filtro anterior, entonces guardamos la nueva fecha deshabilitada en la lista fechasNoDisponibles
				fechasNoDisponibles.add(cambiarFormatoFecha);
				//le agregamos la lista modificada al objeto profesional
				fechasDeshabilitadas.setFechasDeshabilitadas(fechasNoDisponibles);
				try {
					//guardamos la lista  actualizada de fechas deshabilitadas en la base de datos del objeto profesional
					repositorioProfesional.save(fechasDeshabilitadas);
				} catch (Exception e) {
					throw new MiExcepcion("Error al conectar con el servidor");
				}
				
				String exito ="La fecha fue deshabilitado correctamente";
				return "redirect:/turnosPorFecha?emailProfesional=" + emailProfesional + "&idProfesional=" + idProfesional + "&exito=" + exito + "&fechaTurno=" + cambiarFormatoFecha;
			
		}
	return "";
	}
	
	@PostMapping("/horasDeshabilitadas")
	public String horasDeshabilitadas(
			@RequestParam String emailProfesional,
			@RequestParam String idProfesional,
			@RequestParam String horaSeleccionada,
			@RequestParam String fechaSeleccionada,
			Model model) throws MiExcepcion {
		
		String error = null;
		String exito = null;
		
		if (fechaSeleccionada == null || fechaSeleccionada.isEmpty()) {
			error = "Error al deshabilitar la fecha";
			return "redirect:/turnosPorFecha?emailProfesional=" + emailProfesional + "&idProfesional=" + idProfesional + "&error=" + error;
			
		}
		
		if (horaSeleccionada == null || horaSeleccionada.isEmpty()) {
			error = "Error al deshabilitar la hora";
			return "redirect:/turnosPorFecha?emailProfesional=" + emailProfesional + "&idProfesional=" + idProfesional + "&error=" + error;
		}
		
		//cambiamos la fecha de formato string dd/MM/yyyy a LocalDate con formato yyyy-MM-dd
		String cambiarFormatoFecha = servicioHorario.cambiarFormatoFechaStringToLocalDate(fechaSeleccionada);
		LocalDate fechaLocalDate = servicioHorario.pasarFechaStringToLocalDateOtroFormato(cambiarFormatoFecha);
		
		//Buscamos los turnos por id profesional y fecha
		List <Turnos> turnosPorFecha = servicioTurnos.buscarTurnosPorProfesionalIdAndFecha(idProfesional, fechaLocalDate);
		
		//Despues de crear la lista de turnos por fecha, creamos una lista de turnos que contengan la hora seleccionada
		//si la hora seleccionada coincide con la hora de algun turno, entonces guardamos ese turno es la lista turnosPorHora
		List<Turnos> turnosPorHora = new ArrayList<>();
		for (Turnos turnos : turnosPorFecha) {
			if (turnos.getHorario().equals(horaSeleccionada)) {
				turnosPorHora.add(turnos);
			}
		}
		
		//Si la lista turnosPorHora no esta vacía, quiere decir que habia turnos para esta fecha, los buscamos y los cancelamos
		if (!turnosPorHora.isEmpty()) {
			
			for (Turnos turnos : turnosPorFecha) {
				
				//Buscamos solo los turnos que esten activos
				if (turnos.getActivo()) {
					
					//Sirve para pasar el estado de un turno de activo a inactivo, usando el id del turno como parametro
					Turnos turnosCancelados = servicioTurnos.actualizarEstadoDelTurno(turnos.getId(), Rol.PROFESIONAL, EstadoDelTurno.CANCELADO);
					
					//Como el turno es cancelado por un profesional, el metodo tambien sirve para borra los horarios pasados por un rol profesional
					//Es decir el horario que se selecciono para este turno no vuelve a estar habilitado para el usuario
					servicioHorario.agregarOrBorraHorarioDisponible(turnos.getFecha().toString(), turnos.getHorario(), idProfesional, Rol.PROFESIONAL);
					
					//Despues de generar todo el proceso del turno se envia un mail de cancelación
					//al cliente, para esto debemos instancias un objeto EmailUsuario y pasarle toda
					//la info necesario que debe llevar el correo
					EmailUsuarios cancelacionPorEmailTurno = new EmailUsuarios();
					cancelacionPorEmailTurno.setAsunto("Nani estética - CANCELACIÓN DE TURNO");
					cancelacionPorEmailTurno.setDestinatario("alvarocortesia@gmail.com"); // Aqui va turnos.getCliente().getEmail;
					
					//asignamos a la variable el nombre de la plantilla que vamos a utilizar
					String plantillaCancelacionHTML = "emailCancelacionDeTurnoPorProfesional";
					
					//Este boolean sirve para indicar dentro del metodo si se agregar a la plantilla los valor de multa y costo de multa a la plantilla
					Boolean multa = false;
					
					try {
						//Llamamos al servicio para enviar el email
						servicioEmail.enviarConfirmacionOCancelacionTurno(cancelacionPorEmailTurno, turnosCancelados, plantillaCancelacionHTML, multa);
					} catch (Exception e) {
						throw new MiExcepcion("Error al enviar el email de cancelacion de turno por parte de un profesional, cuando se cancela una fecha");
					}
				}
			}
		}else {
			System.out.println("No habia turnos para cancelar asociados a esa hora");
		}
		
		
		List<String> horariosGuardadosDeshabilitados = null;
		Optional<FechaHorarioDeshabilitado> horariosDeshabilitadosPorFecha = servicioFechaHorarioDeshabilitado.horariosDeshabilitadosPorFecha(cambiarFormatoFecha);
		if (horariosDeshabilitadosPorFecha.isPresent()) {
			FechaHorarioDeshabilitado horariosGuardados = horariosDeshabilitadosPorFecha.get();
			horariosGuardadosDeshabilitados = horariosGuardados.getHorariosDeshabilitados();
			
			if (horariosGuardadosDeshabilitados.contains(horaSeleccionada)) {
	            error = "La hora seleccionada ya se encuentra deshabilitada";
	            return "redirect:/turnosPorFecha?emailProfesional=" + emailProfesional + "&idProfesional=" + idProfesional + "&error=" + error + "&fechaTurno=" + cambiarFormatoFecha;
	        } else {
	            horariosGuardadosDeshabilitados.add(horaSeleccionada);
	            horariosGuardados.setHorariosDeshabilitados(horariosGuardadosDeshabilitados);
	            repositorioFechaHorarioDeshabilitado.save(horariosGuardados);
	            exito = "La hora seleccionada ha sido deshabilitada correctamente";
	            return "redirect:/turnosPorFecha?emailProfesional=" + emailProfesional + "&idProfesional=" + idProfesional + "&exito=" + exito + "&fechaTurno=" + cambiarFormatoFecha;
	        }
			
		}else {
			List<String> horarioDeshabilitado = new ArrayList<>();
			horarioDeshabilitado.add(horaSeleccionada);
			FechaHorarioDeshabilitado nuevosHorariosDeshabilitadosPorFecha = new FechaHorarioDeshabilitado();
			nuevosHorariosDeshabilitadosPorFecha.setFecha(cambiarFormatoFecha);
			nuevosHorariosDeshabilitadosPorFecha.setHorariosDeshabilitados(horarioDeshabilitado);
			nuevosHorariosDeshabilitadosPorFecha.setProfesional(new Profesional(idProfesional));
			repositorioFechaHorarioDeshabilitado.save(nuevosHorariosDeshabilitadosPorFecha);
			exito = "La hora ha sido deshabilitada correctamente";
			return "redirect:/turnosPorFecha?emailProfesional=" + emailProfesional + "&idProfesional=" + idProfesional + "&exito=" + exito + "&fechaTurno=" + cambiarFormatoFecha;
		}
		
	}
	
}
