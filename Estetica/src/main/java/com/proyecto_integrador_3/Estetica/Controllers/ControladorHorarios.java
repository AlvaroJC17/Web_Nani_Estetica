package com.proyecto_integrador_3.Estetica.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioProfesional;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCliente;
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
	RepositorioProfesional repositorioProfesional;
	

	@PostMapping("/fechasDeshabilitadas")
	public String fechasDeshabilitadas(
			@RequestParam String fechaSeleccionada,
			@RequestParam String emailProfesional,
			@RequestParam String idProfesional,
			Model model) throws MiExcepcion {
		
		if (fechaSeleccionada == null) {
			//crear forma de redireccionar un mensaje de error
			return "redirect:/turnosPorFecha?emailProfesional=" + emailProfesional + "&idProfesional=" + idProfesional;
		}
			
		
		List<String> fechasNoDisponibles = null;
		Optional<Profesional> buscarProfesional = servicioProfesional.buscarProfesional(idProfesional);
		if (buscarProfesional.isPresent()) {
			Profesional fechasDeshabilitadas = buscarProfesional.get();
			fechasNoDisponibles = fechasDeshabilitadas.getFechasDeshabilitadas(); //Obetenemos la lista de fechas deshabilitadas ylas guardamos en una lista fechasNoDisponibles
			
			//Comparamos la fecha seleccionada con las fechas guardadas
			for (String fechasGuardadas : fechasNoDisponibles) {
				if (fechaSeleccionada.equals(fechasGuardadas)) {
					//Crear forma de redireccionar un mensaje de error
					return "redirect:/turnosPorFecha?emailProfesional=" + emailProfesional + "&idProfesional=" + idProfesional;
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
				
				//redireccionar con mensaje de exito
				return "redirect:/turnosPorFecha?emailProfesional=" + emailProfesional + "&idProfesional=" + idProfesional;
			
		}
	return "";
	}
}
