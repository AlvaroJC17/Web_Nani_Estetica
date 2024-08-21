package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.TRUE;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.Boolean.FALSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.EmailUsuarios;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Tratamiento;
import com.proyecto_integrador_3.Estetica.Entidades.Turnos;
import com.proyecto_integrador_3.Estetica.Enums.EstadoDelTurno;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioTratamiento;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioTurnos;

import jakarta.transaction.Transactional;




@Service
public class ServicioTurnos {
	
	@Autowired
	RepositorioTurnos repositorioTurnos;
	
	@Autowired
	RepositorioCliente repositorioCliente;
	
	@Autowired
	RepositorioPersona repositorioPersona;
	
	@Autowired
	RepositorioTratamiento repositorioTratamiento;
	
	@Autowired
	ServicioHorario servicioHorario;
	
	@Autowired
	ServicioEmail servicioEmail;
	
	
	public List<Turnos> buscarTurnosPorProfesionalIdAndFecha(String idProfesional, LocalDate fecha) throws MiExcepcion{
		try {
			return repositorioTurnos.findByProfesionalIdAndFecha(idProfesional, fecha);
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con el servidor");
		}
	}
	
	public List <Turnos> buscarTurnosPorProfesionalId(String idProfesional){
		return repositorioTurnos.findByProfesionalId(idProfesional);
	}
	
	public List <Turnos> buscarTurnoPorClienteId(String idCliente){
		return repositorioTurnos.findTurnosByClienteId(idCliente);
	}

	public Optional <Turnos> buscarTurnoPorId(String id){
		return repositorioTurnos.findById(id);
	}
	
	public List<Turnos> obtenerTurnosPorDni(String dni) {
        return repositorioTurnos.findByDni(dni);
    }
	
	public List<Turnos> obtenerTurnosPorEmail(String email){
		return repositorioTurnos.findByEmail(email);
	}
	
	public List<Turnos> obetnerTurnosPorEstadoAndActivoAndMultaAndEmailCliente(EstadoDelTurno estado, Boolean activo, Boolean multa, String emailCliente){
		return repositorioTurnos.findByEstadoAndActivoAndMultaAndEmailOrderByFechaCreacion(estado, activo, multa, emailCliente);
	}
	
	public List<Turnos> ObetenerTurnosPorEstadoAndActivoAndMultaAndIdProfesional(EstadoDelTurno estado, Boolean activo, Boolean multa, String idProfesional){
		return repositorioTurnos.findByEstadoAndActivoAndMultaAndProfesionalIdOrderByFechaCreacion(estado, activo, multa, idProfesional);
	}
	
	 public List<Turnos> obtenerUltimosTresRegistros(List<Turnos> turnos) {
	        int size = turnos.size();
	        if (size > 3) {
	            return turnos.subList(size - 3, size);
	        } else {
	            return turnos;
	        }
	    }
	
