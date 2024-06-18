package com.proyecto_integrador_3.Estetica.Controllers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCliente;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioHorario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioProfesional;

@Controller
public class ControladorHorarios {

	@Autowired
	ServicioHorario servicioHorario;
	
	@Autowired
	ServicioProfesional servicioProfesional;
	
	@Autowired
	ServicioCliente servicioCliente;
	
	@PostMapping("/guardarHorariosDisponibles")
    public String guardarHorariosDisponibles(@RequestParam("fecha") String fecha) {
		LocalDate fechaFormateada = null;
		try {
			fechaFormateada = servicioCliente.pasarFechaStringToLocalDate(fecha);
		} catch (MiExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        List<String> horarios = Arrays.asList("10:00", "11:00", "12:00", "13:00", "14:00");
      //  servicioHorario.guardarHorariosDisponibles(fecha, horarios);
        return "redirect:/reservaDeTurnoClienteFacial?fecha=" + fecha;
    }
	
	@GetMapping("/horarios")
    public String mostrarHorarios(@RequestParam("profesionalId") String profesionalId, 
                                  @RequestParam("fecha") String fecha,
                                  Model model) {
        Profesional profesional = servicioProfesional.buscarProfesionalPorId(profesionalId);
   //     List<String> horariosDisponibles = servicioHorario.obtenerHorariosDisponiblesPorProfesionalYFecha(profesional, fecha);
    //    model.addAttribute("horarios", horariosDisponibles);
        return "horarios";
    }
}
