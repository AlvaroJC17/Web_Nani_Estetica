package com.proyecto_integrador_3.Estetica.Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Tratamiento;
import com.proyecto_integrador_3.Estetica.Entidades.Turnos;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.DiasDeLaSemana;
import com.proyecto_integrador_3.Estetica.Enums.Especialidad;
import com.proyecto_integrador_3.Estetica.Enums.EstadoDelTurno;
import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.Enums.TipoDeEspecialidad;
import com.proyecto_integrador_3.Estetica.Enums.TratamientoEnum;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioProfesional;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCliente;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioHorario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioPersona;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioProfesional;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioTratamiento;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioTurnos;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

@Controller
public class ControladorProfesional {
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	@Autowired
	public ServicioProfesional servicioProfesional;
	
	@Autowired
	public ServicioCliente servicioCliente;
	
	@Autowired
	public ServicioTurnos servicioTurnos;
	
	@Autowired
	public ServicioHorario servicioHorario;
	
	@Autowired
	public ServicioTratamiento servicioTratamiento;
	
	@Autowired
	public ServicioPersona servicioPersona;
	
	@Autowired
	public RepositorioProfesional repositorioProfesional;
	
	@Autowired
	public RepositorioUsuario repositorioUsuario;
	
	@Autowired
	public RepositorioCliente repositorioCliente;
	
	
	@PostMapping("/actualizarTratamientos")
	public String actualizarTratamientos( 
			@RequestParam String idProfesional,
			@RequestParam String emailProfesional,
			@RequestParam String tratamientosSeleccionadosCampo,
			Model model) throws MiExcepcion {
		
		try {
			servicioProfesional.actualizarTratamientos(idProfesional, tratamientosSeleccionadosCampo);
			String exito = "Los datos fueron modificados exitosamente";
			model.addAttribute("exito", exito);
			model.addAttribute("showModalExito", true);
		} catch (Exception e) {
			String error = e.getMessage();
			model.addAttribute("error", error);
			model.addAttribute("showModalError", true);
		}
		
		//Buscamos los datos del profesional
		Profesional profesional = servicioProfesional.obetenerDatosProfesional(idProfesional);
		
		//Filtramos la lista de enum por el tipo de especialidad que selecciono el profesional
        List<TratamientoEnum> tratamientosCompletos = Arrays.stream(TratamientoEnum.values())
                .filter(especialidadList -> especialidadList.name().contains(profesional.getTipoEspecialidad().getDisplayName().toUpperCase()))
                .collect(Collectors.toList());
        
        List<String> horariosOrdenados = profesional.getHorariosLaborales();
		Collections.sort(horariosOrdenados); //ordenamos la lista de menor a mayor
        
        List<DiasDeLaSemana> listaDeDiasOrdenada = profesional.getDiasDeLaSemana();
		Collections.sort(listaDeDiasOrdenada); //Ordenamos la lista de lunes a viernes
        
        //Buscamos solo los tratamientos marcados como actuales en la base de datos, los actuales son los que tienen las ultimas modificaciones de costo
        List<Tratamiento> listaTratamientos = servicioTratamiento.tratamientosActuales(idProfesional);
		
		//Buscamos los datos del profesional para pasarlos a la vista
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(emailProfesional);
		
		model.addAttribute("datosProfesional", datosProfesional);
		model.addAttribute("diasLaborales", listaDeDiasOrdenada);
		model.addAttribute("especialidad", profesional.getEspecialidad());
		model.addAttribute("horariosDisponibles", profesional.getHorariosDisponibles());
		model.addAttribute("horariosLaborales", horariosOrdenados);
		model.addAttribute("tipoDeEspecialidad", profesional.getTipoEspecialidad());
		model.addAttribute("tratamientos", listaTratamientos);
		model.addAttribute("tratamientosFiltrados", tratamientosCompletos);
		model.addAttribute("diasDeLaSemana", DiasDeLaSemana.values());
		return "/pagina_profesional/datosProfesional";
	}
	
	@PostMapping("/actualizarHorarioLaboral")
	public String actualizarHorarioLaboral( 
			@RequestParam String idProfesional,
			@RequestParam String emailProfesional,
			@RequestParam String horasSeleccionados,
			Model model) throws MiExcepcion {
		
		try {
			servicioProfesional.actualizarHorasLaborales(idProfesional, horasSeleccionados);
			String exito = "Los datos fueron modificados exitosamente";
			model.addAttribute("exito", exito);
			model.addAttribute("showModalExito", true);
		} catch (Exception e) {
			String error = e.getMessage();
			model.addAttribute("error", error);
			model.addAttribute("showModalError", true);
		}
		
		//Buscamos los datos del profesional
		Profesional profesional = servicioProfesional.obetenerDatosProfesional(idProfesional);
		
		//Filtramos la lista de enum por el tipo de especialidad que selecciono el profesional
		//Esta lista es la que brinda las opciones en el select para modficiar los tratamientos existentes
		List<TratamientoEnum> tratamientosCompletos = Arrays.stream(TratamientoEnum.values())
				.filter(especialidadList -> especialidadList.name().contains(profesional.getTipoEspecialidad().getDisplayName().toUpperCase()))
				.collect(Collectors.toList());
		
		List<String> horariosOrdenados = profesional.getHorariosLaborales();
		Collections.sort(horariosOrdenados); //ordenamos la lista de menor a mayor
		
		List<DiasDeLaSemana> listaDeDiasOrdenada = profesional.getDiasDeLaSemana();
		Collections.sort(listaDeDiasOrdenada); //Ordenamos la lista de lunes a viernes
		
		//Buscamos los datos del profesional para pasarlos a la vista
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(emailProfesional);
		
		//Buscamos solo los tratamientos marcados como actuales en la base de datos, los actuales son los que tienen las ultimas modificaciones de costo
        List<Tratamiento> listaTratamientos = servicioTratamiento.tratamientosActuales(idProfesional);
				
		
		model.addAttribute("datosProfesional", datosProfesional);
		model.addAttribute("diasLaborales", listaDeDiasOrdenada);
		model.addAttribute("especialidad", profesional.getEspecialidad());
		model.addAttribute("horariosDisponibles", profesional.getHorariosDisponibles());
		model.addAttribute("horariosLaborales", horariosOrdenados);
		model.addAttribute("tipoDeEspecialidad", profesional.getTipoEspecialidad());
		model.addAttribute("tratamientosFiltrados", tratamientosCompletos);
		model.addAttribute("tratamientos", listaTratamientos);
		model.addAttribute("diasDeLaSemana", DiasDeLaSemana.values());
		return "/pagina_profesional/datosProfesional";
	}
	