	 public boolean checkActiveTurnos(String email) throws MiExcepcion {
		 try {
			 List<Turnos> activeTurnos = repositorioTurnos.findByActivoAndEmail(true, email);
			 return activeTurnos.size() > 2;
		 } catch (Exception e) {
			 throw new MiExcepcion("Error al conectar con el servidor " + e);
		 }
	 }
			
	
	 @Transactional
	public void actualizarEstadoDelTurno(String id, Rol rol, EstadoDelTurno estadoTurno) throws MiExcepcion {
		 try {
			 Optional<Turnos> turnoPorId = buscarTurnoPorId(id);
			 if (turnoPorId.isPresent()) {
				 Turnos actualizarTurnoActivo = turnoPorId.get();
				 actualizarTurnoActivo.setActivo(FALSE);
				 
				 if (estadoTurno == EstadoDelTurno.CANCELADO) {
					 actualizarTurnoActivo.setEstado(EstadoDelTurno.CANCELADO);
				 }else if(estadoTurno == EstadoDelTurno.ASISTIDO) {
					 actualizarTurnoActivo.setEstado(EstadoDelTurno.ASISTIDO);
				 }
					
				 if (rol == Rol.CLIENTE ) {
					 actualizarTurnoActivo.setCanceladoPor(Rol.CLIENTE);
				 }else if(rol == Rol.PROFESIONAL) {
					 actualizarTurnoActivo.setCanceladoPor(Rol.PROFESIONAL);
				 }else if(rol == Rol.ADMIN) {
					 actualizarTurnoActivo.setCanceladoPor(Rol.ADMIN);
				 }
					
				 Turnos turnoCancelado;
				 turnoCancelado = repositorioTurnos.save(actualizarTurnoActivo);
				 
				 //Creamos el objeto con los datos del email para poder enviarlo
				 EmailUsuarios cancelarPorEmailTurno = new EmailUsuarios();
				 cancelarPorEmailTurno.setAsunto("Nani estética - CANCELACIÓN DE TURNO");
				 cancelarPorEmailTurno.setDestinatario("alvarocortesia@gmail.com");
				 
				 //asignamos a la variable el nombre de la plantilla que vamos a utilizar
				 String plantillaHTML= "emailCancelacionDeTurno";
				 
				 //Este boolean sirve para indicar dentro del metodo si se agregar a la plantilla los valos de multa y costo de multa a la plantilla
				 Boolean multa = false;
				 
				 try {
					 //Llamamos al servicio para enviar el email
					 servicioEmail.enviarConfirmacionOCancelacionTurno(cancelarPorEmailTurno, turnoCancelado, plantillaHTML, multa);
				 } catch (Exception e) {
					 throw new MiExcepcion("Error al enviar el email de cancelacion de turno");
				 }
				 
			 }
		 } catch (Exception e) {
			 throw new MiExcepcion("Error al conectar con el servidor " + e);
		 }
	 }
		 
		 
	
	//Busca los turnos por email en orden ascendente
	public List<Turnos> getTurnosByEmail(String email) throws MiExcepcion {
		try {
			return repositorioTurnos.findByEmailOrderByFechaAsc(email);
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con el servidor " + e);
		}
	}
			

	//busca los turnos en orden de mas viejo a mas nuevo y cuando el tamaño de los turnos del usuario
	// es mayor a 3, elimina el mas antigua de la base de datos
	@Transactional
	public void eliminarTurnoMasAntiguo(String email) throws MiExcepcion {
		try {
			List<Turnos> turnos = getTurnosByEmail(email);
			if (turnos.size() > 3 ) {
				Turnos turnoMasAntiguo = turnos.get(0);
				repositorioTurnos.delete(turnoMasAntiguo);
			}
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con el servidor " + e);
		}
	}
	
	
    
    public List<Turnos> getTurnosNoActivosByEmail(String email) throws MiExcepcion {
    	try {
    		return repositorioTurnos.findByEmailAndActivoOrderByFechaAsc(email, false);
    	} catch (Exception e) {
    		throw new MiExcepcion("Error al conectar con el servidor " + e);
    	}
			
    }

    	
    //Cando hay mas de tres turnos en la bandeja, busca el mas antiguo que no tenga multa y lo borra de la base de datos
    @Transactional
    public void eliminarTurnoMasAntiguoNoActivo(String email) throws MiExcepcion {
    	try {
    		List<Turnos> turnos = getTurnosByEmail(email);
    		if (turnos.size() > 3) {
    			List<Turnos> turnosNoActivosSinMulta = repositorioTurnos.findByEmailAndActivoFalseAndMultaFalse(email);
    			while (turnos.size() > 3 && !turnosNoActivosSinMulta.isEmpty()) {
    				Turnos turnoMasAntiguoNoActivo = turnosNoActivosSinMulta.get(0);
    				repositorioTurnos.delete(turnoMasAntiguoNoActivo);
    				turnosNoActivosSinMulta = repositorioTurnos.findByEmailAndActivoFalseAndMultaFalse(email);  // Refresca la lista de turnos no activos sin multa
    				turnos = getTurnosByEmail(email);  // Refresca la lista de todos los turnos
    			}
    		}
    	} catch (Exception e) {
    		throw new MiExcepcion("Error al conectar con el servidor " + e);
    	}
    }
    	
    
    
