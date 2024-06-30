package com.proyecto_integrador_3.Estetica.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import com.proyecto_integrador_3.Estetica.Entidades.EmailUsuarios;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ServicioEmail {


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
	
}
