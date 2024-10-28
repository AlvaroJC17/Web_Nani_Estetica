package com.proyecto_integrador_3.Estetica.Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.proyecto_integrador_3.Estetica.Entidades.Colaborador;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Turnos;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.EstadoDelTurno;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCliente;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioColaborador;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioHorario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioPersona;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioProfesional;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioTurnos;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

@Controller
public class ControladorColaborador {
	
	@Autowired
	ServicioUsuario servicioUsuario;
	
	@Autowired
	ServicioColaborador servicioColaborador;
	
	@Autowired
	ServicioProfesional servicioProfesional;
	
	@Autowired
	ServicioPersona servicioPersona;
	
	@Autowired
	ServicioTurnos servicioTurnos;
	
	@Autowired
	ServicioCliente servicioCliente;
	
	@Autowired
	ServicioHorario servicioHorario;
	
	@Autowired
	RepositorioUsuario repositorioUsuario;
	

	//Devuelve la pagina homeColaborador con los datos del usuario que le pasemos por mmail
		@GetMapping("/homeColaborador")
		public String homeColaborador(
				@RequestParam String emailColaborador,
				Model model) {
			
			List <Usuario> datosColaborador = servicioUsuario.buscarPorEmail(emailColaborador);
			model.addAttribute("datosColaborador", datosColaborador);
			return "/pagina_colaborador/homeColaborador";	
			
		}
		
		//Muestra todos los datos personales de la persona en la pagina misdatosColaborador
		@GetMapping("/misdatosColaborador")
		public String misdatosColaborador(
				@RequestParam(required = false) String emailColaborador,
				Model model) { // el valor del parametro email viene del html homeColaborador
			
			List <Usuario> datosColaborador = servicioUsuario.buscarPorEmail(emailColaborador);
			model.addAttribute("datosColaborador", datosColaborador);
			model.addAttribute("sexos", Sexo.values());
		return "/pagina_colaborador/misdatosColaborador";	
		}
		
		
		@PostMapping("/actualizarDatosColaborador")
		public String actualizarDatosColaborador(
				@RequestParam String idColaborador, //este atributo es enviado en un input oculto de la pag misdatosCliente
				@RequestParam(required = false) String ocupacion, // Este y los demas atributos los puse como no requeridos para poder personalizar las excepciones
			    @RequestParam(required = false) String emailColaborador,
				@RequestParam(required = false) String domicilio,
				@RequestParam(required = false) String sexo,
				@RequestParam(required = false) String telefono,
				ModelMap model) throws MiExcepcion {
			
			//Buscamos mediante el id el mail anterior del cliente y lo guardamos en la variable emailAnterior por si acaso deja el campo de email vacio o coloca un email no valido
			// entonces usamos este mail anterior para poder pasarlo al controlador de misdatosClientes y poder visualizar los datos del cliente
			// Tambien buscamos los valores previamente guardados en la base de datos para poder compararlos con los nuevos
			Colaborador datosAnteriores = servicioColaborador.optionalBuscarColaboradorPorId(idColaborador);
			
			String ocupacionAnterior = datosAnteriores.getOcupacion().toUpperCase();
			String emailAnterior = datosAnteriores.getEmail();
			String domicilioAnterior = datosAnteriores.getDomicilio().toUpperCase();
			Sexo sexoAnterior = datosAnteriores.getSexo();
			String nuevoSexo = sexoAnterior.toString().toUpperCase();
			String telefonoAnterior = datosAnteriores.getTelefono();
			
			//Cuando el cliente le da guardar a los datos sin modificar nada el sexo viene con valor vacioa
			//por eso armamos este condicional para manejar el error y asignarle un valor a sexo
			//que es el valor que tiene guardado en cliente en la base
			if (sexo == null || sexo.isEmpty()) {
				sexo = nuevoSexo;
			}
			
			if (ocupacionAnterior == null || ocupacionAnterior.isEmpty()) {
				ocupacionAnterior = ocupacion;
			}
			
			List <Usuario> datosColaborador = servicioUsuario.buscarPorEmail(emailColaborador); //Buscamos todos los datos pertenecientes al cliente despues de haber sido actualizados en la base de datos y los mostramos en el campo correspondiente
			//Teniendo el valor de los datos guardados y los que envian al presionar guardar en el formualario podemos comparar si se hiz alguna modificaicon
					//de los datos, si presiona guardar y no se modifico nada, recargar la misma pagina y no muestra ningun mensaje
			if (ocupacionAnterior.equals(ocupacion.toUpperCase()) && domicilioAnterior.equals(domicilio.toUpperCase()) && nuevoSexo.equals(sexo.toUpperCase()) && telefonoAnterior.equals(telefono.toUpperCase())) {
				model.addAttribute("email", emailColaborador);
				model.addAttribute("sexos", Sexo.values());
				model.addAttribute("datosColaborador",datosColaborador);
				return "/pagina_colaborador/misdatosColaborador";
				
			}
			
			try {
				//este metodo verifica valida el mail y los nuevos datos del cliente y los remplaza en la base de datos
				servicioColaborador.modificarColaborador(idColaborador, ocupacion, emailColaborador, emailAnterior, domicilio, sexo, telefono );
				List <Usuario> datosColaboradorActualizados = servicioUsuario.buscarPorEmail(emailColaborador);
				String exito = "<span class='fs-6'>Sus datos han sido actualizados de forma exitosa</span>";
				model.addAttribute("datosColaborador",datosColaboradorActualizados);
				model.addAttribute("exito",exito);
				model.addAttribute("sexos", Sexo.values());
				model.addAttribute("showModalExito", true);
				return "/pagina_colaborador/misdatosColaborador";
				
			} catch (MiExcepcion e) {
				String error = e.getMessage(); // en la exepcion e.getmessage obtenenos el valor de la exepcion personalizada que se de y la enviamos al controlador de misdatosCliente para ser monstrada en pantalla
				List <Usuario> datosColaboradorAnterior = servicioUsuario.buscarPorEmail(emailAnterior); //Buscamos los datos ateriores a la excepcion y los mostramos en caso de que haya un error por parte del usuario
				model.addAttribute("datosColaborador",datosColaboradorAnterior);
				model.addAttribute("sexos", Sexo.values());
				model.addAttribute("error",error);
				model.addAttribute("showModalError", true);
				return "/pagina_colaborador/misdatosColaborador";
			}
		}
		