    //pasa los turnos activos con fecha anterior a la actual a inactivo y les asigna una multa al turno y al cliente
    @Transactional
    public void actualizarTurnosAntiguos(String email) throws MiExcepcion {
    	try {
    		List<Turnos> turnos = getTurnosByEmail(email);
    		LocalDateTime fechaActual = LocalDateTime.now();
    		
    		for (Turnos turno : turnos) {
    			String fechaTurno = turno.getFecha().toString();
    			String horarioTurno = turno.getHorario().toString();
    			String fechaAndHorario = fechaTurno + " " + horarioTurno;
    			String valorMulta = turno.getProfesional().getValorMulta();
    			
    			LocalDateTime fechaTurnoLocalDateTime = null;
    			fechaTurnoLocalDateTime = servicioHorario.pasarFechaStringToLocalDateTime(fechaAndHorario);
    			
    			if (fechaTurnoLocalDateTime.isBefore(fechaActual.plusMinutes(15)) && turno.getActivo()) { //comparamos la fecha del tunos con la fecha actual mas 15 min
    				turno.setMulta(TRUE); //Le colocamos una multa al turno que tiene fecha pasada
    				turno.setActivo(false); // Lo pasamos a inactivo
    				turno.setEstado(EstadoDelTurno.CANCELADO);
    				turno.setCostoMulta(valorMulta);
    				repositorioTurnos.save(turno);
    				
    				Optional<Cliente> obtenerDatosDeMultas = repositorioCliente.findByEmail(email);
    				if (obtenerDatosDeMultas.isPresent()) {
    					Cliente multasDelCliente = obtenerDatosDeMultas.get();
    					multasDelCliente.setMulta(TRUE); // Le colocamos una multa al cliente
    					repositorioCliente.save(multasDelCliente);
    				}
    			}
    		}
    	} catch (MiExcepcion e) {
    		System.out.println("Error al conectar con el servidor, no se pudieron cancelar los turnos antiguos " + e);
    	}
    }
    	
    
    @Transactional
    public void multarTurnoAndClienteMenosDe24Horas(String email, String idTurno) throws MiExcepcion {
    	
    	try {
    	Optional<Turnos> turnoOptional = repositorioTurnos.findById(idTurno);
        if (turnoOptional.isPresent()) {
            Turnos turno = turnoOptional.get();
            String valorMulta = turno.getProfesional().getValorMulta();
            
            //Buscamos el turno y le actualizamos sus valores a cancelado
            if (turno.getActivo()) {
            	turno.setMulta(TRUE); // Le colocamos una multa al turno que tiene fecha pasada
            	turno.setActivo(false); // Lo pasamos a inactivo
            	turno.setEstado(EstadoDelTurno.CANCELADO);
            	turno.setCanceladoPor(Rol.CLIENTE);
            	turno.setCostoMulta(valorMulta);
            	Turnos turnoCancelado24h;
            		turnoCancelado24h = repositorioTurnos.save(turno);
            	
					
            	//Creamos el objeto con los datos del email para poder enviarlo
                EmailUsuarios cancelarPorEmailTurno = new EmailUsuarios();
                cancelarPorEmailTurno.setAsunto("Nani estética - CANCELACIÓN DE TURNO");
                cancelarPorEmailTurno.setDestinatario("alvarocortesia@gmail.com");
                
                //Asiganmos la plantilla html para la cancelacion del turno
                String plantillaHTML= "emailCancelacionDeTurno"; 
                
                //Este boolean sirve para indicar dentro del metodo si se agregar a la plantilla los valos de multa y costo de multa a la plantilla
                Boolean multa = true;
                
                try {
                	//Enviamos al servicio para mandar al email con la cancelacion del turno
                	servicioEmail.enviarConfirmacionOCancelacionTurno(cancelarPorEmailTurno, turnoCancelado24h, plantillaHTML, multa); 
                } catch (Exception e) {
                	throw new MiExcepcion("Error al enviar el email con la cancelacion de turno");
                }
					
            	
                //Buscamos al cliente y le asigamos la multa
            	Optional<Cliente> obtenerDatosDeMultas = repositorioCliente.findByEmail(email);
            	if (obtenerDatosDeMultas.isPresent()) {
            		Cliente multasDelCliente = obtenerDatosDeMultas.get();
            		multasDelCliente.setMulta(TRUE); // Le colocamos una multa al cliente
            		repositorioCliente.save(multasDelCliente);
            	}
            }
        }
    	} catch (Exception e) {
    		throw new MiExcepcion("Error al conectar con el servidor " + e);
    	}
    }
            		
            		
						
            
               
