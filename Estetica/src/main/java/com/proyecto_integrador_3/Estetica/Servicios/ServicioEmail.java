package com.proyecto_integrador_3.Estetica.Servicios;

import java.net.SocketException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import com.proyecto_integrador_3.Estetica.Entidades.EmailUsuarios;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Turnos;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
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
	    public void enviarEmailUsuario(EmailUsuarios emailUsuarios) throws MiExcepcion {
				
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
				
			}catch (MessagingException e) {
				System.err.println("MessagingException class: " + e.getClass().getName());
	            System.err.println("MessagingException message: " + e.getMessage());
	            e.printStackTrace();

	            // Verificar si la causa de la excepción es una SocketException
	            Throwable cause = e.getCause();
	            while (cause != null) {
	                if (cause instanceof SocketException) {
	                    throw new MiExcepcion("No se pudo enviar el email debido a un problema de conexión: ");
	                }
	                cause = cause.getCause();
	            }
	            // Si no es una SocketException, lanzar una excepción genérica
	            throw new MiExcepcion("No se pudo enviar el email");
	        } catch (Exception e) {
	            System.err.println("Exception class: " + e.getClass().getName());
	            System.err.println("Exception message: " + e.getMessage());
	            e.printStackTrace();
	            throw new MiExcepcion("No se pudo enviar el email");
	        }
	    }
	        
	        
	    
	    public void enviarConfirmacionOCancelacionTurno(EmailUsuarios emailUsuarios, Turnos datosDelTurno, String plantillaHTML, Boolean multa) throws MiExcepcion {
	    	//Buscamos el nombre del profesional asociado a ese turno para pasarlo al email de confirmacion
			Optional <Persona> datosProfesional = repositorioPersona.findById(datosDelTurno.getProfesional().getId());
	    	String nombreDelProfesional = null;
	    	String apellidoDelProfesional = null;
	    	String nombreCompletoDelProfesional = null;
			if (datosProfesional.isPresent()) {
				Persona nombreProfesinal = datosProfesional.get();
				nombreDelProfesional = nombreProfesinal.getNombre();
				apellidoDelProfesional = nombreProfesinal.getApellido();
				nombreCompletoDelProfesional = apellidoDelProfesional + " " + nombreDelProfesional;
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
				//Si viene un valor true en la variable quiere decir que el mensaje se envia con una multa
				if (multa) {
					context.setVariable("multa", datosDelTurno.getMulta());
					context.setVariable("costoMulta", datosDelTurno.getCostoMulta());
				}
				context.setVariable("profesional", nombreCompletoDelProfesional);
				context.setVariable("cliente", nombreDelCliente);
				String contenidoHTML = templateEngine.process(plantillaHTML, context);
				helper.setText(contenidoHTML, true); // el true es para que no envie texto plano sino que reconozca todo el formato HTML de la plantilla
				emailSender.send(mensaje);
									
	    	}catch (MessagingException e) {
		            // Verificar si la causa de la excepción es una SocketException
		            Throwable cause = e.getCause();
		            while (cause != null) {
		                if (cause instanceof SocketException) {
		                    throw new MiExcepcion("No se pudo enviar el email debido a un problema de conexión: ");
		                }
		                cause = cause.getCause();
		            }
		            // Si no es una SocketException, lanzar una excepción genérica
		            throw new MiExcepcion("<span class='fs-6 fw-bold'>Estimado Cliente,</span><br><br>"
	                		+ "<span class='fs-6'> Ha ocurrido un error al enviar el email de confimación del turno generado, pero no te preocupes"
	                		+ "que igualmente el turno fue generado con exito y podrás visualizarlo en la pestaña de Mis Turnos sin ningún problema<br>"
	                		+ "Te pedimos disculpas por las molestias ocacionadas");
		        } catch (Exception e) {
		            System.err.println("Exception class: " + e.getClass().getName());
		            System.err.println("Exception message: " + e.getMessage());
		            e.printStackTrace();
		            throw new MiExcepcion("<span class='fs-6 fw-bold'>Estimado Cliente,</span><br><br>"
	                		+ "<span class='fs-6'> Ha ocurrido un error al enviar el email de confimación del turno generado, pero no te preocupes"
	                		+ "que igualmente el turno fue generado con exito y podrás visualizarlo en la pestaña de Mis Turnos sin ningún problema<br>"
	                		+ "Te pedimos disculpas por las molestias ocacionadas");
		        
			}
	        
	    }
	
}
