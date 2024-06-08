package com.proyecto_integrador_3.Estetica.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Entidades.Turnos;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioHorario;
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

	@PostMapping("/cancelarTurno")
	public String cancelarTurnos(
			@RequestParam(name="email") String email,
			@RequestParam(name="idTurno") String idTurno,
			@RequestParam(name="fecha") String fecha,
			@RequestParam(name="horario") String horario,
			Model model
			) {
		
		//Delvuelve a la lista de horarios de determinada fecha el horario seleccionado por el usuario
		servicioHorario.agregarHorarioDisponible(fecha, horario);
		
		//Sirve para pasar el estado de un turno de activo a inactivo, usando el id del turno como parametro
		servicioTurnos.actualizarEstadoDelTurno(idTurno);
		
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		
		//Obtenemos una lista de todos los turnos que tiene el usuario con ese email
		List<Turnos> tunosDisponibles = servicioTurnos.obtenerTurnosPorEmail(email);
		
		model.addAttribute("email", email);
		model.addAttribute("datosCliente", datosCliente);
		model.addAttribute("datosTurno", tunosDisponibles);
		return "/pagina_cliente/misturnos";	
	}
}
	
	
	
	
	
	
	
