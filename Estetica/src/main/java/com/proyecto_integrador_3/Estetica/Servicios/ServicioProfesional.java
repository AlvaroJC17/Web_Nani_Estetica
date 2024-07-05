package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.TRUE;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioProfesional;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;

import jakarta.transaction.Transactional;

@Service
public class ServicioProfesional {
	
	@Autowired
	public RepositorioUsuario repositorioUsuario;
	
	@Autowired
	public RepositorioProfesional repositorioProfesional;
	
	@Autowired
	public RepositorioPersona repositorioPersona;
	

	
	public Optional <Persona> buscarDatosProfesionalPorId(String id){
		return repositorioPersona.buscarPersonaPorId(id);
	}
	
	
		public Boolean validarProvincia(String provincia) throws MiExcepcion {
			if (provincia == null || provincia.isEmpty() || provincia.equals("Seleccione provincia")) {
				return false;
			}else {
				return true;
			}
				
		}
	    
	    //este me sirve
	    public List<Persona> buscarPacienteByRolAndDni(String dni, Rol rol){
	    	return repositorioPersona.buscarPacientePorRolYDni(rol, dni);
	    }
	    
	    public List<Persona> buscarProfesionaByRolAndProvincis(Rol rol, Provincias provincias){
	    	return repositorioPersona.buscarNombreApellidoPorRolYProvincia(rol, provincias);
	    }
	    
	    public List<Persona> buscarProfesionaByRolAndProvinciasYActivo(Rol rol, Provincias provincias, Boolean activo){
	    	return repositorioPersona.buscarNombreApellidoPorRolProvinciaYActivo(rol, provincias, activo);
	    }
	    
	
	    //Este me sirve
		public List <Persona> buscarPacienteByRolAndEmail2(String email, Rol rol){
	    	return repositorioPersona.buscarPacientePorRolYEmail(rol, email);
	    }
		
		// este me sirve
		public List <Persona> buscarPacienteByRolAndNombre(String nombre, Rol rol){
	    	return repositorioPersona.buscarPacientePorRolYNombre(rol, nombre);
	    }
		
		public List<Profesional> listarTodos() {
	        return repositorioProfesional.findAll();
	    }
		
		 public Profesional buscarProfesionalPorId(String id) {
		        Optional<Profesional> optionalProfesional = repositorioProfesional.findById(id);
		        return optionalProfesional.orElseThrow(() -> new RuntimeException("Profesional no encontrado con el ID: " + id));
		    }

	    public Profesional obtenerPorId(String id) {
	        return repositorioProfesional.findById(id).orElse(null);
	    }
		
		
	    

	@Transactional
	public void registrarProfesional(String email, String matricula, String especialidad,
			String provincia, String direccion, String telefono, String sexo) throws MiExcepcion {
		
		validarDatosProfesional(matricula, especialidad, sexo, telefono, provincia, direccion);

		Optional <Usuario> datosUsuario = repositorioUsuario.buscarPorEmailOptional(email);
		Optional <Persona> datosPersona = repositorioPersona.buscarPorEmailOptional(email);
		
		//Buscamos los datos de nombre, apellido, fechaNacimiento y dni de la persona y los guardamos como nuevo profesional, asi la persona no los tiene que ingresar de nuevo en el formulario
		if (datosUsuario.isPresent() && datosPersona.isPresent()) {
			Usuario datosDelUsuario = datosUsuario.get();
			Persona datosPersonalesPersona = datosPersona.get();
			
			//Pasamos el sexo de String a un objeto de tipo Date
			Sexo nuevoSexo = null;
			nuevoSexo = Sexo.valueOf(sexo.toUpperCase());
			
			Provincias Nuevaprovincia = null;
			Nuevaprovincia = Provincias.valueOf(provincia.toUpperCase());
			
			Profesional nuevo_profesional = new Profesional();
			nuevo_profesional.setEmail(email);
			nuevo_profesional.setContrasena(datosDelUsuario.getContrasena());
			nuevo_profesional.setRol(datosDelUsuario.getRol());
			nuevo_profesional.setActivo(datosDelUsuario.getActivo());
			nuevo_profesional.setValidacionForm(TRUE);
			nuevo_profesional.setEmailValidado(datosDelUsuario.getEmailValidado());
			nuevo_profesional.setFechaCreacion(datosDelUsuario.getFechaCreacion());
			nuevo_profesional.setDni(datosPersonalesPersona.getDni());
			nuevo_profesional.setNombre(datosPersonalesPersona.getNombre());
			nuevo_profesional.setApellido(datosPersonalesPersona.getApellido());
			nuevo_profesional.setMatricula(matricula);
			nuevo_profesional.setEspecialidad(especialidad);;
			nuevo_profesional.setTelefono(telefono);
			nuevo_profesional.setProvincia(Nuevaprovincia);
			nuevo_profesional.setDomicilio(direccion);
			nuevo_profesional.setFechaNacimiento(datosDelUsuario.getFechaNacimiento());
			nuevo_profesional.setSexo(nuevoSexo);
			repositorioProfesional.save(nuevo_profesional);
			repositorioUsuario.delete(datosDelUsuario);
		}
	}
	
