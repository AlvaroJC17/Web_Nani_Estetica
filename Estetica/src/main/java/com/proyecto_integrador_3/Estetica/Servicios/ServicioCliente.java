package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.FALSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Turnos;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioTurnos;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;

import jakarta.transaction.Transactional;

@Service
public class ServicioCliente {

	@Autowired
	private RepositorioCliente repositorioCliente;
	
	@Autowired
	private RepositorioUsuario repositorioUsuario;
	
	@Autowired
	private RepositorioTurnos repositorioTurnos;
	
	@Autowired
	private ServicioUsuario servicioUsuario;
	
	@Autowired
	private RepositorioPersona repositorioPersona;
	
	
	
	public List <Cliente> buscarPacientePorId(String idCliente){
		return repositorioCliente.buscarPorId(idCliente);
	}
	
	
	
	@Transactional
	public void registrarCliente(String email, String dni, String nombre, String apellido, String ocupacion,
			String direccion, String telefono, String sexo) throws MiExcepcion {
		
		validarDatosCliente(nombre, apellido, dni, sexo, telefono, direccion, ocupacion);

		Optional <Usuario> datosUsuario = servicioUsuario.buscarPorEmailOptional(email);
		if (datosUsuario.isPresent()) {
			Usuario datosCliente = datosUsuario.get();
		
			//Pasamos la fecha de nacimiento de String a un objeto tipo Date
			Sexo nuevoSexo = null;
			nuevoSexo = Sexo.valueOf(sexo.toUpperCase());
			
			
			Cliente nuevo_cliente = new Cliente();
			nuevo_cliente.setEmail(email);
			nuevo_cliente.setContrasena(datosCliente.getContrasena());
			nuevo_cliente.setRol(datosCliente.getRol());
			nuevo_cliente.setActivo(datosCliente.getActivo());
			nuevo_cliente.setValidacionForm(TRUE);
			nuevo_cliente.setFomularioDatos(FALSE);
			nuevo_cliente.setDni(dni);
			nuevo_cliente.setNombre(nombre);
			nuevo_cliente.setApellido(apellido);
			nuevo_cliente.setTelefono(telefono);
			nuevo_cliente.setDomicilio(direccion);
			nuevo_cliente.setFechaNacimiento(datosCliente.getFechaNacimiento());
			nuevo_cliente.setSexo(nuevoSexo);
			nuevo_cliente.setOcupacion(ocupacion);
			repositorioCliente.save(nuevo_cliente);
			repositorioUsuario.delete(datosCliente);
		}
	}
			
	

