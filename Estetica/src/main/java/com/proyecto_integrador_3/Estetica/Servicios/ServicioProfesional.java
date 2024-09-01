package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.TRUE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Tratamiento;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.DiasDeLaSemana;
import com.proyecto_integrador_3.Estetica.Enums.Especialidad;
import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.Enums.TipoDeEspecialidad;
import com.proyecto_integrador_3.Estetica.Enums.TratamientoEnum;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
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
	
	@Autowired
	public RepositorioCliente repositorioCliente;
	

	

	public Optional <Profesional> buscarProfesional(String idProfesional){
		return repositorioProfesional.findById(idProfesional);
	}
	
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
		
	    public String manejoDeErroresControladorProfesional(String identificador, String error, Model model) {
	    	Boolean isEspecialidadDisabled = false;
			Boolean isProfesionalDisabled = false;
			Boolean isFechaDisabled = false;
			Boolean isHorarioDisabled = false;
			Boolean isTratamientoDisabled = false;
			
			model.addAttribute("error", error);
			model.addAttribute("isFechaDisabled", isFechaDisabled);
			model.addAttribute("isEspecialidadDisabled",isEspecialidadDisabled);
			model.addAttribute("isProfesionalDisabled",isProfesionalDisabled);
			model.addAttribute("isHorarioDisabled" ,isHorarioDisabled);
			model.addAttribute("isTratamientoDisabled" ,isTratamientoDisabled);
			model.addAttribute("showModalError", true);
			switch (identificador) {
			case "tratamientoFacial":
				return "/pagina_cliente/reservaDeTurnoClienteFacial";
			case "tratamientoCorporal":
				return "/pagina_cliente/reservaDeTurnoClienteCorporal";
			case "tratamientoEstetico":	
				return "/pagina_cliente/reservaDeTurnoClienteEstetico";
			}			
	    	return"";
	    }
	
	    public String manejoDeErroresSeleccionDeFecha(
	    		String identificador,
	    		String error,
	    		Model modelo) {
	    	
	    	Boolean isProfesionalDisabled = false;
			Boolean isEspecialidadDisabled = false;
			Boolean isFechaDisabled = false;
	    	
	    	modelo.addAttribute("error", error);
			modelo.addAttribute("showModalError", true);
			modelo.addAttribute("isProfesionalDisabled", isProfesionalDisabled);
			modelo.addAttribute("isEspecialidadDisabled", isEspecialidadDisabled);
			modelo.addAttribute("isFechaDisabled", isFechaDisabled);
			if (identificador.equals("tratamientoFacial")) {
				return "/pagina_cliente/reservaDeTurnoClienteFacial";
			}else if(identificador.equals("tratamientoCorporal")) {
				return "/pagina_cliente/reservaDeTurnoClienteCorporal";
			}else if(identificador.equals("tratamientoEstetico")) {
				return "/pagina_cliente/reservaDeTurnoClienteEstetico";
			}else {
				return "";
			}
	    	
	    }
		
		
	    public List<TipoDeEspecialidad> listarEspecialidadesDisponibles(String identificador){
		List<TipoDeEspecialidad> especialidadDisponibles = null;
		// Se filtra la lista de enum de tipo especialidades segun el identificador seleccionado y se guarda en la lista vacía
				if (identificador.equals("tratamientoFacial")) {
					 List<TipoDeEspecialidad> especialidadesConFacial = Arrays.stream(TipoDeEspecialidad.values())
					            .filter(especialidad -> especialidad.name().contains("FACIAL"))
					            .collect(Collectors.toList());
					 especialidadDisponibles = especialidadesConFacial;
					 
				}else if(identificador.equals("tratamientoCorporal")) {
					List<TipoDeEspecialidad> especialidadesCorporal = Arrays.stream(TipoDeEspecialidad.values())
				            .filter(especialidad -> especialidad.name().contains("CORPORAL"))
				            .collect(Collectors.toList());
				 especialidadDisponibles = especialidadesCorporal;
				 
				}else if(identificador.equals("tratamientoEstetico")) {
					List<TipoDeEspecialidad> especialidadesEstetico = Arrays.stream(TipoDeEspecialidad.values())
				            .filter(especialidad -> especialidad.name().contains("ESTETICO"))
				            .collect(Collectors.toList());
				 especialidadDisponibles = especialidadesEstetico;
				}else {
					//Si por alguna razon el identificador esta vacio o es diferente a las opciones indicadas, devuelve una lista vacía
					especialidadDisponibles = Collections.emptyList();
				}
				
		return especialidadDisponibles;
	}	
	    

	@Transactional
	public void registrarProfesional(String email, String matricula, String provincia,
			String direccion, String telefono, String sexo, String especialidadModoEnum, String tipoEspecialidadModoEnum,
			String DiasDeLaSemanaSeleccionados, String horariosSeleccionados, String tratamientosSeleccionados ) throws MiExcepcion {
		
		   //Validamos todos los datos ingresados por el usuario antes de crear al profesional
		validarTratamientosAndEspecialidadesProfesional(especialidadModoEnum, tipoEspecialidadModoEnum, DiasDeLaSemanaSeleccionados, horariosSeleccionados, tratamientosSeleccionados);
		
		//Obtenemos el string de especialidades y lo pasamos a enum
		Especialidad nuevoEspecialidad = null;
		nuevoEspecialidad = Especialidad.valueOf(especialidadModoEnum);
		
		//Pasamos el string tipoDeEspecialidad a enum
		TipoDeEspecialidad nuevoTipoDeEspecialidad = null;
		nuevoTipoDeEspecialidad = TipoDeEspecialidad.valueOf(tipoEspecialidadModoEnum);
		
		
		//Obtenemos el string de diasDeLaSemana, lo dividimos y lo pasamos a una lista de enum DiasDeLaSemana
		List<DiasDeLaSemana> listaDiasDeLaSemana = new ArrayList<>();
		List<String> diasDeLaSemanaList = Arrays.asList(DiasDeLaSemanaSeleccionados.split(","));
		for (String diasDeLaSemana : diasDeLaSemanaList) {
			DiasDeLaSemana nuevoDiasDeLaSemana = null;
			nuevoDiasDeLaSemana = DiasDeLaSemana.valueOf(diasDeLaSemana.toUpperCase());
			listaDiasDeLaSemana.add(nuevoDiasDeLaSemana);
		}
		
		//Obtenemos el string de horarios, lo dividimos y guardamos en una lista de String
		List<String> horariosList = Arrays.asList(horariosSeleccionados.split(","));
		
		
		//Obtenemos el string de tratamientos con sus precios, los dividimos y los usamos para crear
		//un objeto Tratamiento y luego creamos una lista de objetos Tratamientos
		List<String> tratamientosList = Arrays.asList(tratamientosSeleccionados.split(",")); // acá separamos los tratamientos
	        // 2. Separar cada tratamiento de su costo
	        List<Tratamiento> listaTratamientos = new ArrayList<>(); //Lista donde guardaremos los objetos Tratamiento
	        for (String tratamientoCosto : tratamientosList) {
	        	String[] partes = tratamientoCosto.split(" - \\$"); //Acá separamos el tratamiento del costo
	        	
	        	 // Validar que el tratamiento tenga el formato correcto
	            if (partes.length < 2 || partes[0].trim().isEmpty() || partes[1].trim().isEmpty()) {
	                throw new MiExcepcion("Error: El tratamiento \"" + tratamientoCosto + "\" no tiene un formato válido.");
	            }
	            
	            String nombreTratamiento = partes[0]; // Acá separamos el tratamiento que corresponde al indice [0] del array y lo guardamos en una variable
	            int costoTratamiento = Integer.parseInt(partes[1]); //Acá separamos el costo que corresponde al indice [1] del array, lo parseamos a un int y lo guardamos en una variable.
	            
	            // Validar que el tratamiento tenga un costo válido
	            if (costoTratamiento < 0) {
	                throw new MiExcepcion("Error: El tratamiento \"" + nombreTratamiento + "\" tiene un costo inválido.");
	            }
	                 
	            //Pasamos la nombre del tratamiento a un tipo Enum
	            TratamientoEnum nuevoTratamienoEnum = null;
	            nuevoTratamienoEnum = TratamientoEnum.valueOf(nombreTratamiento.toUpperCase());
	            //Creamos un objeto Tratamiento con las dos variables anteriores y lo guardamos en una lista de tipo Tratamientos
	            listaTratamientos.add(new Tratamiento(nuevoTratamienoEnum, costoTratamiento));
	        }
	        
				
		try {
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
			nuevo_profesional.setEmail(email.trim());
			nuevo_profesional.setContrasena(datosDelUsuario.getContrasena());
			nuevo_profesional.setRol(datosDelUsuario.getRol());
			nuevo_profesional.setActivo(datosDelUsuario.getActivo());
			nuevo_profesional.setValidacionForm(TRUE);
			nuevo_profesional.setEmailValidado(datosDelUsuario.getEmailValidado());
			nuevo_profesional.setFechaCreacion(datosDelUsuario.getFechaCreacion());
			nuevo_profesional.setDni(datosPersonalesPersona.getDni());
			nuevo_profesional.setNombre(datosPersonalesPersona.getNombre());
			nuevo_profesional.setApellido(datosPersonalesPersona.getApellido());
			nuevo_profesional.setMatricula(matricula.trim());
			nuevo_profesional.setEspecialidad(nuevoEspecialidad);
			nuevo_profesional.setTipoEspecialidad(nuevoTipoDeEspecialidad);
			nuevo_profesional.setDiasDeLaSemana(listaDiasDeLaSemana);
			nuevo_profesional.setHorariosLaborales(horariosList);
			nuevo_profesional.setTratamientos(listaTratamientos);
			nuevo_profesional.setTelefono(telefono.trim());
			nuevo_profesional.setProvincia(Nuevaprovincia);
			nuevo_profesional.setDomicilio(direccion.trim());
			nuevo_profesional.setFechaNacimiento(datosDelUsuario.getFechaNacimiento());
			nuevo_profesional.setSexo(nuevoSexo);
			repositorioProfesional.save(nuevo_profesional);
			repositorioUsuario.delete(datosDelUsuario);
		}
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con el servidor " + e);
		}
	}
	
	
	
	@Transactional
	public void modificarProfesional(String idAdmin, String email, String emailAnterior, String domicilio, String sexo, String telefono) throws MiExcepcion {
		
		verificarEmailProfesional(email, emailAnterior);
		validarActualizacionDeDatosProfesional(domicilio, sexo, telefono);
		
		try {
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
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con el servidor " + e);
		}
	}
	
	public List<Tratamiento> buscarTratamitosPorProfesional(String idProfesional) throws Exception{
		
		List<Tratamiento> tratamientosProfesional = null;
		try {
			Optional<Profesional> identificarProfesional = repositorioProfesional.findById(idProfesional);
			if (identificarProfesional.isPresent()) {
				Profesional datosProfesional = identificarProfesional.get(); // Atribuye el objeto presente a esta nueva variable
				tratamientosProfesional = datosProfesional.getTratamientos();
				return tratamientosProfesional;	
			}
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con el servidor " + e);
		}
		return tratamientosProfesional;
	}
	
	//Servicio para guardar las notas que le profesional le coloca al cliente
	@Transactional
	public void guardarNotasProfesional(String idCliente, String notas_profesional) throws MiExcepcion {
		
		if (notas_profesional.length() > 500)  /*Este no lleva la validacion del Seleccione porque es un textarea*/
			 throw new MiExcepcion("Ha superado el máximo de caracteres permitidos para el campo de cuidado de la piel");
			
		try {
			Optional<Cliente> identificarCliente = repositorioCliente.findById(idCliente);
			
			String notasDelProfesional = null;
			LocalDate fechaModificacion = null;
			LocalDate fechaActual = LocalDate.now();
			if (identificarCliente.isPresent()) {
				Cliente datosCliente = identificarCliente.get();
				notasDelProfesional = datosCliente.getNotas_profesional();
				fechaModificacion = datosCliente.getFechaModificacion();
				
				
				if (notasDelProfesional == null) {
					notasDelProfesional = "";
				}
				
				if (fechaModificacion == null) {
					fechaModificacion = LocalDate.now();
				}
				
				if (fechaModificacion != fechaActual) {
					fechaModificacion = fechaActual;
				}
				
				if (notasDelProfesional.equalsIgnoreCase(notas_profesional)) {
					return;
				}
				
				datosCliente.setNotas_profesional(notas_profesional);
				datosCliente.setFechaModificacion(fechaModificacion);
				repositorioCliente.save(datosCliente);
				
				
			}else {
				throw new MiExcepcion("No se encontro el cliente");
			}
			
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con el servidor");
		}
	}
	
	
	public void validarTratamientosAndEspecialidadesProfesional(String especialidadesSeleccionadas, String tipoEspecialidadesSeleccionadas, String DiasDeLaSemanaSeleccionados,
			 String horariosSeleccionados, String tratamientosSeleccionados) throws MiExcepcion {
		
		
		 if (tipoEspecialidadesSeleccionadas.isEmpty() || tipoEspecialidadesSeleccionadas.trim().isEmpty() || tipoEspecialidadesSeleccionadas == null) {
			 throw new MiExcepcion("Debe seleccionar por lo menos un tipo de especialidad");
		}
		 
		 if (especialidadesSeleccionadas.isEmpty() || especialidadesSeleccionadas.trim().isEmpty() || especialidadesSeleccionadas == null) {
			 throw new MiExcepcion("Debe seleccionar por lo menos una especialidad");
		}
		 
		 if (tratamientosSeleccionados.isEmpty() || tratamientosSeleccionados.trim().isEmpty() || tratamientosSeleccionados == null) {
			 throw new MiExcepcion("Debe indicar por lo menos un tratamiento");
		}
		 
		 if (DiasDeLaSemanaSeleccionados.isEmpty() || DiasDeLaSemanaSeleccionados.trim().isEmpty() || DiasDeLaSemanaSeleccionados == null) {
			 throw new MiExcepcion("Debe seleccionar por lo menos un día de la semana");
		}
		 
		 if (horariosSeleccionados.isEmpty() || horariosSeleccionados.trim().isEmpty() || horariosSeleccionados == null) {
			 throw new MiExcepcion("Debe indicar por lo menos un horario");
		}
		
	}
	
	 public void validarDatosProfesional(String matricula, String sexo, String telefono, String provincia,
			 String direccion) throws MiExcepcion {
	
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
