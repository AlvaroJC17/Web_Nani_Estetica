package com.proyecto_integrador_3.Estetica.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto_integrador_3.Estetica.Entidades.EmailUsuarios;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioEmail;

@RestController
public class ControladorEmail {

   @Autowired
   ServicioEmail servicioEmail;
   
   @PostMapping("/EnviarEmail")
   private ResponseEntity<String> enviarEmail(
		   @RequestBody EmailUsuarios emailUsuario){
	   
	   servicioEmail.enviarEmailUsuario(emailUsuario);
	   return new ResponseEntity<>("correo enviado exitosamente", HttpStatus.OK);
   }
	
	
}