    @Transactional
	public void formularioTurnos(String idCliente, String email, String fuma, String drogas, String alcohol, String deportes,
			String ejercicios, String medicamentos, String nombreMedicamento, String embarazo, String amamantando,
			String ciclo_menstrual, String alteracion_hormonal,String vitaminas, String corticoides, String hormonas, String metodo_anticonceptivo, String sufre_enfermedad,
			String cual_enfermedad, String tiroides, String paciente_oncologica, String fractura_facial, String cirugia_estetica, 
			String indique_cirugia_estetica, String tiene_implantes, String marca_pasos, String horas_sueno, String exposicion_sol,
			String protector_solar, String reaplica_protector, String consumo_carbohidratos, String tratamientos_faciales_anteriores,
			String resultados_tratamiento_anterior, String cuidado_de_piel, String motivo_consulta, String notas_profesional) throws MiExcepcion {
	
		
		validarDatosFormularioTurno(fuma, drogas, alcohol, deportes, ejercicios, medicamentos, nombreMedicamento, embarazo, amamantando, ciclo_menstrual, alteracion_hormonal, vitaminas, corticoides,
				hormonas, metodo_anticonceptivo, sufre_enfermedad, cual_enfermedad, tiroides, paciente_oncologica,
				fractura_facial, cirugia_estetica, indique_cirugia_estetica, tiene_implantes, marca_pasos,
				horas_sueno, exposicion_sol, protector_solar, reaplica_protector, consumo_carbohidratos,
				tratamientos_faciales_anteriores, resultados_tratamiento_anterior, cuidado_de_piel,
				motivo_consulta);
		
				
		try {
		Optional<Cliente> identificarCliente = repositorioCliente.findById(idCliente);
		if (identificarCliente.isPresent()) {
			Cliente formulario_cliente = identificarCliente.get(); // Atribuye el objeto presente a esta nueva variable
			
			formulario_cliente.setFuma(fuma);
			formulario_cliente.setDrogas(drogas);
			formulario_cliente.setAlcohol(alcohol);
			formulario_cliente.setDeportes(deportes);
			formulario_cliente.setEjercicio(ejercicios);
			formulario_cliente.setMedicamentos(medicamentos);
			formulario_cliente.setNombreMedicamento(nombreMedicamento);
			formulario_cliente.setEmbarazo(embarazo);
			formulario_cliente.setAmamantando(amamantando);
			formulario_cliente.setCiclo_menstrual(ciclo_menstrual);
			formulario_cliente.setAlteracion_hormonal(alteracion_hormonal);
			formulario_cliente.setVitaminas(vitaminas);
			formulario_cliente.setCorticoides(corticoides);
			formulario_cliente.setHormonas(hormonas);
			formulario_cliente.setMetodo_anticonceptivo(metodo_anticonceptivo);
			formulario_cliente.setSufre_enfermedad(sufre_enfermedad);
			formulario_cliente.setCual_enfermedad(cual_enfermedad);
			formulario_cliente.setTiroides(tiroides);
			formulario_cliente.setPaciente_oncologica(paciente_oncologica);
			formulario_cliente.setFractura_facial(fractura_facial);
			formulario_cliente.setCirugia_estetica(indique_cirugia_estetica);
			formulario_cliente.setIndique_cirugia_estetica(indique_cirugia_estetica);
			formulario_cliente.setTiene_implantes(tiene_implantes);
			formulario_cliente.setMarca_pasos(marca_pasos);
			formulario_cliente.setHoras_sueno(horas_sueno);
			formulario_cliente.setExposicion_sol(exposicion_sol);
			formulario_cliente.setProtector_solar(protector_solar);
			formulario_cliente.setReaplica_protector(reaplica_protector);
			formulario_cliente.setConsumo_carbohidratos(consumo_carbohidratos);
			formulario_cliente.setTratamientos_faciales_anteriores(tratamientos_faciales_anteriores);
			formulario_cliente.setResultados_tratamiento_anterior(resultados_tratamiento_anterior);
			formulario_cliente.setCuidado_de_piel(cuidado_de_piel);
			formulario_cliente.setMotivo_consulta(motivo_consulta);
			formulario_cliente.setFomularioDatos(TRUE);
			formulario_cliente.setNotas_profesional(notas_profesional);
			repositorioCliente.save(formulario_cliente);
		}
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con el servidor, no se lograron"
					+ "guardar los datos ingresados " + e);
		}
    }
				
	@Transactional
	public Turnos guardarTurno(String idCliente, String nombreDelProfesional, String fechaSeleccionada,
			String provinciaString, String idProfesional, String tratamientosSeleccionados,
			String horario, String email) throws MiExcepcion {
		
		
		
		//Validamos que todos los valores vengan bien
		validarGuardarTurno(provinciaString, nombreDelProfesional, fechaSeleccionada, horario, tratamientosSeleccionados);
		
		//Separamos el string de tratamientos
		String [] tratamientosSeparados = tratamientosSeleccionados.split(",");
		
		//Creamos una lista donde vamos a filtrar los tratamientos seleccionados, para evitar que el usuario seleccione la opcion "Seleccione tratamientos"
		List <String> tratamientosFiltradosList = new ArrayList<>(); 
	
		
		for (int i = 0; i < tratamientosSeparados.length; i++) {
			//filtramos la palabra "Seleccione tratamientos" y guardamos en el nuevo array
			if (!tratamientosSeparados[i].trim().equalsIgnoreCase("Seleccione tratamientos")) {
				//guardamos los tratamientos filtrados en la lista creada
				tratamientosFiltradosList.add(tratamientosSeparados[i].trim());
			}
		}
		
		if (tratamientosFiltradosList.isEmpty()) {
			throw new MiExcepcion("Debe seleccionar un tratamiento");
		}
	
		//Validamos que el array despues del filtrado no sea mayor a 2, osea validamos que maximo se puedan seleccionar dos tratamientos por turno
		if (tratamientosFiltradosList.size() > 2) {
			throw new MiExcepcion("Solo se pueden seleccionar máximo dos tratamientos por turno");
		}
		
		//Creamos la lista de tratamientos donde los vamos a guardar
		List<Tratamiento> listaTratamientos = new ArrayList<>();
		
		//Recorremos el array de string de tratamientos, los cuales son puros id
		for (String tratamientoId : tratamientosFiltradosList) {
			//con los id buscamos los tratamientos
			Optional <Tratamiento> trataminetosEncontrados = repositorioTratamiento.findById(tratamientoId);
			if (trataminetosEncontrados.isPresent()) {
				Tratamiento tratamientos = trataminetosEncontrados.get();
				tratamientos.getNombreTratamientos();
				tratamientos.getCostoTratamiento();
				//agregamos los tratamientos encontrados a la lista de tratamientos
				listaTratamientos.add(tratamientos);
			}
			
		}
		
		LocalDateTime fechaDeCreacionTurno = null;
		try {
			fechaDeCreacionTurno = LocalDateTime.now();
		} catch (Exception e) {
			System.out.println("No se puede obtener la fecha de creacion del turno");
		}
	
		//Funcion para pasar un fecha de tipo String a LocalDate
		LocalDate fechaUsuario = servicioHorario.pasarFechaStringToLocalDate(fechaSeleccionada);
		
		//Rodeamos con try para manejar si se produce un error cualquiera al intentar guardar los datos en la base
		try {
		//Buscamos el dni del cliente que esta seleccionando el turno para adjuntarlo al objeto turno que se va a guardar en la base de datos
		String dniCliente = null;
		Optional<Persona> dniUsuario = repositorioPersona.findById(idCliente);
		if (dniUsuario.isPresent()) {
			Persona usu = dniUsuario.get();
			dniCliente = usu.getDni();
		}
		
		Optional <Cliente> cliente = repositorioCliente.findClienteById(idCliente);
		Cliente datosDelCliente = null;
		if (cliente.isPresent()) {
			datosDelCliente = cliente.get();
		}
		
		// Creamos un nuevo objeto de tipo turno que es el que se va a guardar en la base de datos
		Turnos nuevoTurno = new Turnos();
		nuevoTurno.setProvincia(provinciaString);
		nuevoTurno.setProfesional(new Profesional(idProfesional));
		nuevoTurno.setCliente(datosDelCliente);
		nuevoTurno.setFecha(fechaUsuario);
		nuevoTurno.setFechaCreacion(fechaDeCreacionTurno);
		nuevoTurno.setHorario(horario);
		nuevoTurno.setEmail(email);
		nuevoTurno.setTratamientos(listaTratamientos);
		nuevoTurno.setDniCliente(dniCliente);
		nuevoTurno.setMulta(FALSE);
		nuevoTurno.setActivo(TRUE);
		nuevoTurno.setEstado(EstadoDelTurno.PENDIENTE);
		//la funcionalidad de jpa que perminte obtener los datos de un objeto recien guarado siempre y cuando este haya sido
		//guardado como una entidad nueva.
		Turnos turnoGuardado;
		turnoGuardado =repositorioTurnos.save(nuevoTurno); //Guardo el turno como una nueva entidad para porder aprovechar
		return turnoGuardado;

		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con el servidor, el turno no pudo ser generado " + e);
		}
		
	}
		

	public List<Turnos> obtenerTurnosPorProfesionalYFecha(Profesional profesional, LocalDate fecha) throws MiExcepcion {
		try {
			return repositorioTurnos.findByProfesionalAndFecha(profesional, fecha);
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con el servidor, no se lograron obtener los turnos"
					+ "por profesional y fecha " + e);
		}
			
	}
	
                        
    public void validarGuardarTurno(String provinciaString, String nombreDelProfesional, String fechaSeleccionada,
			 String horario, String tratamientosSeleccionados) throws MiExcepcion {
		
		if (provinciaString == null || provinciaString.isEmpty() || provinciaString == "") {
			throw new MiExcepcion("Debe seleccionar una provincia");
		}
		
		if (nombreDelProfesional == null || nombreDelProfesional.isEmpty() || nombreDelProfesional == "") {
			throw new MiExcepcion("Debe seleccionar un profesional");
		}
		
		if(fechaSeleccionada == null || fechaSeleccionada.isEmpty() || nombreDelProfesional == "") {
			throw new MiExcepcion("Debe seleccionar una fecha");
		}
		
		if(horario == null || horario.trim().isEmpty() || horario == " ") {
			throw new MiExcepcion("Debe seleccionar un horario");
		}
		
		if (tratamientosSeleccionados == null || tratamientosSeleccionados.isEmpty() || tratamientosSeleccionados == "" || tratamientosSeleccionados.equalsIgnoreCase("Seleccione tratamientos")) {
			throw new MiExcepcion("Debe seleccionar almenos un tratamiento");
		}
	}
    
    public void validarDatosFormularioTurno(String fuma, String drogas, String alcohol, String deportes, String ejercicios, 
			 String medicamentos, String nombreMedicamento,String embarazo, String amamantando, String ciclo_menstrual, String alteracion_hormonal,
				String vitaminas, String corticoides, String hormonas, String metodo_anticonceptivo, String sufre_enfermedad,
				String cual_enfermedad, String tiroides, String paciente_oncologica, String fractura_facial, String cirugia_estetica, 
				String indique_cirugia_estetica, String tiene_implantes, String marca_pasos, String horas_sueno, String exposicion_sol,
				String protector_solar, String reaplica_protector, String consumo_carbohidratos, String tratamientos_faciales_anteriores,
				String resultados_tratamiento_anterior, String cuidado_de_piel, String motivo_consulta) throws MiExcepcion {
		 
		 if (fuma == null || fuma.isEmpty() || fuma.trim().isEmpty() || fuma.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indicar si fuma");
		 
		 if (drogas == null || drogas.isEmpty() || drogas.trim().isEmpty() || drogas.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indicar si consume alguna droga");
		 
		 if (alcohol == null || alcohol.isEmpty() || alcohol.trim().isEmpty() || alcohol.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indicar su consumo aproximado de alcohol");
		 
		 if (deportes == null || deportes.isEmpty() || deportes.trim().isEmpty() || deportes.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indicar si realiza algun deporte");
		 
		 if (ejercicios == null || ejercicios.isEmpty() || ejercicios.trim().isEmpty() || ejercicios.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indicar si realiza algun ejercicio");
		 
		 if (medicamentos == null || medicamentos.isEmpty() || medicamentos.trim().isEmpty() || medicamentos.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indicar si toma algun medicamento");
		 
//		 if (nombreMedicamento == null || nombreMedicamento.isEmpty() || nombreMedicamento.trim().isEmpty() || nombreMedicamento.equals("Seleccione")) 
//			 throw new MiExcepcion("Por favor indicar el nombre del medicamento");
		 
		 if (embarazo == null || embarazo.isEmpty() || embarazo.trim().isEmpty() || embarazo.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indicar si esta embarazada");
		 
		 if (amamantando == null || amamantando.isEmpty() || amamantando.trim().isEmpty() || amamantando.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indicar si esta amamantando");
		 
		 if (ciclo_menstrual == null || ciclo_menstrual.isEmpty() || ciclo_menstrual.trim().isEmpty() || ciclo_menstrual.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indique como es su ciclo menstrual");
		 
		 if (alteracion_hormonal == null || alteracion_hormonal.isEmpty() || alteracion_hormonal.trim().isEmpty() || alteracion_hormonal.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indique si ha tenido o tiene alteraciones hormonales");
		 
		 if (vitaminas == null || vitaminas.isEmpty() || vitaminas.trim().isEmpty() || vitaminas.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indique si toma alguna vitamina");
		 
		 if (corticoides == null || corticoides.isEmpty() || corticoides.trim().isEmpty() || corticoides.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indique si toma corticoides");
		 
		 if (hormonas == null || hormonas.isEmpty() || hormonas.trim().isEmpty() || hormonas.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indique si toma hormonas");
		 
		 if (metodo_anticonceptivo == null || metodo_anticonceptivo.isEmpty() || metodo_anticonceptivo.trim().isEmpty() || metodo_anticonceptivo.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indique si utiliza algun metodo anticonceptivo");
		 
		 if (sufre_enfermedad == null || sufre_enfermedad.isEmpty() || sufre_enfermedad.trim().isEmpty()|| sufre_enfermedad.equals("Seleccione") ) 
			 throw new MiExcepcion("Por favor indique si sufre de alguna enfermedad");
		 
	/*	 if (cual_enfermedad == null || cual_enfermedad.isEmpty() || cual_enfermedad.trim().isEmpty()) 
			 throw new MiExcepcion("Por favor indicar cual enfermedad ha tenido"); */
		 
		 if (tiroides == null || tiroides.isEmpty() || tiroides.trim().isEmpty() || tiroides.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indique si padece de la tiroide");
		 
		 if (paciente_oncologica == null || paciente_oncologica.isEmpty() || paciente_oncologica.trim().isEmpty() || paciente_oncologica.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indique si es o fue paciente oncologico");
		 
		 if (fractura_facial == null || fractura_facial.isEmpty() || fractura_facial.trim().isEmpty() || fractura_facial.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indique si posee alguna fractura facial");
		 
		 if (cirugia_estetica == null || cirugia_estetica.isEmpty() || cirugia_estetica.trim().isEmpty() || cirugia_estetica.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indique si tiene alguna cirugia estetica");
		 
	/*	 if (indique_cirugia_estetica == null || indique_cirugia_estetica.isEmpty() || indique_cirugia_estetica.trim().isEmpty()) 
			 throw new MiExcepcion("Por favor indique cual cirugia estetica se realizo"); */
		 
		 if (tiene_implantes == null || tiene_implantes.isEmpty() || tiene_implantes.trim().isEmpty() || tiene_implantes.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indique si tiene algun implante");
		 
		 if (marca_pasos == null || marca_pasos.isEmpty() || marca_pasos.trim().isEmpty() || marca_pasos.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indique si tiene marca pasos");
		 
		 if (horas_sueno == null || horas_sueno.isEmpty() || horas_sueno.trim().isEmpty() || horas_sueno.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indique un aproximado de las horas de sueno");
		 
		 if (exposicion_sol == null || exposicion_sol.isEmpty() || exposicion_sol.trim().isEmpty() || exposicion_sol.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indique un aproximidado de las horas de exposicion al sol"); 
		 
		 if (protector_solar == null || protector_solar.isEmpty() || protector_solar.trim().isEmpty() || protector_solar.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indique si utiliza protector solar");
		 
		 if (reaplica_protector == null || reaplica_protector.isEmpty() || reaplica_protector.trim().isEmpty() || reaplica_protector.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indique si se reaplica el protector solar");
		 
		 if (consumo_carbohidratos == null || consumo_carbohidratos.isEmpty() || consumo_carbohidratos.trim().isEmpty() || consumo_carbohidratos.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indique el consumo aproximado de carbohidratos");
		 
		 if (tratamientos_faciales_anteriores == null || tratamientos_faciales_anteriores.isEmpty() || tratamientos_faciales_anteriores.trim().isEmpty() || tratamientos_faciales_anteriores.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indicar si se realizo tratamientos faciales anteriormente"); 
		 
		 if (resultados_tratamiento_anterior == null || resultados_tratamiento_anterior.isEmpty() || resultados_tratamiento_anterior.trim().isEmpty() || resultados_tratamiento_anterior.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indicar el resultado de los tratamientos faciales anteriores");
		 
		 if (cuidado_de_piel == null || cuidado_de_piel.isEmpty() || cuidado_de_piel.trim().isEmpty())  /*Este no lleva la validacion del Seleccione porque es un textarea*/
			 throw new MiExcepcion("Por favor indique como se cuida la piel");
		 
		 if (motivo_consulta == null || motivo_consulta.isEmpty() || motivo_consulta.trim().isEmpty() || motivo_consulta.equals("Seleccione")) 
			 throw new MiExcepcion("Por favor indicar el motivo de la consulta");
	 }
    
   
}
    
				 
				 
           

			
		
		
	
	