	@PostMapping("/actualizarDiaLaboral")
	public String actualizarDiaLaboral( 
			@RequestParam String idProfesional,
			@RequestParam String emailProfesional,
			@RequestParam String diasSeleccionados,
			Model model) throws MiExcepcion {
		
		try {
			servicioProfesional.actualizarDiasLaborales(idProfesional, diasSeleccionados);
			String exito = "Los datos fueron modificados exitosamente";
			model.addAttribute("exito", exito);
			model.addAttribute("showModalExito", true);
		} catch (Exception e) {
			String error = e.getMessage();
			model.addAttribute("error", error);
			model.addAttribute("showModalError", true);
		}
		
		//Buscamos los datos del profesional
		Profesional profesional = servicioProfesional.obetenerDatosProfesional(idProfesional);
		
		//Buscamos los datos del profesional para pasarlos a la vista
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(emailProfesional);
		
		//Filtramos la lista de enum por el tipo de especialidad que selecciono el profesional
		//Esta lista es la que brinda las opciones en el select para modficiar los tratamientos existentes
		List<TratamientoEnum> tratamientosCompletos = Arrays.stream(TratamientoEnum.values())
				.filter(especialidadList -> especialidadList.name().contains(profesional.getTipoEspecialidad().getDisplayName().toUpperCase()))
				.collect(Collectors.toList());
		
		List<String> horariosOrdenados = profesional.getHorariosLaborales();
		Collections.sort(horariosOrdenados); //ordenamos la lista de menor a mayor
		
		List<DiasDeLaSemana> listaDeDiasOrdenada = profesional.getDiasDeLaSemana();
		Collections.sort(listaDeDiasOrdenada); //Ordenamos la lista de lunes a viernes
		
		//Buscamos solo los tratamientos marcados como actuales en la base de datos, los actuales son los que tienen las ultimas modificaciones de costo
        List<Tratamiento> listaTratamientos = servicioTratamiento.tratamientosActuales(idProfesional);
				
		model.addAttribute("datosProfesional", datosProfesional);
		model.addAttribute("diasLaborales", listaDeDiasOrdenada);
		model.addAttribute("especialidad", profesional.getEspecialidad());
		model.addAttribute("horariosDisponibles", profesional.getHorariosDisponibles());
		model.addAttribute("horariosLaborales", horariosOrdenados);
		model.addAttribute("tipoDeEspecialidad", profesional.getTipoEspecialidad());
		model.addAttribute("tratamientosFiltrados", tratamientosCompletos);
		model.addAttribute("tratamientos", listaTratamientos);
		model.addAttribute("diasDeLaSemana", DiasDeLaSemana.values());
		return "/pagina_profesional/datosProfesional";
			
	}
	
	