		@PostMapping("/buscadorPacientesColaborador")
		public String buscadorPacientesColaborador(
				@RequestParam(required = false) String dato,  //la variable dato puede ser un nombre, dni o mail
				@RequestParam String emailColaborador,
				Model model) throws MiExcepcion {
			
								
			List <Usuario> datosColaborador = servicioUsuario.buscarPorEmail(emailColaborador);
			String error = null;
			String datoSinEspacios = dato.trim(); //Le quitamos los espacios en blanco al principio y final de la palabra

			//Validamos que la busqueda no se haya hecho en blanco y mostramos un mensaje
			if (datoSinEspacios.isEmpty() || Objects.equals(datoSinEspacios, null)) {
				error = "Indique un numero de DNI, nombre o email para realizar la busqueda";
				model.addAttribute("datosColaborador", datosColaborador);
				model.addAttribute("error", error);
				model.addAttribute("showModalError", true);
				return "/pagina_colaborador/buscadorPacienteColaborador";
			}
			
			//Validamos que el usuario buscado solo por dni o emial exista en la base de datos, sino mandamos un mensaje de error.
			//Solo validamos email y dni porque si resultado siempre va a ser un unico registro y el metodo isPresent solo acepta un solo registro como resultado
			if (!servicioUsuario.buscarPorDniOptional(datoSinEspacios).isPresent() &&  !servicioUsuario.buscarPorEmailOptional(datoSinEspacios).isPresent() && servicioUsuario.buscarNombre(datoSinEspacios).isEmpty()) {
				error = "Usuario no se encuentra registrado";
				model.addAttribute("datosColaborador", datosColaborador);
					model.addAttribute("error", error);
					model.addAttribute("showModalError", true);
					return "/pagina_colaborador/buscadorPacienteColaborador";
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
				model.addAttribute("datosColaborador", datosColaborador);
				model.addAttribute("error", error);
				model.addAttribute("showModalError", true);
				return "/pagina_colaborador/buscadorPacienteColaborador";
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
			model.addAttribute("datosColaborador", datosColaborador);
			model.addAttribute("dato", dato); // le pasamos el valor de dato a la variable dato del formulario modificarUsuario, para despues usarla en el metodo mensajeErrorNoId
			return "/pagina_colaborador/buscadorPacienteColaborador";
		}
		
		
		
		
		@GetMapping("/listarPacientesOcultosColaborador")
	    public String listarUsuarios(
	    		@RequestParam String emailColaborador, //Esta variable proviene de homeColaborador
	    		Model model) throws MiExcepcion {

			List <Usuario> datosColaborador = servicioUsuario.buscarPorEmail(emailColaborador);
			model.addAttribute("datosColaborador", datosColaborador);
			return "/pagina_colaborador/buscadorPacienteColaborador";
		}
		
		
		@PostMapping("/datosPersonalesPacienteColaborador")
		public String datosPersonalesPacienteColaborador(
				@RequestParam(required = false) String emailColaborador,
				@RequestParam(required = false) String idCliente,
				@RequestParam String idColaborador,
				@RequestParam(required = false) String dato,
				Model modelo) throws MiExcepcion {

			Boolean btnCancelarTurno = false; //boolean para habilitar o deshabilitar el boton para cancelar los turnos
			
			//Datos del profesional para los menu de la pagina
			List <Usuario> datosColaborador = servicioUsuario.buscarPorEmail(emailColaborador);
					
			//si el colaborador le da al boton ver perfil sin seleccionar un paciente, entra en este codigo
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
				modelo.addAttribute("datosColaborador",datosColaborador);
				modelo.addAttribute("usuarios", datosPaciente);
				modelo.addAttribute("showModalError", true);
				modelo.addAttribute("error", error);
				modelo.addAttribute("dato", dato);
				return "/pagina_colaborador/buscadorPacienteColaborador";
			}
			
			List<Turnos> buscarTurnosCliente = servicioTurnos.turnosActivosPorIdClienteFechaAsc(idCliente);
			if (!buscarTurnosCliente.isEmpty()) {
				btnCancelarTurno = true;
			}
			
			List<Turnos> tunosConMulta = servicioTurnos.buscarPorIdClienteAndMulta(idCliente);
			
			List<Usuario> datosCliente = servicioUsuario.buscarDatosUsuarioPorId(idCliente);
	
			modelo.addAttribute("btnCancelarTurno", btnCancelarTurno);
			modelo.addAttribute("tunosConMulta", tunosConMulta);
			modelo.addAttribute("buscarTurnosCliente", buscarTurnosCliente);
			modelo.addAttribute("datosCliente", datosCliente);
			modelo.addAttribute("sexos", Sexo.values());
			modelo.addAttribute("datosColaborador",datosColaborador);
			return "/pagina_colaborador/perfilCliente";
		}
		
		
		@PostMapping("/altaUsuario")
		public String altaUsuario(
				@RequestParam String emailColaborador, //Esta variable proviene de homeColaborador
				@RequestParam String idColaborador,
				@RequestParam String idCliente,
				@RequestParam String emailCliente,
				@RequestParam Boolean clienteActivo,
				Model modelo) throws MiExcepcion {
			
			Boolean btnCancelarTurno = false;
			
			//Datos del colaborador para los menu de la pagina
			List <Usuario> datosColaborador = servicioUsuario.buscarPorEmail(emailColaborador);
			
			//Buscamos los turnos activos del cliente para pasar a la tabla que se encuentra en la vista
			List<Turnos> buscarTurnosCliente = servicioTurnos.turnosActivosPorIdClienteFechaAsc(idCliente);
			if (!buscarTurnosCliente.isEmpty()) {
				btnCancelarTurno = true;
			}
			
			//Buscamos los turnos con multa para pasar a la tabla que esta en la vista
			List<Turnos> tunosConMulta = servicioTurnos.buscarPorIdClienteAndMulta(idCliente);
			
			//primero verificamos que el cliente este de baja antes de habilitarlo
			if (!clienteActivo) {
				//Servicio para dar de alta al cliente
				servicioUsuario.altaUsuario(idCliente);
				String exito = "Alta realizada correctamente";
				modelo.addAttribute("exito", exito);
				modelo.addAttribute("sexos", Sexo.values());
				modelo.addAttribute("showModalExito", true);
			}else {
				String error = "El usuario ya se encuentra activo";
				modelo.addAttribute("sexos", Sexo.values());
				modelo.addAttribute("error", error);
				modelo.addAttribute("showModalError", true);
			}
			
			//buscamos los datos del cliente para pasar a la vista
			List<Usuario> datosCliente = servicioUsuario.buscarDatosUsuarioPorId(idCliente);
			
			modelo.addAttribute("btnCancelarTurno", btnCancelarTurno);
			modelo.addAttribute("tunosConMulta", tunosConMulta);
			modelo.addAttribute("buscarTurnosCliente", buscarTurnosCliente);
			modelo.addAttribute("datosColaborador",datosColaborador);
			modelo.addAttribute("datosCliente", datosCliente);
			modelo.addAttribute("sexos", Sexo.values());
			return "/pagina_colaborador/perfilCliente";
		}
			
			
		
