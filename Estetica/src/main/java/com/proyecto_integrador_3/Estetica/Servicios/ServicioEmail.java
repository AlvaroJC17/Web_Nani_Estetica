package com.proyecto_integrador_3.Estetica.Servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import com.proyecto_integrador_3.Estetica.Entidades.EmailUsuarios;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Turnos;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ServicioEmail {

	@Autowired
	RepositorioPersona repositorioPersona;

	 private  JavaMailSender emailSender;

	 private TemplateEngine templateEngine;
	 
	 @Autowired
	 public ServicioEmail(JavaMailSender emailSender, TemplateEngine templateEngine) {
		 this.emailSender = emailSender;
		 this.templateEngine = templateEngine;
	 }

	 //Esta es el metodo que se encarga de enviar los mial, recibe como parametro una
	 //entidad EmailUsuario y un String con el nombre del archivo html que quiero usar para el mensaje
	    public void enviarEmailUsuario(EmailUsuarios emailUsuarios) {
				
	    	try {
	        	MimeMessage mensaje = emailSender.createMimeMessage();
	        	MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
				helper.setTo(emailUsuarios.getDestinatario());
				helper.setSubject(emailUsuarios.getAsunto());
				Context context = new Context();
				context.setVariable("mensaje", emailUsuarios.getMensaje()); // se pueden crear varias setVariables para enviar varias variables al documento HTML
				String contenidoHTML = templateEngine.process("emailValidarCorreo", context);
				helper.setText(contenidoHTML, true); // el true es para que no envie texto plano sino que reconozca todo el formato HTML de la plantilla
				emailSender.send(mensaje);
				
			} catch (MessagingException e) {
				System.out.println("Error en los datos del mensaje");
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
	        
	    }
	    
	    public void enviarConfirmacionOCancelacionTurno(EmailUsuarios emailUsuarios, Turnos datosDelTurno, String plantillaHTML, Boolean multa) {
	    	//Buscamos el nombre del profesional asociado a ese turno para pasarlo al email de confirmacion
			Optional <Persona> datosProfesional = repositorioPersona.findById(datosDelTurno.getProfesional().getId());
	    	String nombreDelProfesional = null;
			if (datosProfesional.isPresent()) {
				Persona nombreProfesinal = datosProfesional.get();
				nombreDelProfesional = nombreProfesinal.getNombre();
			}
			
			//Buscamos el nombre del cliente asociado a ese turno para pasarlo al emial de confimación
			Optional <Persona> datosCliente = repositorioPersona.findById(datosDelTurno.getCliente().getId());
	    	String nombreDelCliente = null;
			if (datosCliente.isPresent()) {
				Persona nombreCliente = datosCliente.get();
				nombreDelCliente = nombreCliente.getNombre();
			}
			
			//Enviamos el email con todos los datos del turno y el nombre del Cliente
	    	try {
	        	MimeMessage mensaje = emailSender.createMimeMessage();
	        	MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
				helper.setTo(emailUsuarios.getDestinatario());
				helper.setSubject(emailUsuarios.getAsunto());
				Context context = new Context();
				context.setVariable("mensaje", emailUsuarios.getMensaje()); // se pueden crear varias setVariables para enviar varias variables al documento HTML
				context.setVariable("idTurno", datosDelTurno.getId());
				context.setVariable("horario", datosDelTurno.getHorario());
				context.setVariable("fecha", datosDelTurno.getFecha());
				context.setVariable("tratamiento", datosDelTurno.getTratamiento());
				if (multa) {
					context.setVariable("multa", datosDelTurno.getMulta());
					context.setVariable("costoMulta", datosDelTurno.getCostoMulta());
				}
				context.setVariable("profesional", nombreDelProfesional);
				context.setVariable("cliente", nombreDelCliente);
				String contenidoHTML = templateEngine.process(plantillaHTML, context);
				helper.setText(contenidoHTML, true); // el true es para que no envie texto plano sino que reconozca todo el formato HTML de la plantilla
				emailSender.send(mensaje);
									
			} catch (MessagingException e) {
				System.out.println("Error en los datos del mensaje o al enviar en email");
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
	        
	    }
	
}