	@Transactional
	public void modificarProfesional(String idAdmin, String email, String emailAnterior, String domicilio, String sexo, String telefono) throws MiExcepcion {
		
		verificarEmailProfesional(email, emailAnterior);
		validarActualizacionDeDatosProfesional(domicilio, sexo, telefono);
		
		Optional<Profesional> identificarProfesional = repositorioProfesional.findById(idAdmin);
		
		if (identificarProfesional.isPresent()) {
			Profesional profesional_actualizado = identificarProfesional.get(); // Atribuye el objeto presente a esta nueva variable
			
			Sexo nuevoSexo = null;
			nuevoSexo = Sexo.valueOf(sexo.toUpperCase());
			
			profesional_actualizado.setEmail(email);
			profesional_actualizado.setSexo(nuevoSexo);
			profesional_actualizado.setDomicilio(domicilio);
			profesional_actualizado.setTelefono(telefono);
        	repositorioProfesional.save(profesional_actualizado);
		}
	}
	
	 public void validarDatosProfesional(String matricula, String especialidad,  String sexo, String telefono, String provincia, String direccion) throws MiExcepcion {
	
		 // Expresión regular para validar un telefono
		 String regex = "\\d{7,10}";
		 
		 // Compilar la expresión regular
		 Pattern pattern = Pattern.compile(regex);
		 
		 // Crear un objeto Matcher
		 Matcher matcher = pattern.matcher(telefono);
		 
		 if (!matcher.matches()) {
			 throw new MiExcepcion("<span class='fs-6 fw-bold'>Estimado usuario,</span><br><br>"
					 + "<span class='fs-6'>El telefono no cumple con el formato solicitado, por favor verifique e intente nuevamente.</span>");
		 } 
				 
		 if (provincia == null || provincia.isEmpty() || provincia.trim().isEmpty()) {
			 throw new MiExcepcion("Debe seleccionar una provincia"); 
		 } 
		 
		 if (matricula == null || matricula.isEmpty() || matricula.trim().isEmpty()) {
			 throw new MiExcepcion("La matricula no puede estar vacia");
		 }
		 if (especialidad == null || especialidad.isEmpty() || especialidad.trim().isEmpty()) {
			 throw new MiExcepcion("La especialidad no puede estar vacia");
		 }
		 
		 if (sexo == null || sexo.isEmpty() || sexo.trim().isEmpty() || sexo.equals("Seleccione")) {
			 throw new MiExcepcion("El sexo no puede estar vacio");
		 }
		 
		 if (telefono == null || telefono.toString().isEmpty() || telefono.toString().isEmpty()) {
			 throw new MiExcepcion("EL telefono no puede estar vacio");
		 }
		 if (direccion == null || direccion.isEmpty() || direccion.trim().isEmpty()) {
			 throw new MiExcepcion("La dirección no puede estar vacia");
		 }
	 }

	 
	 public void validarActualizacionDeDatosProfesional(String domicilio, String sexo, String telefono) throws MiExcepcion {
		 
		 // Expresión regular para validar un telefono
		 String regex = "\\d{7,10}";
		 
		 // Compilar la expresión regular
		 Pattern pattern = Pattern.compile(regex);
		 
		 // Crear un objeto Matcher
		 Matcher matcher = pattern.matcher(telefono);
		 
		 if (!matcher.matches()) {
			 throw new MiExcepcion("<span class='fs-6 fw-bold'>Estimado usuario,</span><br><br>"
					 + "<span class='fs-6'>El telefono no cumple con el formato solicitado, por favor verifique e intente nuevamente.</span>");
		 } 
		 
		 if (domicilio == null || domicilio.isEmpty() || domicilio.trim().isEmpty()) {
			 throw new MiExcepcion("La direccion no puede estar vacia");
		 }
		 
		 if (telefono == null || telefono.toString().isEmpty() || telefono.toString().isEmpty()) {
			 throw new MiExcepcion("EL telefono no puede estar vacio");
		 }
		 
		 if (sexo == null || sexo.isEmpty() || sexo.trim().isEmpty() || sexo.equals("Seleccione")) {
			 throw new MiExcepcion("El sexo no puede estar vacio");
		 }
		 
		 if (!sexo.equalsIgnoreCase("masculino") && !sexo.equalsIgnoreCase("femenino") && !sexo.equalsIgnoreCase("otro")) {
			 throw new MiExcepcion("El sexo solo puede ser masculino, femenino u otro");
		 }
		 
	 }
	 
	 public void verificarEmailProfesional(String email, String emailAnterior) throws MiExcepcion {
		 
			// Expresión regular para validar un correo electrónico
		        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

		        // Compilar la expresión regular
		        Pattern pattern = Pattern.compile(regex);

		        // Crear un objeto Matcher
		        Matcher matcher = pattern.matcher(email);
		        
		        if (email == null || email.isEmpty() || email.trim().isEmpty()) {
		            throw new MiExcepcion("El email no puede estar vacio");
		        }

		        // Verificar si la cadena cumple con la expresión regular
		        if (!matcher.matches()) {
		            throw new MiExcepcion("El correo electronico no es valido");
		        } 
		       
		        if (!email.equalsIgnoreCase(emailAnterior)) {
		        	if (repositorioUsuario.buscarPorEmailOptional(email).isPresent()) {
		        		throw new MiExcepcion("El email ingresado ya se encuentra registrado, por favor ingrese otro");
		        	}
						
		        }
		        
	 }
	 
	
}