		@PostMapping("/bajaUsuario")
		public String bajaUsuario(
				@RequestParam String emailColaborador, //Esta variable proviene de homeColaborador
				@RequestParam String idColaborador,
				@RequestParam String idCliente,
				@RequestParam String emailCliente,
				@RequestParam Boolean clienteActivo,
				Model modelo) throws MiExcepcion {
	
			
			Boolean btnCancelarTurno = false;
			
			//Datos del colaborador para los menu de la pagina
			List <Usuario> datosColaborador = servicioUsuario.buscarPorEmail(emailColaborador);
			
			//Buscamos los turnos activos para pasar a la tabla que esta en la vista
			List<Turnos> buscarTurnosCliente = servicioTurnos.turnosActivosPorIdClienteFechaAsc(idCliente);
			if (!buscarTurnosCliente.isEmpty()) {
				btnCancelarTurno = true;
			}
			
			//Buscamos los turnos con multa para pasar a la tabla que esta en la vista
			List<Turnos> tunosConMulta = servicioTurnos.buscarPorIdClienteAndMulta(idCliente);
			
			//Verificamos primero que el cliente este activo antes de darle de baja
			if (clienteActivo) {
				//Servicio para dar de baja
				servicioUsuario.bajaUsuario(idCliente);
				String exito = "Baja realizada correctamente";
				modelo.addAttribute("sexos", Sexo.values());
				modelo.addAttribute("exito", exito);
				modelo.addAttribute("showModalExito", true);
			}else {
				String error = "El usuario ya se encuentra inactivo";
				modelo.addAttribute("sexos", Sexo.values());
				modelo.addAttribute("error", error);
				modelo.addAttribute("showModalError", true);
			}
			
			//buscamos los datos del cliente para pasar a la vista
			List<Usuario> datosCliente = servicioUsuario.buscarDatosUsuarioPorId(idCliente);
			
			modelo.addAttribute("sexos", Sexo.values());
			modelo.addAttribute("btnCancelarTurno", btnCancelarTurno);
			modelo.addAttribute("tunosConMulta", tunosConMulta);
			modelo.addAttribute("buscarTurnosCliente", buscarTurnosCliente);
			modelo.addAttribute("datosColaborador",datosColaborador);
			modelo.addAttribute("datosCliente", datosCliente);
			return "/pagina_colaborador/perfilCliente";
		}
		
		
		@PostMapping("/quitarMultasCliente")
		public String quitarMultasCliente(
				@RequestParam String emailColaborador, //Esta variable proviene de homeColaborador
				@RequestParam String idColaborador,
				@RequestParam String idCliente,
				@RequestParam String emailCliente,
				@RequestParam Boolean clienteActivo,
				@RequestParam Boolean clienteMulta,
				Model modelo) throws MiExcepcion {
			
			
			Boolean btnCancelarTurno = false;
			
			//Verificamos mediante la variable que viene de la vista si el cliente tiene multas
			if (clienteMulta) {
				//Servicio para setear en false el campo multa tanto del cliente como del o de los turnos
				servicioCliente.quitarMultasCliente(idCliente);
				String exito = "Las multas fueron blanqueadas correctamente";
				modelo.addAttribute("sexos", Sexo.values());
				modelo.addAttribute("exito", exito);
				modelo.addAttribute("showModalExito", true);
			}else {
				String error = "El cliente no tiene multas";
				modelo.addAttribute("sexos", Sexo.values());
				modelo.addAttribute("error", error);
				modelo.addAttribute("showModalError", true);
			}
			
			//Buscamos los turnos activos ordenados por fecha para pasar a la tabla que se encuentra en la vista
			List<Turnos> buscarTurnosCliente = servicioTurnos.turnosActivosPorIdClienteFechaAsc(idCliente);
			if (!buscarTurnosCliente.isEmpty()) {
				btnCancelarTurno = true;
			}
			
			//Buscamos los turnos con multas para pasar a la vista, si el codigo entro en el condicional y en el servicio quitarMultaCliente, esta lista siempre
			//debe estar vacía
			List<Turnos> tunosConMulta = servicioTurnos.buscarPorIdClienteAndMulta(idCliente);
			
			//Buscamos los datos del cliente para pasar a la vista
			List<Usuario> datosCliente = servicioUsuario.buscarDatosUsuarioPorId(idCliente);
			
			//Datos del colaborador para los menu de la pagina
			List <Usuario> datosColaborador = servicioUsuario.buscarPorEmail(emailColaborador);
			
			modelo.addAttribute("sexos", Sexo.values());
			modelo.addAttribute("btnCancelarTurno", btnCancelarTurno);
			modelo.addAttribute("buscarTurnosCliente", buscarTurnosCliente);
			modelo.addAttribute("tunosConMulta", tunosConMulta);
			modelo.addAttribute("datosColaborador",datosColaborador);
			modelo.addAttribute("datosCliente", datosCliente);
			return "/pagina_colaborador/perfilCliente";
		}
			
			
		@PostMapping("/actualizarDatosClienteColaborador")
		public String actualizarDatosClienteColaborador(
				@RequestParam String idCliente, //este atributo es enviado en un input oculto de la pag misdatosCliente
				@RequestParam String emailColaborador,
				@RequestParam(required = false) String ocupacion, // Este y los demas atributos los puse como no requeridos para poder personalizar las excepciones
			    @RequestParam(required = false) String emailCliente,
				@RequestParam(required = false) String domicilio,
				@RequestParam(required = false) String sexo,
				@RequestParam(required = false) String telefono,
				@RequestParam(required = false) String dni,
				@RequestParam(required = false) String fechaNacimiento,
				ModelMap model) throws MiExcepcion {
			
			Boolean btnCancelarTurno = false;
			
			//Buscamos los turnos con multas para pasar a la vista, si el codigo entro en el condicional y en el servicio quitarMultaCliente, esta lista siempre
			//debe estar vacía
			List<Turnos> tunosConMulta = servicioTurnos.buscarPorIdClienteAndMulta(idCliente);
			
			//Datos del colaborador para los menu de la pagina
			List <Usuario> datosColaborador = servicioUsuario.buscarPorEmail(emailColaborador);
			
			//Buscamos los turnos activos ordenados por fecha para pasar a la tabla que se encuentra en la vista
			List<Turnos> buscarTurnosCliente = servicioTurnos.turnosActivosPorIdClienteFechaAsc(idCliente);
			if (!buscarTurnosCliente.isEmpty()) {
				btnCancelarTurno = true;
			}
			
			//Buscamos todos los datos del cliente
			Cliente cliente = servicioCliente.buscarDatosCliente(idCliente);
			
			LocalDate fechaNacimientoCliente = servicioHorario.pasarFechaStringToLocalDateOtroFormato(fechaNacimiento);
	
			//Guardamos los datos del cliente en variables las cuales vas a funcionar como variables base u originales, si hay algun error al introducir la nueva modificacion
			//de datos, entonces mandamos a la vista estos datos originales
			String ocupacionAnterior = cliente.getOcupacion().toUpperCase();
			String emailAnterior = cliente.getEmail();
			String domicilioAnterior = cliente.getDomicilio().toUpperCase();
			Sexo sexoAnterior = cliente.getSexo();
			String nuevoSexo = sexoAnterior.toString().toUpperCase();
			String telefonoAnterior = cliente.getTelefono();
			String dniAnterior = cliente.getDni();
			LocalDate fechaNacimientoAnterior = cliente.getFechaNacimiento();
			
			//Cuando el cliente le da guardar a los datos sin modificar nada el sexo viene con valor vacioa
			//por eso armamos este condicional para manejar el error y asignarle un valor a sexo
			//que es el valor que tiene guardado en cliente en la base
			if (sexo == null || sexo.isEmpty()) {
				sexo = nuevoSexo;
			}
			
			if (ocupacionAnterior == null || ocupacionAnterior.isEmpty()) {
				ocupacionAnterior = ocupacion;
			}
			
			//Buscamos los datos del cliente para pasarlos a la vista
			List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(emailCliente); 
			
			//Comparamos las modificaciones hechas por el usuario con las guardadas en la base de datos, sino hay ninguna modificacion lo mando nuevamente a la
			//vista sin ningun mensaje
			if (ocupacionAnterior.equals(ocupacion.toUpperCase()) && domicilioAnterior.equals(domicilio.toUpperCase()) && nuevoSexo.equals(sexo.toUpperCase()) && telefonoAnterior.equals(telefono.toUpperCase())
					&& dniAnterior.equals(dni) && fechaNacimientoAnterior.equals(fechaNacimientoCliente) && emailAnterior.equals(emailCliente)) {
				model.addAttribute("sexos", Sexo.values());
				model.addAttribute("btnCancelarTurno", btnCancelarTurno);
				model.addAttribute("tunosConMulta", tunosConMulta);
				model.addAttribute("buscarTurnosCliente", buscarTurnosCliente);
				model.addAttribute("datosColaborador",datosColaborador);
				model.addAttribute("datosCliente",datosCliente);
				return "/pagina_colaborador/perfilCliente";
			}
				
			try {

				//este metodo verifica valida el mail y los nuevos datos del cliente y los remplaza en la base de datos
				servicioColaborador.modificarClienteColaborador(idCliente, ocupacion, emailCliente, emailAnterior, dniAnterior, domicilio, sexo, telefono, dni, fechaNacimiento);
				
				//Buscamos de nuevo los datos actualizados del cliente para pasarlos a la vista
				List <Usuario> datosClienteActualizados = servicioUsuario.buscarDatosUsuarioPorId(idCliente);
				String exito = "<span class='fs-6'>Sus datos han sido actualizados de forma exitosa</span>";
				model.addAttribute("datosCliente",datosClienteActualizados);
				model.addAttribute("btnCancelarTurno", btnCancelarTurno);
				model.addAttribute("tunosConMulta", tunosConMulta);
				model.addAttribute("buscarTurnosCliente", buscarTurnosCliente);
				model.addAttribute("datosColaborador",datosColaborador);
				model.addAttribute("exito",exito);
				model.addAttribute("sexos", Sexo.values());
				model.addAttribute("showModalExito", true);
				return "/pagina_colaborador/perfilCliente";
				
			} catch (MiExcepcion e) {
				String error = e.getMessage(); // en la exepcion e.getmessage obtenenos el valor de la exepcion personalizada que se de y la enviamos al controlador de misdatosCliente para ser monstrada en pantalla
				List <Usuario> datosClienteAnterior = servicioUsuario.buscarPorEmail(emailAnterior); //Buscamos los datos ateriores a la excepcion y los mostramos en caso de que haya un error por parte del usuario
				model.addAttribute("datosCliente",datosClienteAnterior);
				model.addAttribute("btnCancelarTurno", btnCancelarTurno);
				model.addAttribute("tunosConMulta", tunosConMulta);
				model.addAttribute("buscarTurnosCliente", buscarTurnosCliente);
				model.addAttribute("datosColaborador",datosColaborador);
				model.addAttribute("sexos", Sexo.values());
				model.addAttribute("error",error);
				model.addAttribute("showModalError", true);
				return "/pagina_colaborador/perfilCliente";
			}
		}	
			
			
			
		
}

