package com.proyecto_integrador_3.Estetica.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Entidades.FechaHorarioDeshabilitado;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioFechaHorarioDeshabilitado;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioProfesional;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCliente;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioFechaHorarioDeshabilitado;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioHorario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioProfesional;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

@Controller
public class ControladorHorarios {

	@Autowired
	ServicioHorario servicioHorario;
	
	@Autowired
	ServicioProfesional servicioProfesional;
	
	@Autowired
	ServicioCliente servicioCliente;
	
	@Autowired
	ServicioUsuario servicioUsuario;
	
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
		
		if (fechaSeleccionada == null) {
			error = "Error al seleccionar la fecha";
			return "redirect:/turnosPorFecha?emailProfesional=" + emailProfesional + "&idProfesional=" + idProfesional + "&error=" + error;
		}
			
		
		List<String> fechasNoDisponibles = null;
		Optional<Profesional> buscarProfesional = servicioProfesional.buscarProfesional(idProfesional);
		if (buscarProfesional.isPresent()) {
			Profesional fechasDeshabilitadas = buscarProfesional.get();
			fechasNoDisponibles = fechasDeshabilitadas.getFechasDeshabilitadas(); //Obetenemos la lista de fechas deshabilitadas ylas guardamos en una lista fechasNoDisponibles
			
			//Comparamos la fecha seleccionada con las fechas guardadas
			for (String fechasGuardadas : fechasNoDisponibles) {
				if (fechaSeleccionada.equals(fechasGuardadas)) {
					error = "La fecha seleccionada ya se encuentra deshabilitada";
					return "redirect:/turnosPorFecha?emailProfesional=" + emailProfesional + "&idProfesional=" + idProfesional + "&error=" + error;
				}
			}
					
				//si pasa el filtro anterior, entonces guardamos la nueva fecha deshabilitada en la lista fechasNoDisponibles
				fechasNoDisponibles.add(fechaSeleccionada);
				//le agregamos la lista modificada al objeto profesional
				fechasDeshabilitadas.setFechasDeshabilitadas(fechasNoDisponibles);
				try {
					//guardamos la lista en la base de datos del objeto profesional
					repositorioProfesional.save(fechasDeshabilitadas);
				} catch (Exception e) {
					throw new MiExcepcion("Error al conectar con el servidor");
				}
				
				String exito ="La fecha fue deshabilitado correctamente";
				return "redirect:/turnosPorFecha?emailProfesional=" + emailProfesional + "&idProfesional=" + idProfesional + "&exito=" + exito + "&fechaTurno=" + fechaSeleccionada;
			
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
		List<String> horariosGuardadosDeshabilitados = null;
		Optional<FechaHorarioDeshabilitado> horariosDeshabilitadosPorFecha = servicioFechaHorarioDeshabilitado.horariosDeshabilitadosPorFecha(fechaSeleccionada);
		if (horariosDeshabilitadosPorFecha.isPresent()) {
			FechaHorarioDeshabilitado horariosGuardados = horariosDeshabilitadosPorFecha.get();
			horariosGuardadosDeshabilitados = horariosGuardados.getHorariosDeshabilitados();
			
			if (horariosGuardadosDeshabilitados.contains(horaSeleccionada)) {
	            error = "La hora seleccionada ya se encuentra deshabilitada";
	            return "redirect:/turnosPorFecha?emailProfesional=" + emailProfesional + "&idProfesional=" + idProfesional + "&error=" + error + "&fechaTurno=" + fechaSeleccionada;
	        } else {
	            horariosGuardadosDeshabilitados.add(horaSeleccionada);
	            horariosGuardados.setHorariosDeshabilitados(horariosGuardadosDeshabilitados);
	            repositorioFechaHorarioDeshabilitado.save(horariosGuardados);
	            exito = "La hora seleccionada ha sido deshabilitada correctamente";
	            return "redirect:/turnosPorFecha?emailProfesional=" + emailProfesional + "&idProfesional=" + idProfesional + "&exito=" + exito + "&fechaTurno=" + fechaSeleccionada;
	        }
			
		}else {
			List<String> horarioDeshabilitado = new ArrayList<>();
			horarioDeshabilitado.add(horaSeleccionada);
			FechaHorarioDeshabilitado nuevosHorariosDeshabilitadosPorFecha = new FechaHorarioDeshabilitado();
			nuevosHorariosDeshabilitadosPorFecha.setFecha(fechaSeleccionada);
			nuevosHorariosDeshabilitadosPorFecha.setHorariosDeshabilitados(horarioDeshabilitado);
			nuevosHorariosDeshabilitadosPorFecha.setProfesional(new Profesional(idProfesional));
			repositorioFechaHorarioDeshabilitado.save(nuevosHorariosDeshabilitadosPorFecha);
			exito = "La hora ha sido deshabilitada correctamente";
			return "redirect:/turnosPorFecha?emailProfesional=" + emailProfesional + "&idProfesional=" + idProfesional + "&exito=" + exito + "&fechaTurno=" + fechaSeleccionada;
		}
		
	}
	
}