	@Transactional
	public void modificarCliente(String idCliente, String ocupacion, String email, String emailAnterior, String domicilio, String sexo, String telefono) throws MiExcepcion {
		
		verificarEmailCliente(email, emailAnterior);
		validarActualizacionDeDatosCliente(ocupacion, domicilio, sexo, telefono);
		
		Optional<Cliente> identificarCliente = repositorioCliente.findById(idCliente);
		
		if (identificarCliente.isPresent()) {
			Cliente cliente_actualizado = identificarCliente.get(); // Atribuye el objeto presente a esta nueva variable
			
			Sexo nuevoSexo = null;
			nuevoSexo = Sexo.valueOf(sexo.toUpperCase());
			
			cliente_actualizado.setEmail(email);
        	cliente_actualizado.setOcupacion(ocupacion);
        	cliente_actualizado.setSexo(nuevoSexo);
        	cliente_actualizado.setDomicilio(domicilio);
        	cliente_actualizado.setTelefono(telefono);
        	repositorioCliente.save(cliente_actualizado);
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
		
		System.out.println("entro en el metodo y esta antes de validar datos");
		
		validarDatosFormularioTurno(fuma, drogas, alcohol, deportes, ejercicios, medicamentos, nombreMedicamento, embarazo, amamantando, ciclo_menstrual, alteracion_hormonal, vitaminas, corticoides,
				hormonas, metodo_anticonceptivo, sufre_enfermedad, cual_enfermedad, tiroides, paciente_oncologica,
				fractura_facial, cirugia_estetica, indique_cirugia_estetica, tiene_implantes, marca_pasos,
				horas_sueno, exposicion_sol, protector_solar, reaplica_protector, consumo_carbohidratos,
				tratamientos_faciales_anteriores, resultados_tratamiento_anterior, cuidado_de_piel,
				motivo_consulta);
		
		System.out.println("valido los datos");
				
		Optional<Cliente> identificarCliente = repositorioCliente.findById(idCliente);
		if (identificarCliente.isPresent()) {
			Cliente formulario_cliente = identificarCliente.get(); // Atribuye el objeto presente a esta nueva variable
			
			System.out.println("encontro el cliente y va a guardar los datos");
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
			
			System.out.println("Antes de guardar los datos");
			repositorioCliente.save(formulario_cliente);
			System.out.println("Despues de guardar los datos");
			
		}
		
	}

	@Transactional
	public void guardarTurno(String idCliente, String profesional, Turnos turnos, String fecha,
			String facial, String espalda, String pulido, String dermaplaning, String exfoliacion,
			String lifting, String perfilado, String laminado, String hydralips, String microneedling,
			String horario, String email) throws MiExcepcion {
		
		//Validamos que todos los valores vengan bien
		validarGuardarTurno(profesional, turnos, fecha, facial, espalda, pulido, dermaplaning,
				exfoliacion, lifting, perfilado, laminado, hydralips,
				microneedling, horario);
		
		/*Se crea la variable de tipo string para guardar el dato de la provincia que selecciono
		 * el usuario, este dato viene adjunto en el objeto de tipo turno que se recibe del
		 * formulario */
		String provincia = turnos.getProvincias().toString(); // se para convertir el enum de la provincia  a string
		
		//Como se recibe el nombre y el apellido en un solo string aplicamos este codigo para separarlos
		String [] nombreApellidoProfesional = profesional.split("/");
		String nombreProfesional = nombreApellidoProfesional[0];
		String apellidoProfesional = nombreApellidoProfesional[1];
		
		
		
		//Creamos un array con los tratamientos y limpiamos los seleccionados  que vienen null
		// A los que viene con un valos de string los va sumando en un contador y los que vienen null les asigna valor vacio
		// esto para que en la base datos no me guanden la palabra null de los tratamientos no seleccionados
		String [] tratamientos = {facial, espalda, pulido, dermaplaning, exfoliacion, lifting, perfilado, laminado, hydralips,
				microneedling,};
		int contador = 0;
		String tratamientosSeleccionados ="";
		
		for (int i = 0; i < tratamientos.length; i++) {
			if (tratamientos[i] != null && tratamientos[i] != "") {
				contador++;
				tratamientosSeleccionados += tratamientos[i] + " ";
			}else if(tratamientos[i] == null || tratamientos[i] == " " || tratamientos[i] == "" ) {
				tratamientos[i] = "";
				tratamientosSeleccionados += tratamientos[i];
			}
		}
		
		//Usamos este contador para saber cuantos turnos se seleccionaron y si es mayor a dos, se lanza
		// un mensaje de error al usuario.
		if (contador > 2) {
			throw new MiExcepcion("Solo se pueden seleccionar dos tratamientos por turno");
		}
		
	
		//Funcion para pasar un fecha de tipo String a LocalDate
		LocalDate fechaUsuario = pasarFechaStringToLocalDate(fecha);
		
		
		//Buscamos el dni del cliente que esta seleccionando el turno para adjuntarlo al objeto turno que se va a guardar en la base de datos
		String dniCliente = null;
		Optional<Persona> dniUsuario = repositorioPersona.findById(idCliente);
		if (dniUsuario.isPresent()) {
			Persona usu = dniUsuario.get();
			dniCliente = usu.getDni();
		}
		
		
		// Creamos un nuevo objeto de tipo turno que es el que se va a guardar en la base de datos
		Turnos nuevoTurno = new Turnos(); 
		nuevoTurno.setProvincia(provincia);
		nuevoTurno.setProfesional(nombreProfesional + " " + apellidoProfesional);
		nuevoTurno.setFecha(fechaUsuario);
		nuevoTurno.setHorario(horario);
		nuevoTurno.setEmail(email);
		nuevoTurno.setTratamiento(tratamientosSeleccionados);
		nuevoTurno.setDniCliente(dniCliente);
		nuevoTurno.setActivo(TRUE);
		repositorioTurnos.save(nuevoTurno);
		
	}

	public void validarGuardarTurno(String profesional, Turnos turnos, String fecha, String facial,
			String espalda, String pulido, String dermaplaning, String exfoliacion, String lifting,
			String perfilado, String laminado, String hydralips, String microneedling, String horario) throws MiExcepcion {
		
		if (turnos.getProvincias() == null) {
			throw new MiExcepcion("Debe seleccionar una provincia");
		}
		
		if (profesional.equals("Profesional") || profesional == null) {
			throw new MiExcepcion("Debe seleccionar un profesional");
		}
		
		if(fecha == null || fecha.isEmpty() || fecha == "") {
			throw new MiExcepcion("Debe seleccionar una fecha");
		}
		
		if(horario == null || horario.isEmpty() || horario == "") {
			throw new MiExcepcion("Debe seleccionar un horario");
		}
		
		if (facial == null && espalda == null && pulido == null && dermaplaning == null
				&& exfoliacion == null && lifting == null && perfilado == null && laminado == null
				&& hydralips == null && microneedling == null ) {
			throw new MiExcepcion("Debe seleccionar almenos un tratamiento");
		}
	}
		

	 public void validarDatosCliente(String nombre, String apellido, String dni,  String sexo,
			 String telefono, String direccion, String ocupacion) throws MiExcepcion {
		 
		 if (nombre == null || nombre.isEmpty() || nombre.trim().isEmpty()) {
			 throw new MiExcepcion("El nombre no puede estar vacio");
		 }
		 if (apellido == null || apellido.isEmpty() || apellido.trim().isEmpty()) {
			 throw new MiExcepcion("El apellido no puede estar vacio");
		 }
		 if (dni == null || dni.isEmpty() || dni.trim().isEmpty()) {
			 throw new MiExcepcion("El dni no puede estar vacio");
		 } 
		 if(repositorioPersona.buscarPorDniOptional(dni).isPresent()) {
			 throw new MiExcepcion("El numero de dni ya está registrado");
		 }
		 if (sexo == null || sexo.isEmpty() || sexo.trim().isEmpty() || sexo.equals("Seleccione")) {
			 throw new MiExcepcion("El sexo no puede estar vacio");
		 }
		 if (direccion == null || direccion.isEmpty() || direccion.trim().isEmpty()) {
			 throw new MiExcepcion("La dirección no puede estar vacia");
		 }
		 if (telefono == null || telefono.isEmpty() || telefono.trim().isEmpty()) {
			 throw new MiExcepcion("EL telefono no puede estar vacio");
		 }
		 if (ocupacion == null || ocupacion.isEmpty() || ocupacion.trim().isEmpty()) {
			 throw new MiExcepcion("La ocupacion no puede estar vacio");
		 }
	 }
	 
	 	 
	 public void validarActualizacionDeDatosCliente(String ocupacion, String domicilio, String sexo, String telefono) throws MiExcepcion {
		 if (ocupacion == null || ocupacion.isEmpty() || ocupacion.trim().isEmpty()) {
			 throw new MiExcepcion("La ocupacion no puede estar vacio");
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
	 
	 public void verificarEmailCliente(String email, String emailAnterior) throws MiExcepcion {
		 
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
	 
	 public LocalDate pasarFechaStringToLocalDate(String fecha) throws MiExcepcion {
		 
		//Recibimos la fecha como un string y la pasamos a Date y luego a LocalDate para guardarla en la base de datos.
			LocalDate fechaUsuario = null;
			Date fechaFormateada = null;
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			try {
				fechaFormateada = formato.parse(fecha);
				fechaUsuario =  fechaFormateada.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
			} catch (ParseException e) {
				throw new MiExcepcion("Debe seleccionar una fecha");
			}
			
			return fechaUsuario;
			
	 }
	 
	 public boolean esFinDeSemana(LocalDate fecha) {
		 DayOfWeek dayOfWeek = fecha.getDayOfWeek();
	        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
	    }
	 
	 public boolean fechaYaPaso(LocalDate fecha) {
	        return fecha.isBefore(LocalDate.now());
	    }
	 
}
	 
		        	
	 
	 



