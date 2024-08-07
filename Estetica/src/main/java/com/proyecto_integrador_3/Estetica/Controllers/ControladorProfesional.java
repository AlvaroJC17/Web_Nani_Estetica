package com.proyecto_integrador_3.Estetica.Controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.DiasDeLaSemana;
import com.proyecto_integrador_3.Estetica.Enums.Especialidad;
import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.Enums.TipoDeEspecialidad;
import com.proyecto_integrador_3.Estetica.Enums.TratamientoEnum;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioProfesional;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCliente;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioProfesional;
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
	public RepositorioProfesional repositorioProfesional;
	
	@Autowired
	public RepositorioUsuario repositorioUsuario;

	
	@PostMapping("/editarDatosPersonalesPaciente")
	public String editarDatosPersonalesPaciente(
			@RequestParam(required = false) String emailCliente,
			@RequestParam(required = false) String emailProfesional,
			@RequestParam(required = false) String idCliente,
			Model model) throws MiExcepcion {
	
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(emailProfesional);
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailCliente);
		List<Cliente> datosPaciente = servicioCliente.buscarPacientePorId(idCliente);
		
		model.addAttribute("datosProfesional", datosProfesional);
		model.addAttribute("datosCliente", datosCliente);
		model.addAttribute("datosPaciente",datosPaciente);
		return "/pagina_profesional/datosPersonalesPacienteEditar";
	}
	
	@PostMapping("/guardarDatosPacienteEditado")
	public String guardarDatosPacienteEditado(
			@RequestParam(required = false) String idCliente,
			@RequestParam(required = false) String emailCliente,
			@RequestParam(required = false) String emailProfesional,
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
			@RequestParam(name="tratamientosFacialesAnteriores", required = false) String tratamientos_faciales_anteriores,
			@RequestParam(required = false) String resultados_tratamiento_anterior,
			@RequestParam(required = false) String cuidado_de_piel,
			@RequestParam(required = false) String motivo_consulta,
			@RequestParam(required = false) String notas_profesional,
			Model modelo) throws MiExcepcion {

							
		try {
					servicioTurnos.formularioTurnos(idCliente, email, fuma, drogas, alcohol, deportes,
					ejercicios, medicamentos, nombreMedicamento, embarazo, amamantando, ciclo_menstrual,
					alteracion_hormonal, vitaminas, corticoides, hormonas, metodo_anticonceptivo,
					sufre_enfermedad, cual_enfermedad, tiroides, paciente_oncologica, fractura_facial,
					cirugia_estetica, indique_cirugia_estetica, tiene_implantes, marca_pasos, horas_sueno,
					exposicion_sol, protector_solar, reaplica_protector, consumo_carbohidratos,
					tratamientos_faciales_anteriores, resultados_tratamiento_anterior, cuidado_de_piel,
					motivo_consulta, notas_profesional);
			
			List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(emailProfesional);
			List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailCliente);
			List<Cliente> datosPaciente = servicioCliente.buscarPacientePorId(idCliente);
			modelo.addAttribute("datosProfesional", datosProfesional); //datos para el menu de la pagina
			modelo.addAttribute("datosCliente", datosCliente); // datos para la seccion de datos personales
			modelo.addAttribute("datosPaciente", datosPaciente); // datos para la seccion del formulario y nota del paciente
			return "/pagina_profesional/datosPersonalesPaciente";
			
			
				} catch (Exception e) {
					System.out.println(e.getMessage());
					
				}
		return "";
	}
      
	
	@PostMapping("/buscarDatosPersonalesPaciente")
	public String buscarDatosPersonalesPaciente(
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String idCliente,
			@RequestParam(required = false) String dato,
			Model modelo) throws MiExcepcion {

		
		//Datos del profesional para los menu de la pagina
				List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(email);
				
		//si el profesional le da al boton ver perfil sin seleccionar un paciente, entra en este codigo
		//para enviarle un mensaje de error pero siempre mostrandole el resultado de la busqueda previa que
		//realizo. La variable dato es la misma del metodo /buscadorPaciente
		if (idCliente == null || idCliente.isEmpty()) {
			String datoSinEspacios = dato.trim();
			List<Persona> pacienteDni = servicioProfesional.buscarPacienteByRolAndDni(datoSinEspacios, Rol.CLIENTE);
			List<Persona> pacienteNombre = servicioProfesional.buscarPacienteByRolAndNombre(datoSinEspacios, Rol.CLIENTE);
			List<Persona> pacienteEmail = servicioProfesional.buscarPacienteByRolAndEmail2(datoSinEspacios, Rol.CLIENTE);
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
			
		//Buscamos el email del cliente con el id
		String emailCliente = null;
		Optional <Usuario> buscarEmailCliente = repositorioUsuario.buscarPorIdOptional(idCliente);
		if (buscarEmailCliente.isPresent()) {
			Usuario emailUsuario = buscarEmailCliente.get();
			emailCliente = emailUsuario.getEmail();
		}
	
		//Datos del cliente para los datos personales
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailCliente);
		
		//datos del cliente/paciente para las respuestas del formulario
		List<Cliente> datosPaciente = servicioCliente.buscarPacientePorId(idCliente);
		modelo.addAttribute("datosProfesional", datosProfesional); //datos para el menu de la pagina
		modelo.addAttribute("datosCliente", datosCliente); // datos para la seccion de datos personales
		modelo.addAttribute("datosPaciente", datosPaciente); // datos para la seccion del formulario y nota del paciente
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
		List<Persona> pacienteDni = servicioProfesional.buscarPacienteByRolAndDni(datoSinEspacios, Rol.CLIENTE);
		List<Persona> pacienteNombre = servicioProfesional.buscarPacienteByRolAndNombre(datoSinEspacios, Rol.CLIENTE);
		List<Persona> pacienteEmail = servicioProfesional.buscarPacienteByRolAndEmail2(datoSinEspacios, Rol.CLIENTE);
		
		//Validamos que alguna de las listas de arriba encontro un resultado, esto implica que si
		//encontro resultado entonces el usuario que encontro tiene rol de cliente
		//si el cliente buscado no existe o no tiene rol de cliente entonces las listas estaran vacias
		// y con esta validacion mandamos un mensaje de error al usuario profesional
		if (pacienteDni.isEmpty() && pacienteNombre.isEmpty() && pacienteEmail.isEmpty()) {
			error = "Los datos ingresador no pertenecen a un paciente o el paciente no se encuentra registrado";
			//model.addAttribute("usuariosEmail", email);
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
		model.addAttribute("datosProfesional", datosProfesional);
		return "/pagina_profesional/misdatosProfesional";	
	}
	
	@PostMapping("/guardarDatosProfesional")
	public String guardarDatosProfesional(
			@RequestParam String matricula,
			@RequestParam String sexo,
			@RequestParam String telefono,
			@RequestParam String provincia,
			@RequestParam String direccion,
			@RequestParam String especialidadesSeleccionadas,
			@RequestParam String tipoEspecialidadesSeleccionadas,
			@RequestParam String tratamientosSeleccionados,
			@RequestParam String DiasDeLaSemanaSeleccionados,
			@RequestParam String horariosSeleccionados,
			@RequestParam String emailUsuario, //Esta valor viene del input oculto de la hoja completarDatos, que a su vez viene del meotodo Login en ControladorPagina
			ModelMap model) throws MiExcepcion {
		
		try {
			
			//Guardamos los datos del formulario que lleno el nuevo cliente
			servicioProfesional.registrarProfesional(emailUsuario, matricula, provincia, direccion, telefono, sexo,
					especialidadesSeleccionadas, tipoEspecialidadesSeleccionadas, DiasDeLaSemanaSeleccionados, horariosSeleccionados, tratamientosSeleccionados);				
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
			return "pagina_profesional/completarDatosProfesional";
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
		String emailAnterior = null;
		String domicilioAnterior = null;
		Sexo sexoAnterior = null;
		String nuevoSexo = null;
		String telefonoAnterior = null;
		
		Optional<Profesional> identificarProfesional = repositorioProfesional.findById(idProfesional);
		if (identificarProfesional.isPresent()) {
			Profesional datosAnteriorProfesional = identificarProfesional.get();
			emailAnterior = datosAnteriorProfesional.getEmail().toUpperCase();
			domicilioAnterior = datosAnteriorProfesional.getDomicilio().toUpperCase();
			sexoAnterior = datosAnteriorProfesional.getSexo();
			nuevoSexo = sexoAnterior.toString().toUpperCase();
			telefonoAnterior = datosAnteriorProfesional.getTelefono().toUpperCase();
		}
					
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(email);
		//Teniendo el valos de los datos guardados y los que envian al presionar guardar en el formualario podemos comparar si se hiz alguna modificaicon
		//de los datos, si presiona guardar y no se modifico nada, recargar la misma pagina y no muestra ningun mensaje
		if (emailAnterior.equals(email.toUpperCase()) && domicilioAnterior.equals(domicilio.toUpperCase()) && nuevoSexo.equals(sexo.toUpperCase()) && telefonoAnterior.equals(telefono.toUpperCase())) {
			model.addAttribute("email", email);
			model.addAttribute("datosProfesional",datosProfesional);
			return "/pagina_profesional/misdatosProfesional";
			//return "redirect:/misdatosProfesional?email=" + email;
		}
		
		try {
			//este metodo verifica valida el mail y los nuevos datos del cliente y los remplaza en la base de datos
			servicioProfesional.modificarProfesional(idProfesional, email, emailAnterior, domicilio, sexo, telefono );
			List <Usuario> datosProfesionalActualizado = servicioUsuario.buscarPorEmail(email);
			String exito = "Datos actualizados correctamente";
			model.addAttribute("datosProfesional",datosProfesionalActualizado);
			model.addAttribute("exito",exito);
			model.addAttribute("showModalExito", true);
			return "/pagina_profesional/misdatosProfesional"; //si todo sale bien redireccionamos al metodo misdatosProfesional con el mail actualizado y un mensaje de exito
			
			
		} catch (MiExcepcion e) {
			String error = e.getMessage(); // en la exepcion e.getmessage obtenenos el valor de la exepcion personalizada que se de y la enviamos al controlador de misdatosProfesional para ser monstrada en pantalla
			List <Usuario> datosProfesionalAnterior = servicioUsuario.buscarPorEmail(emailAnterior);
			model.addAttribute("datosProfesional",datosProfesionalAnterior);
			model.addAttribute("error",error);
			model.addAttribute("showModalError", true);
			return "/pagina_profesional/misdatosProfesional"; // si se produce alguna exepcion en algun campo enviamos el mail anterior del usuario y un mensaje de error al metodo misdatosProfesional
		}
	}
	
	//metodo relacionado con actualizarContrasenaProfesional
	@GetMapping("/cambiarContrasenaProfesional")
	public String cambiarContrasenaProfesional(
			@RequestParam String email,
			@RequestParam(required = false) String exito,
			@RequestParam(required = false) String error,
			ModelMap model) {
		
		
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(email);
		
		model.addAttribute("datosProfesional", datosProfesional);
		model.addAttribute("exito", exito);
		model.addAttribute("error", error);
		return "/pagina_profesional/cambiarContrasenaProfesional";
	}
	
	//Metodo relacionado con cambiarContrasenaProfesional
	@PostMapping("actualizarContrasenaProfesional")
	public String actualizarContrasenaProfesional(
			@RequestParam String emailProfesional, //Esta variable viene de un input oculto de la pag cambiarContrasenaProfesional
			@RequestParam String idProfesional, //Esta variable viene de un input oculto de la pag de la pag cambiarContrasenaCliente
			@RequestParam String oldPass, //A partir de estas viene del formulario
			@RequestParam String newPass,
			@RequestParam String repeatNewPass,
			ModelMap model) throws MiExcepcion {
		
		String error = null;
		try {
			servicioUsuario.modificarContrasena(idProfesional,oldPass, newPass, repeatNewPass);
			return "/index";
		} catch (Exception e) {
			error = e.getMessage();
			return "redirect:/cambiarContrasenaProfesional?email=" + emailProfesional + "&error=" + error;
		}
		
	}

}
