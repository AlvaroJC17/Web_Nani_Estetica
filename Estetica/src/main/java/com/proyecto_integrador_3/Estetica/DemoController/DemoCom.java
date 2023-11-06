package com.proyecto_integrador_3.Estetica.DemoController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@Controller
@RequestMapping("/home")
public class DemoCom {

	@GetMapping("/saludar")
	public String Saludo() {
		return "Hola mundo";
	}
	
	@GetMapping("/datos")
	public String hojas() {
		return "home";
	}
}
