package com.proyecto_integrador_3.Estetica.Servicios;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.CodigoDeVerificacion;
import com.proyecto_integrador_3.Estetica.Entidades.EmailUsuarios;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCodigoDeVerificacion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;

import jakarta.transaction.Transactional;

@Service
public class ServicioCodigoDeVerificacion {

	@Autowired
	RepositorioCodigoDeVerificacion repositorioCodigoDeVerificacion;
	
	@Autowired
	RepositorioUsuario repositorioUsuario;
	
	@Autowired
	ServicioEmail servicioEmail;
	
	//Metodo para generar codigo alfa numerico de 6 digitos
	  public String generadorDeCodigos() {
		  	final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		    final int CODE_LENGTH = 6;
		    final SecureRandom random = new SecureRandom();
		    
		    StringBuilder codigo = new StringBuilder(CODE_LENGTH);
	        for (int i = 0; i < CODE_LENGTH; i++) {
	            int index = random.nextInt(CHARACTERS.length());
	            codigo.append(CHARACTERS.charAt(index));
	        }
	        return codigo.toString();
	  }
	
	
	  //meotodo para gener los datos del codigo y guardarlos en la tabla de codigos,
	  // el unico valor que usamos en este metodo del objeto usuario es su id.
	  //este metodo tambien se encargar de enviar el mail al usuario
	  @Transactional
	public void generarGuardarYEnviarCodigo(Usuario usuario, String email) throws MiExcepcion {
        String codigo = generadorDeCodigos();
        LocalDateTime expiracion = LocalDateTime.now().plusMinutes(5);

        CodigoDeVerificacion codigoVerificacion = new CodigoDeVerificacion();
        codigoVerificacion.setCodigo(codigo);
        codigoVerificacion.setExpiracion(expiracion);
        codigoVerificacion.setUsuario(usuario);
        codigoVerificacion.setUsado(false);

        try {
        	repositorioCodigoDeVerificacion.save(codigoVerificacion);
        } catch (Exception e) {
        	throw new MiExcepcion("Error al conectar con el servidor " + e);
        }
			        
        EmailUsuarios datosDelEmail = new EmailUsuarios();
        datosDelEmail.setAsunto("Nani estética - Validar correo eléctronico");
        datosDelEmail.setDestinatario("alvarocortesia@gmail.com");
        datosDelEmail.setMensaje(codigo);
        
        try {
			servicioEmail.enviarEmailUsuario(datosDelEmail);
		} catch (Exception e) {
			throw new MiExcepcion("Error al enviar codigo de verificación.");
		}
	  }
			
	
	//Metodo para comparar el codigo ingesado por el usuario con el que esta guardado en la base de datos
	//ademos valida que el codigo no haya expirado y que no este usado y sino fue usado entonces lo pasa
	//a usado
	  @Transactional
	  public boolean validarCodigoIngresado(String codigoUsuario, Usuario usuario) throws MiExcepcion {

		  try {
			  CodigoDeVerificacion codigoVerificacion = repositorioCodigoDeVerificacion.findByCodigoAndUsuario(codigoUsuario, usuario);
	        if (codigoVerificacion != null && codigoVerificacion.esValido()) {
	            codigoVerificacion.setUsado(true);
	            repositorioCodigoDeVerificacion.save(codigoVerificacion);
	            return true;
	        }
	        return false;
		  } catch (Exception e) {
			  System.out.println("Error al conectar con el servidor " + e);
		  }
		  return false;
	  }
	        
	           
					
	  
	  //meotod para buscar en un registro en la tabla de codigos por id del usuario
	  public Optional<CodigoDeVerificacion> obtenerDatosTablaCodigoPorUsuarioId(String usuarioId) throws MiExcepcion {
		  
		  try {
			  return repositorioCodigoDeVerificacion.findByUsuarioId(usuarioId);
		  } catch (Exception e) {
			 throw new MiExcepcion("Error al conectar con el servidor " + e);
		  }  
			
		}

}

