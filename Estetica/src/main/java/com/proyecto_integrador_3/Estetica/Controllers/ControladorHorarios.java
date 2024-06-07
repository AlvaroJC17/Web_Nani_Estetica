package com.proyecto_integrador_3.Estetica.Controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Servicios.ServicioHorario;

@Controller
public class ControladorHorarios {

	@Autowired
	ServicioHorario servicioHorario;
	
	@PostMapping("/guardarHorariosDisponibles")
    public String guardarHorariosDisponibles(@RequestParam("fecha") String fecha) {
        List<String> horarios = Arrays.asList("10:00", "11:00", "12:00", "13:00", "14:00");
        servicioHorario.guardarHorariosDisponibles(fecha, horarios);
        System.out.println("LLego la fecha: " + fecha);
        return "redirect:/reservaDeTurnoClienteFacial?fecha=" + fecha;
    }
}