	@GetMapping("/datosProfesional")
	public String datosProfesional(
			@RequestParam String email,
			@RequestParam String idProfesional,
			Model model) throws MiExcepcion {
		
		//Buscamos los datos del profesional
		Profesional profesional = servicioProfesional.obetenerDatosProfesional(idProfesional);
		
		//Guardamos los horarios laborales en una nueva lista
		List<String> horariosOrdenados = profesional.getHorariosLaborales();
		Collections.sort(horariosOrdenados); //Ordenamos la lista de menor a mayor
		
		List<DiasDeLaSemana> listaDeDiasOrdenada = profesional.getDiasDeLaSemana();
		Collections.sort(listaDeDiasOrdenada); //Ordenamos la lista de lunes a viernes
		
		//Filtramos la lista de enum por el tipo de especialidad que selecciono el profesional
		//Esta lista es la que brinda las opciones en el select para modficiar los tratamientos existentes
        List<TratamientoEnum> tratamientosCompletos = Arrays.stream(TratamientoEnum.values())
                .filter(especialidadList -> especialidadList.name().contains(profesional.getTipoEspecialidad().getDisplayName().toUpperCase()))
                .collect(Collectors.toList());
        
        //Buscamos solo los tratamientos marcados como actuales en la base de datos, los actuales son los que tienen las ultimas modificaciones de costo
        List<Tratamiento> listaTratamientos = servicioTratamiento.tratamientosActuales(idProfesional);
        
		
		//Pasamos los datos del profesional a la vista
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(email);
				
		model.addAttribute("datosProfesional", datosProfesional);
		model.addAttribute("diasLaborales", listaDeDiasOrdenada);
		model.addAttribute("especialidad", profesional.getEspecialidad());
		model.addAttribute("horariosDisponibles", profesional.getHorariosDisponibles());
		model.addAttribute("horariosLaborales", horariosOrdenados);
		model.addAttribute("tipoDeEspecialidad", profesional.getTipoEspecialidad());
		model.addAttribute("tratamientos", listaTratamientos);
		model.addAttribute("tratamientosFiltrados", tratamientosCompletos);
		model.addAttribute("diasDeLaSemana", DiasDeLaSemana.values());
		return "/pagina_profesional/datosProfesional";
	}
	
	
	//Metodo para guardar y modificar los datos del perfil del cliente, como el formulario de datos y las recomendaciones del turno
	@PostMapping("/guardarDatosPacienteEditado")
	public String guardarDatosPacienteEditado(
			@RequestParam String idCliente,
			@RequestParam String idProfesional,
			@RequestParam String emailCliente,
			@RequestParam String emailProfesional,
			@RequestParam String funcionalidadBotones,
			@RequestParam(required = false) String idTurnoModificado,
			@RequestParam(required = false) String fuma,
			@RequestParam(required = false) String drogas,
			@RequestParam(required = false) String alcohol,
			@RequestParam(required = false) String deportes,
			@RequestParam(required = false) String ejercicios,
			@RequestParam(required = false) String medicamentos,
			@RequestParam(required = false) String nombreMedicamento,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String embarazo,
			@RequestParam(required = false) String amamantando,
			@RequestParam(required = false) String ciclo_menstrual,
			@RequestParam(required = false) String alteracion_hormonal,
			@RequestParam(required = false) String vitaminas,
			@RequestParam(required = false) String corticoides,
			@RequestParam(required = false) String hormonas,
			@RequestParam(required = false) String metodo_anticonceptivo,
			@RequestParam(required = false) String sufre_enfermedad,
			@RequestParam(required = false) String cual_enfermedad,
			@RequestParam(required = false) String tiroides,
			@RequestParam(required = false) String paciente_oncologica,
			@RequestParam(required = false) String fractura_facial,
			@RequestParam(required = false) String cirugia_estetica,
			@RequestParam(required = false) String indique_cirugia_estetica,
			@RequestParam(required = false) String tiene_implantes,
			@RequestParam(required = false) String marca_pasos,
			@RequestParam(required = false) String horas_sueno,
			@RequestParam(required = false) String exposicion_sol,
			@RequestParam(required = false) String protector_solar,
			@RequestParam(required = false) String reaplica_protector,
			@RequestParam(required = false) String consumo_carbohidratos,
			@RequestParam(required = false, name="tratamientosFacialesAnteriores") String tratamientos_faciales_anteriores,
			@RequestParam(required = false) String resultados_tratamiento_anterior,
			@RequestParam(required = false) String cuidado_de_piel,
			@RequestParam(required = false) String motivo_consulta,
			@RequestParam(required = false) String notas_profesional,
			@RequestParam(required = false) String recomendaciones,
			Model modelo) throws MiExcepcion {
		
			
			Boolean esEdicion = true; //con este boolean le indicamos al servicio que es para editar un formulario ya registrado
			String exito =  null;
		
			try {
				
			if (funcionalidadBotones.equalsIgnoreCase("botonGuardarFormulario")) {
				
				//Servicio para guardar un formulario nuevo y actualizar uno ya existente
				servicioTurnos.formularioTurnos(idCliente, email, fuma, drogas, alcohol, deportes,
				ejercicios, medicamentos, nombreMedicamento, embarazo, amamantando, ciclo_menstrual,
				alteracion_hormonal, vitaminas, corticoides, hormonas, metodo_anticonceptivo,
				sufre_enfermedad, cual_enfermedad, tiroides, paciente_oncologica, fractura_facial,
				cirugia_estetica, indique_cirugia_estetica, tiene_implantes, marca_pasos, horas_sueno,
				exposicion_sol, protector_solar, reaplica_protector, consumo_carbohidratos,
				tratamientos_faciales_anteriores, resultados_tratamiento_anterior, cuidado_de_piel,
				motivo_consulta, esEdicion);
				
				exito = "Los datos fueron modificados exitosamente";
				modelo.addAttribute("exito", exito);
				modelo.addAttribute("showModalExito", true);
				
			}else if(funcionalidadBotones.equalsIgnoreCase("botonGuardarRecomendaciones")) {
				
				//Servicio para agregar o editar una recomendacion de un profesional a un turno
				servicioTurnos.guardarModificarConsultaTurno(idTurnoModificado, recomendaciones);
				
				exito = "Los datos fueron modificados exitosamente";
				modelo.addAttribute("exito", exito);
				modelo.addAttribute("showModalExito", true);
				
			}else if(funcionalidadBotones.equalsIgnoreCase("botonGuardarNotas")) {
				
				System.out.println("Notas del profesional: " + notas_profesional);
				
				//servicio para guardar las notas de un profesional en el perfil del cliente
				servicioProfesional.guardarNotasProfesional(idCliente, notas_profesional);
				
				exito = "Los datos fueron modificados exitosamente";
				modelo.addAttribute("exito", exito);
				modelo.addAttribute("showModalExito", true);
			}
			
			List<Turnos> ultimosTurnosCliente = servicioTurnos.buscarTurnosPorProfesionalAndClienteAndEstadoDelTurno(idProfesional, idCliente, EstadoDelTurno.ASISTIDO);
			List<Turnos> tresUltimosTurnos = new ArrayList<>();
			int size = ultimosTurnosCliente.size();
			if (size >= 3) {
			    tresUltimosTurnos = ultimosTurnosCliente.subList(size - 3, size);
			} else {
			    tresUltimosTurnos = ultimosTurnosCliente; // Si hay menos de 3 elementos, tomamos todos
			}
			
			//Buscamos los si el fomrulario de datos fue completado por el cliente
			Boolean isEditarDisabled = null;
			Optional <Cliente> buscarDatosCliente = repositorioCliente.findById(idCliente);
			if (buscarDatosCliente.isPresent()) {
				Cliente datosCliente = buscarDatosCliente.get();
				isEditarDisabled = datosCliente.getFomularioDatos();
			}
			
			//Si el valor del formulario es null entonces que le coloque un valor de false
			if (isEditarDisabled == null) {
				isEditarDisabled = false;
			}
			
			
			Boolean cienteSinTurnos = false;
			if (tresUltimosTurnos.isEmpty()) {
				cienteSinTurnos = true; 
			}
			
			List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(emailProfesional);
			List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailCliente);
			List<Cliente> datosPaciente = servicioCliente.buscarPacientePorId(idCliente);
			modelo.addAttribute("isEditarDisabled", isEditarDisabled);
			modelo.addAttribute("datosProfesional", datosProfesional); //datos para el menu de la pagina
			modelo.addAttribute("datosCliente", datosCliente); // datos para la seccion de datos personales
			modelo.addAttribute("datosPaciente", datosPaciente); // datos para la seccion del formulario y nota del paciente
			modelo.addAttribute("ultimosTurnos", tresUltimosTurnos); // mandamos los ultimos tres turnos asistidos a la vista
			modelo.addAttribute("cienteSinTurnos", cienteSinTurnos);
				
			//Cuando todo sale bien retornamos a esta vista con todos los datos de arriba
			return "/pagina_profesional/datosPersonalesPaciente";
			
			} catch (Exception e) {
				String error = e.getMessage();
				modelo.addAttribute("error", error);
				modelo.addAttribute("showModalError", true);
				return "/pagina_profesional/datosPersonalesPaciente";
			}
	}
			
	
	
	@PostMapping("/buscarDatosPersonalesPaciente")
	public String buscarDatosPersonalesPaciente(
			@RequestParam(required = false) String emailProfesional,
			@RequestParam(required = false) String idCliente,
			@RequestParam String idProfesional,
			@RequestParam(required = false) String dato,
			Model modelo) throws MiExcepcion {

		
		//Datos del profesional para los menu de la pagina
				List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(emailProfesional);
				
		//si el profesional le da al boton ver perfil sin seleccionar un paciente, entra en este codigo
		//para enviarle un mensaje de error pero siempre mostrandole el resultado de la busqueda previa que
		//realizo. La variable dato es la misma del metodo /buscadorPaciente
		if (idCliente == null || idCliente.isEmpty()) {
			String datoSinEspacios = dato.trim();
			List<Persona> pacienteDni = servicioPersona.buscarPacienteByRolAndDni(datoSinEspacios, Rol.CLIENTE);
			List<Persona> pacienteNombre = servicioPersona.buscarPacienteByRolAndNombre(datoSinEspacios, Rol.CLIENTE);
			List<Persona> pacienteEmail = servicioPersona.buscarPacienteByRolAndEmail2(datoSinEspacios, Rol.CLIENTE);
			List<Persona> datosPaciente = null;
			// En este codigo buscamos asignarle el valor de la lista que no venga vacia a una variable general llamada datosPaciente
			if (!pacienteDni.isEmpty()) {
				datosPaciente = pacienteDni;
			}else if(!pacienteNombre.isEmpty()) {
				datosPaciente = pacienteNombre;
			}else if(!pacienteEmail.isEmpty()) {
				datosPaciente = pacienteEmail;
			}
			String error = "Debe seleccionar un paciente";
			modelo.addAttribute("datosProfesional",datosProfesional);
			modelo.addAttribute("usuarios", datosPaciente);
			modelo.addAttribute("showModalError", true);
			modelo.addAttribute("error", error);
			modelo.addAttribute("dato", dato);
			return "/pagina_profesional/buscadorPaciente";
		}
			
		//Buscamos el email del cliente/usuario con el id
		String emailCliente = null;
		Optional <Usuario> buscarEmailCliente = repositorioUsuario.buscarPorIdOptional(idCliente);
		if (buscarEmailCliente.isPresent()) {
			Usuario emailUsuario = buscarEmailCliente.get();
			emailCliente = emailUsuario.getEmail();
		}
		
		//Buscamos los si el fomrulario de datos fue completado por el cliente
		Boolean isEditarDisabled = null;
		Optional <Cliente> buscarDatosCliente = repositorioCliente.findById(idCliente);
		if (buscarDatosCliente.isPresent()) {
			Cliente datosCliente = buscarDatosCliente.get();
			isEditarDisabled = datosCliente.getFomularioDatos();
		}
		
		//Si el valor del formulario es null entonces que le coloque un valor de false
		if (isEditarDisabled == null) {
			isEditarDisabled = false;
		}
		
		
		List<Turnos> ultimosTurnosCliente = servicioTurnos.buscarTurnosPorProfesionalAndClienteAndEstadoDelTurno(idProfesional, idCliente, EstadoDelTurno.ASISTIDO);
		
		List<Turnos> tresUltimosTurnos = new ArrayList<>();
		int size = ultimosTurnosCliente.size();
		if (size >= 3) {
		    tresUltimosTurnos = ultimosTurnosCliente.subList(size - 3, size);
		} else {
		    tresUltimosTurnos = ultimosTurnosCliente; // Si hay menos de 3 elementos, tomamos todos
		}
		
		//Verificamos que el cliente tengo por lo menos un turno activo, sino usamos el boolean en la vista para mostrar un mensaje
		Boolean cienteSinTurnos = false;
		if (tresUltimosTurnos.isEmpty()) {
			cienteSinTurnos = true; 
		}
	
		//Datos del cliente para los datos personales
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailCliente);
		
		//datos del cliente/paciente para las respuestas del formulario
		List<Cliente> datosPaciente = servicioCliente.buscarPacientePorId(idCliente);
		
		modelo.addAttribute("datosProfesional", datosProfesional); //datos para el menu de la pagina
		modelo.addAttribute("datosCliente", datosCliente); // datos para la seccion de datos personales
		modelo.addAttribute("datosPaciente", datosPaciente); // datos para la seccion del formulario y nota del paciente
		modelo.addAttribute("ultimosTurnos", tresUltimosTurnos); // mandamos los ultimos tres turnos asistidos a la vista
		modelo.addAttribute("isEditarDisabled", isEditarDisabled);
		modelo.addAttribute("cienteSinTurnos", cienteSinTurnos);
		return "/pagina_profesional/datosPersonalesPaciente";
	}

	
	

	@GetMapping("/listarPacientesOcultos")
    public String listarUsuarios(
    		@RequestParam String email, //Esta variable proviene de homeAdmin
    		Model model) throws MiExcepcion {

		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosProfesional", datosProfesional);
		return "/pagina_profesional/buscadorPaciente";
	}
	
	@PostMapping("/buscadorPacientes")
	public String buscadorPacientes(
			@RequestParam(required = false) String dato,  //la variable dato puede ser un nombre, dni o mail
			@RequestParam String email,
			Model model) throws MiExcepcion {
		
							
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(email);
		String error = null;
		String datoSinEspacios = dato.trim(); //Le quitamos los espacios en blanco al principio y final de la palabra

		//Validamos que la busqueda no se haya hecho en blanco y mostramos un mensaje
		if (datoSinEspacios.isEmpty() || Objects.equals(datoSinEspacios, null)) {
			error = "Indique un numero de DNI, nombre o email para realizar la busqueda";
			model.addAttribute("datosProfesional", datosProfesional);
			model.addAttribute("error", error);
			model.addAttribute("showModalError", true);
			return "/pagina_profesional/buscadorPaciente";
		}
		
		//Validamos que el usuario buscado solo por dni o emial exista en la base de datos, sino mandamos un mensaje de error.
		//Solo validamos email y dni porque si resultado siempre va a ser un unico registro y el metodo isPresent solo acepta un solo registro como resultado
		if (!servicioUsuario.buscarPorDniOptional(datoSinEspacios).isPresent() &&  !servicioUsuario.buscarPorEmailOptional(datoSinEspacios).isPresent() && servicioUsuario.buscarNombre(datoSinEspacios).isEmpty()) {
			error = "Usuario no se encuentra registrado";
			model.addAttribute("datosProfesional", datosProfesional);
				model.addAttribute("error", error);
				model.addAttribute("showModalError", true);
				return "/pagina_profesional/buscadorPaciente";
		}
		// Si cumple las condiciones para pasar los condicionales de arriba, entonces
		//usamos el valor de dato, segun corresponda
		List<Persona> pacienteDni = servicioPersona.buscarPacienteByRolAndDni(datoSinEspacios, Rol.CLIENTE);
		List<Persona> pacienteNombre = servicioPersona.buscarPacienteByRolAndNombre(datoSinEspacios, Rol.CLIENTE);
		List<Persona> pacienteEmail = servicioPersona.buscarPacienteByRolAndEmail2(datoSinEspacios, Rol.CLIENTE);
		
		//Validamos que alguna de las listas de arriba encontro un resultado, esto implica que si
		//encontro resultado entonces el usuario que encontro tiene rol de cliente
		//si el cliente buscado no existe o no tiene rol de cliente entonces las listas estaran vacias
		// y con esta validacion mandamos un mensaje de error al usuario profesional
		if (pacienteDni.isEmpty() && pacienteNombre.isEmpty() && pacienteEmail.isEmpty()) {
			error = "Los datos ingresador no pertenecen a un paciente o el paciente no se encuentra registrado";
			model.addAttribute("datosProfesional", datosProfesional);
			model.addAttribute("error", error);
			model.addAttribute("showModalError", true);
			return "/pagina_profesional/buscadorPaciente";
		}
		
		//Asignamos el valor de la lista que no venga vacia a la variable datosPaciente
		List<Persona> datosPaciente = null;
		if (!pacienteDni.isEmpty()) {
			datosPaciente = pacienteDni;
		}else if(!pacienteNombre.isEmpty()) {
			datosPaciente = pacienteNombre;
		}else if(!pacienteEmail.isEmpty()) {
			datosPaciente = pacienteEmail;
		}
		model.addAttribute("usuarios", datosPaciente); // asignamos el valor de la variable administradoresDni a la variable html administradores y asi poder iterarla en el documento
		model.addAttribute("datosProfesional", datosProfesional);
		model.addAttribute("dato", dato); // le pasamos el valor de dato a la variable dato del formulario modificarUsuario, para despues usarla en el metodo mensajeErrorNoId
		return "/pagina_profesional/buscadorPaciente";
	}
	
		
		
	@GetMapping("/homeProfesional")
	public String homeProfesional(@RequestParam String email, Model model) {
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosProfesional", datosProfesional);
		return "/pagina_profesional/homeProfesional";	
	}

	@GetMapping("/misdatosProfesional")
	public String misdatosProfesional(
			@RequestParam String email,
			ModelMap model) {
		
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(email);
		LocalDate fechaNacimiento = null;
		
		for (Usuario profesional : datosProfesional) {
			fechaNacimiento = profesional.getFechaNacimiento();
		}
	
		model.addAttribute("datosProfesional", datosProfesional);
		model.addAttribute("fechaNacimientoLocalDate", fechaNacimiento);
		model.addAttribute("sexos", Sexo.values());
		return "/pagina_profesional/misdatosProfesional";	
	}
	
	
	@GetMapping("/datosBasicosProfesional")
	public String datosBasicosProfesional(
			@RequestParam String matricula,
			@RequestParam String direccion,
			@RequestParam String sexo,
			@RequestParam String telefono,
			@RequestParam String provincia,
			@RequestParam String emailUsuario,
			Model model) throws MiExcepcion {
		
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(emailUsuario);
		model.addAttribute("matricula", matricula);
		model.addAttribute("sexo", sexo);
		model.addAttribute("telefono", telefono);
		model.addAttribute("provincia", provincia);
		model.addAttribute("direccion", direccion);
		model.addAttribute("emailUsuario", emailUsuario);
		model.addAttribute("especialidad", Especialidad.values());
		model.addAttribute("DiasDeLaSemana", DiasDeLaSemana.values());
		model.addAttribute("datosProfesional", datosProfesional);
			
		try {
			servicioProfesional.validarDatosProfesional(matricula, sexo, telefono, provincia, direccion);
		} catch (MiExcepcion e) {
			String error = e.getMessage();
			model.addAttribute("error", error);
			model.addAttribute("showModalError", true);
			model.addAttribute("provincias", Provincias.values());
			return "pagina_profesional/completarDatosProfesional";
		}
		
		return "pagina_profesional/completarEspecialidadesAndTratamientoProfesional";
	}
		
	
	@GetMapping("/mostrarTipoEspecialidad")
	public String mostrarTipoEspecialidad(
			@RequestParam String especialidad,
			@RequestParam String matricula,
			@RequestParam String direccion,
			@RequestParam String sexo,
			@RequestParam String telefono,
			@RequestParam String provincia,
			@RequestParam String emailUsuario,
			Model model) throws MiExcepcion{
		
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(emailUsuario);
		model.addAttribute("matricula", matricula);
		model.addAttribute("sexo", sexo);
		model.addAttribute("telefono", telefono);
		model.addAttribute("provincia", provincia);
		model.addAttribute("direccion", direccion);
		model.addAttribute("emailUsuario", emailUsuario);
		model.addAttribute("especialidad", Especialidad.values());
		model.addAttribute("DiasDeLaSemana", DiasDeLaSemana.values());
		model.addAttribute("datosProfesional", datosProfesional);
		
		Especialidad especialidadSeleccionada = Especialidad.valueOf(especialidad);
		
		try {
			switch (especialidad) {
			
			case "FACIAL":
				
				  // Filtrar las especialidades que contienen "FACIAL" en su nombre y guardarlas en una lista
		        List<TipoDeEspecialidad> especialidadesFaciales = Arrays.stream(TipoDeEspecialidad.values())
		                .filter(especialidadList -> especialidadList.name().contains("FACIAL"))
		                .collect(Collectors.toList());

		        model.addAttribute("tipoEspecialidad", especialidadesFaciales);
		        model.addAttribute("especialidadSeleccionada", especialidadSeleccionada.getDisplayName());
		        model.addAttribute("especialidadModoEnum", especialidadSeleccionada);
		        return "pagina_profesional/completarEspecialidadesAndTratamientoProfesional";
		        
			case "CORPORAL":
				
				  // Filtrar las especialidades que contienen "CORPORAL" en su nombre y guardarlas en una lista
		        List<TipoDeEspecialidad> especialidadesCorporales = Arrays.stream(TipoDeEspecialidad.values())
		                .filter(especialidadList -> especialidadList.name().contains("CORPORAL"))
		                .collect(Collectors.toList());

		        model.addAttribute("tipoEspecialidad", especialidadesCorporales);
		        model.addAttribute("especialidadSeleccionada", especialidadSeleccionada.getDisplayName());
		        model.addAttribute("especialidadModoEnum", especialidadSeleccionada);
		        return "pagina_profesional/completarEspecialidadesAndTratamientoProfesional";
		        
			case "ESTETICO":
				
				 // Filtrar las especialidades que contienen "FACIAL" en su nombre y guardarlas en una lista
		        List<TipoDeEspecialidad> especialidadesEstetica = Arrays.stream(TipoDeEspecialidad.values())
		                .filter(especialidadList -> especialidadList.name().contains("ESTETICO"))
		                .collect(Collectors.toList());

		        model.addAttribute("tipoEspecialidad", especialidadesEstetica);
		        model.addAttribute("especialidadSeleccionada", especialidadSeleccionada.getDisplayName());
		        model.addAttribute("especialidadModoEnum", especialidadSeleccionada);
		        return "pagina_profesional/completarEspecialidadesAndTratamientoProfesional";
			}
		        
		} catch (Exception e) {
			String error = "Error al seleccionar una especialidad";
			model.addAttribute("showModalError", true);
			model.addAttribute("error", error);
			return "pagina_profesional/completarEspecialidadesAndTratamientoProfesional";
		}
		
		
		return "";	
	}
	
	@GetMapping("/mostrarTratamientos")
	public String mostrarTratamientos(
			@RequestParam String tipoEspecialidad,
			@RequestParam String especialidadModoEnum,
			@RequestParam String matricula,
			@RequestParam String direccion,
			@RequestParam String sexo,
			@RequestParam String telefono,
			@RequestParam String provincia,
			@RequestParam String emailUsuario,
			Model model) throws MiExcepcion {
		
		Especialidad nuevaSeleccionEspecialidad = Especialidad.valueOf(especialidadModoEnum);
		TipoDeEspecialidad tipoEspecialidadSeleccionada = TipoDeEspecialidad.valueOf(tipoEspecialidad);  
		
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(emailUsuario);
		model.addAttribute("matricula", matricula);
		model.addAttribute("sexo", sexo);
		model.addAttribute("telefono", telefono);
		model.addAttribute("provincia", provincia);
		model.addAttribute("direccion", direccion);
		model.addAttribute("emailUsuario", emailUsuario);
		model.addAttribute("especialidad", Especialidad.values());
		model.addAttribute("DiasDeLaSemana", DiasDeLaSemana.values());
		model.addAttribute("especialidadModoEnum", especialidadModoEnum);
		model.addAttribute("datosProfesional", datosProfesional);
		
		if (tipoEspecialidadSeleccionada.getDisplayName().equalsIgnoreCase("Cosmetologa")) {
			
			// Filtrar los tratamientos que contienen "COSMETOLOGA" en su nombre y guardarlas en una lista
		    List<TratamientoEnum> tratamientosCosmetologa = Arrays.stream(TratamientoEnum.values())
		            .filter(especialidadList -> especialidadList.name().contains("COSMETOLOGA"))
		            .collect(Collectors.toList());
		    
		    // Filtrar las especialidades que contienen "FACIAL" en su nombre y guardarlas en una lista
	        List<TipoDeEspecialidad> especialidadesFaciales = Arrays.stream(TipoDeEspecialidad.values())
	                .filter(especialidadList -> especialidadList.name().contains("FACIAL"))
	                .collect(Collectors.toList());

	        //Pasamos las dos listas a la vista
	        model.addAttribute("tipoEspecialidad", especialidadesFaciales);
		    model.addAttribute("tratamiento", tratamientosCosmetologa);
		    model.addAttribute("especialidadSeleccionada", nuevaSeleccionEspecialidad.getDisplayName());
		    model.addAttribute("tipoEspecialidadSeleccionada", tipoEspecialidadSeleccionada.getDisplayName());
		    model.addAttribute("tipoEspecialidadModoEnum", tipoEspecialidadSeleccionada);
		    return "pagina_profesional/completarEspecialidadesAndTratamientoProfesional";
			
		}else if(tipoEspecialidadSeleccionada.getDisplayName().equalsIgnoreCase("Lashista")){
			
			// Filtrar los tratamientos que contienen "LASHISTA" en su nombre y guardarlas en una lista
		    List<TratamientoEnum> tratamientosLashista = Arrays.stream(TratamientoEnum.values())
		            .filter(especialidadList -> especialidadList.name().contains("LASHISTA"))
		            .collect(Collectors.toList());
		    
		    // Filtrar las especialidades que contienen "ESTETICO" en su nombre y guardarlas en una lista
	        List<TipoDeEspecialidad> especialidadesEstetica = Arrays.stream(TipoDeEspecialidad.values())
	                .filter(especialidadList -> especialidadList.name().contains("ESTETICO"))
	                .collect(Collectors.toList());

	        //pasamos ambas lista a la vista
		    model.addAttribute("tratamiento", tratamientosLashista);
		    model.addAttribute("tipoEspecialidad", especialidadesEstetica);
		    model.addAttribute("especialidadSeleccionada", nuevaSeleccionEspecialidad.getDisplayName());
		    model.addAttribute("tipoEspecialidadSeleccionada", tipoEspecialidadSeleccionada.getDisplayName());
		    model.addAttribute("tipoEspecialidadModoEnum", tipoEspecialidadSeleccionada);
		    return "pagina_profesional/completarEspecialidadesAndTratamientoProfesional";
			
			
		}else if(tipoEspecialidadSeleccionada.getDisplayName().equalsIgnoreCase("Masajista")) {
			
			// Filtrar los tratamientos que contienen "MASAJE" en su nombre y guardarlas en una lista
		    List<TratamientoEnum> tratamientosMasajista = Arrays.stream(TratamientoEnum.values())
		            .filter(especialidadList -> especialidadList.name().contains("MASAJE"))
		            .collect(Collectors.toList());
		    
		    // Filtrar las especialidades que contienen "CORPORAL" en su nombre y guardarlas en una lista
	        List<TipoDeEspecialidad> especialidadesCorporales = Arrays.stream(TipoDeEspecialidad.values())
	                .filter(especialidadList -> especialidadList.name().contains("CORPORAL"))
	                .collect(Collectors.toList());

	        //pasamos ambas listas a la vista
		    model.addAttribute("tratamiento", tratamientosMasajista);
		    model.addAttribute("tipoEspecialidad", especialidadesCorporales);
		    model.addAttribute("especialidadSeleccionada", nuevaSeleccionEspecialidad.getDisplayName());
		    model.addAttribute("tipoEspecialidadSeleccionada", tipoEspecialidadSeleccionada.getDisplayName());
		    model.addAttribute("tipoEspecialidadModoEnum", tipoEspecialidadSeleccionada);
		    return "pagina_profesional/completarEspecialidadesAndTratamientoProfesional";
		    
		}else if(tipoEspecialidadSeleccionada.getDisplayName().equalsIgnoreCase("Manicura")) {
			
			// Filtrar los tratamientos que contienen "Manicura" en su nombre y guardarlas en una lista
		    List<TratamientoEnum> tratamientosManicura = Arrays.stream(TratamientoEnum.values())
		            .filter(especialidadList -> especialidadList.name().contains("MANICURA"))
		            .collect(Collectors.toList());
		    
		    // Filtrar las especialidades que contienen "CORPORAL" en su nombre y guardarlas en una lista
	        List<TipoDeEspecialidad> especialidadesCorporales = Arrays.stream(TipoDeEspecialidad.values())
	                .filter(especialidadList -> especialidadList.name().contains("ESTETICO"))
	                .collect(Collectors.toList());

	        //pasamos ambas listas a la vista
		    model.addAttribute("tratamiento", tratamientosManicura);
		    model.addAttribute("tipoEspecialidad", especialidadesCorporales);
		    model.addAttribute("especialidadSeleccionada", nuevaSeleccionEspecialidad.getDisplayName());
		    model.addAttribute("tipoEspecialidadSeleccionada", tipoEspecialidadSeleccionada.getDisplayName());
		    model.addAttribute("tipoEspecialidadModoEnum", tipoEspecialidadSeleccionada);
		    return "pagina_profesional/completarEspecialidadesAndTratamientoProfesional";
			
			
		}else if(tipoEspecialidadSeleccionada.getDisplayName().equalsIgnoreCase("Pedicura")) {
			
			// Filtrar los tratamientos que contienen "PEDICURA" en su nombre y guardarlas en una lista
		    List<TratamientoEnum> tratamientosManicura = Arrays.stream(TratamientoEnum.values())
		            .filter(especialidadList -> especialidadList.name().contains("PEDICURA"))
		            .collect(Collectors.toList());
		    
		    // Filtrar las especialidades que contienen "CORPORAL" en su nombre y guardarlas en una lista
	        List<TipoDeEspecialidad> especialidadesCorporales = Arrays.stream(TipoDeEspecialidad.values())
	                .filter(especialidadList -> especialidadList.name().contains("ESTETICO"))
	                .collect(Collectors.toList());

	        //pasamos ambas listas a la vista
		    model.addAttribute("tratamiento", tratamientosManicura);
		    model.addAttribute("tipoEspecialidad", especialidadesCorporales);
		    model.addAttribute("especialidadSeleccionada", nuevaSeleccionEspecialidad.getDisplayName());
		    model.addAttribute("tipoEspecialidadSeleccionada", tipoEspecialidadSeleccionada.getDisplayName());
		    model.addAttribute("tipoEspecialidadModoEnum", tipoEspecialidadSeleccionada);
		    return "pagina_profesional/completarEspecialidadesAndTratamientoProfesional";
			
		}else {
			String error = "Error al seleccionar una especialidad";
			model.addAttribute("showModalError", true);
			model.addAttribute("error", error);
			return "pagina_profesional/completarEspecialidadesAndTratamientoProfesional";
		}
		
		
	}
		
	
	@PostMapping("/guardarDatosProfesional")
	public String guardarDatosProfesional(
			@RequestParam (required = false) String matricula,
			@RequestParam (required = false) String sexo,
			@RequestParam (required = false) String telefono,
			@RequestParam (required = false) String provincia,
			@RequestParam (required = false) String direccion,
			@RequestParam (required = false) String especialidadModoEnum,
			@RequestParam (required = false) String tipoEspecialidadModoEnum,
			@RequestParam (required = false) String tratamientosSeleccionados,
			@RequestParam (required = false) String DiasDeLaSemanaSeleccionados,
			@RequestParam (required = false) String horariosSeleccionados,
			@RequestParam (required = false) String emailUsuario, //Esta valor viene del input oculto de la hoja completarDatos, que a su vez viene del meotodo Login en ControladorPagina
			ModelMap model) throws MiExcepcion {
		
		try {
			
			//Guardamos los datos del formulario que lleno el nuevo cliente
			servicioProfesional.registrarProfesional(emailUsuario, matricula, provincia, direccion, telefono, sexo,
					especialidadModoEnum, tipoEspecialidadModoEnum, DiasDeLaSemanaSeleccionados, horariosSeleccionados, tratamientosSeleccionados);				
		} catch (MiExcepcion e) {
			System.out.println(e.getMessage());
			model.put("error", e.getMessage());
			//Pasamos todo a la vista si hay error, para que el usuario no tenga que volver a ingresar todos los datos de nuevo si hay errores
			model.addAttribute("provincias", Provincias.values());
			model.addAttribute("especialidad", Especialidad.values());
			model.addAttribute("tipoEspecialidad", TipoDeEspecialidad.values());
   		 	model.addAttribute("tratamiento", TratamientoEnum.values());
   		 	model.addAttribute("DiasDeLaSemana", DiasDeLaSemana.values());
			model.addAttribute("matricula", matricula);
			model.addAttribute("sexo",sexo);
			model.addAttribute("telefono", telefono);
			model.addAttribute("direccion", direccion);
			model.addAttribute("emailUsuario", emailUsuario);
			model.addAttribute("showModalError", true);
			return "pagina_profesional/completarEspecialidadesAndTratamientoProfesional";
		}
		return "redirect:/homeProfesional?email=" + emailUsuario; //redirecionamos al metodo homeProfesional enviando la varibale mail
	}
	
	@PostMapping("/actualizarDatosProfesional")
	public String actualizarDatosProfesional(
			@RequestParam String idProfesional, //este atributo es enviado en un input oculto de la pag misdatosProfesional
		    @RequestParam(required = false) String email, // Este y los demas atributos los puse como no requeridos para poder personalizar las excepciones
			@RequestParam(required = false) String domicilio,
			@RequestParam(required = false) String sexo,
			@RequestParam(required = false) String telefono,
			ModelMap model) throws MiExcepcion {
		
		//Buscamos mediante el id el mail anterior del admin y lo guardamos en la variable emailAnterior por si acaso deja el campo de email vacio o coloca un email no valido
		// entonces usamos este mail anterior para poder pasarlo al controlador de misdatosClientes y poder visualizar los datos del cliente
		// Tambien buscamos los valores previamente guardados en la base de datos para poder compararlos con los nuevos
		Profesional profesional = servicioProfesional.obetenerDatosProfesional(idProfesional);
					
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(email);
		//Teniendo el valos de los datos guardados y los que envian al presionar guardar en el formualario podemos comparar si se hiz alguna modificaicon
		//de los datos, si presiona guardar y no se modifico nada, recargar la misma pagina y no muestra ningun mensaje
		if (profesional.getEmail().toUpperCase().equals(email.toUpperCase()) 
				&& profesional.getDomicilio().toUpperCase().equals(domicilio.toUpperCase()) 
				&& profesional.getSexo().toString().toUpperCase().equals(sexo.toUpperCase()) 
				&& profesional.getTelefono().toUpperCase().equals(telefono.toUpperCase())) {
			
			model.addAttribute("email", email);
			model.addAttribute("datosProfesional",datosProfesional);
			return "/pagina_profesional/misdatosProfesional";
			
		}
		
		try {
			//este metodo verifica valida el mail y los nuevos datos del cliente y los remplaza en la base de datos
			servicioProfesional.modificarProfesional(idProfesional, email, profesional.getEmail().toUpperCase(), domicilio, sexo, telefono );
			List <Usuario> datosProfesionalActualizado = servicioUsuario.buscarPorEmail(email);
			
			LocalDate fechaNacimiento = null;
			for (Usuario profesionalFechaNacimiento : datosProfesional) {
				fechaNacimiento = profesionalFechaNacimiento.getFechaNacimiento();
			}
			
			String exito = "Datos actualizados correctamente";
			model.addAttribute("datosProfesional",datosProfesionalActualizado);
			model.addAttribute("fechaNacimientoLocalDate", fechaNacimiento);
			model.addAttribute("exito",exito);
			model.addAttribute("showModalExito", true);
			return "/pagina_profesional/misdatosProfesional"; //si todo sale bien redireccionamos al metodo misdatosProfesional con el mail actualizado y un mensaje de exito
			
			
		} catch (MiExcepcion e) {
			String error = e.getMessage(); // en la exepcion e.getmessage obtenenos el valor de la exepcion personalizada que se de y la enviamos al controlador de misdatosProfesional para ser monstrada en pantalla
			List <Usuario> datosProfesionalAnterior = servicioUsuario.buscarPorEmail(profesional.getEmail().toUpperCase());
			model.addAttribute("datosProfesional",datosProfesionalAnterior);
			model.addAttribute("error",error);
			model.addAttribute("showModalError", true);
			return "/pagina_profesional/misdatosProfesional"; // si se produce alguna exepcion en algun campo enviamos el mail anterior del usuario y un mensaje de error al metodo misdatosProfesional
		}
	}
	

}
