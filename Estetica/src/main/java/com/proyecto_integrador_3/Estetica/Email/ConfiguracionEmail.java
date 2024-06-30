package com.proyecto_integrador_3.Estetica.Email;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:email.properties") //podemos crear un archivo de properties propio y con esta etiqueta le indicamos el nombre de ese archivo personalizado para que lo tome como referencia en esta clase
public class ConfiguracionEmail {

	//Asignamos en valor de las variables creadas en el archivo properties a las variables de la clase
	@Value("${email.username}")
	private String email;
	
	//igual para esta
	@Value("${email.password}")
	private String password;
	
	//establecemos las propiedades del servicio de correo usanso el objeto Properties
	//en esta caso son para usar el mail de gmail
	private  Properties getMailProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host","smtp.gmail.com");
		properties.put("mail.smtp.port","587");
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		return properties;
	}

	//creamos los bean que se van a inyectar en el servicio de ServicioEmail
    @Bean
   public JavaMailSender javaMailSender() {
		JavaMailSenderImpl envioEmail = new JavaMailSenderImpl();
		envioEmail.setJavaMailProperties(getMailProperties());
		envioEmail.setUsername(email);
		envioEmail.setPassword(password);
		return envioEmail;
	}

    @Bean
   public ResourceLoader resourceLoader() {
		return new DefaultResourceLoader();
	}
}
