package com.proyecto_integrador_3.Estetica.Controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCliente;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

import jakarta.persistence.EntityManager;

@Controller
//@RequestMapping(")
public class ControladorCliente {

	@Autowired
	public RepositorioCliente repositorioCliente;
	
	@Autowired
	public RepositorioUsuario repositorioUsuario;
	
	@Autowired
	public ServicioCliente servicioCliente;
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	/*Este metodo deriva a la pagina de tratamientos con los valores de mail y id del cliente*/
	@GetMapping("/tratamientos")
	public String tratamientos(
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "idCliente") String idCliente,
			ModelMap model) {
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosCliente", datosCliente);
		return "/pagina_cliente/tratamientos";
	}

	/*Este metodo deriva a la pagina de turnos esteicos con los valores de mail y id del cliente*/
	@GetMapping("/reservaDeTurnoClienteEstetico")
	public String reservaDeTurnoClienteEstetico(
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "idCliente") String idCliente,
			ModelMap model) {
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosCliente", datosCliente);
		return "/pagina_cliente/reservaDeTurnoClienteEstetico";
	}
	
	/*Se usa postmapping por que los valores que recibe este metodo vienen de un formulario con un metodo post*/
	/*Valida si el formulario de preguntas ya se lleno y le muestra al cliente la pagina correspondiente dependiendo de su seleccion*/
	@PostMapping("/reservaDeTurnoCliente")
	public String reservaDeTurnoCliente(
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "idCliente") String idCliente,
			@RequestParam(name = "tratamiento") String tratamiento,
			ModelMap model) {
		
		/*El valor de tratamiento sirve para filtrar que pagina le vamos a mostrar
		 * al cliente para solicitar turno, puede ser facial o corporal*/
		
		Boolean usoDeFormulario = null;
		Optional<Cliente> formularioDeDatos = repositorioCliente.findById(idCliente);
		if (formularioDeDatos.isPresent()) {
			Cliente validarUsoFormulario = formularioDeDatos.get();
			usoDeFormulario = validarUsoFormulario.getFomularioDatos();
		}
		
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
			
		if (usoDeFormulario && tratamiento.equals("facial")) {
			model.addAttribute("datosCliente", datosCliente);
			return "/pagina_cliente/reservaDeTurnoClienteFacial";	
			
		}else if(usoDeFormulario && tratamiento.equals("corporal")) {
			model.addAttribute("datosCliente", datosCliente);
			return "/pagina_cliente/reservaDeTurnoClienteCorporal";	
			
		}else {
			model.addAttribute("tratamiento", tratamiento);
			model.addAttribute("datosCliente", datosCliente);
			model.addAttribute("email", email);
			return "/pagina_cliente/formularioPreguntas";
			
		}
	}
			
			
	@GetMapping("/misturnos")
	public String misturnos() {
	return "/pagina_cliente/misturnos";	
	}
	
	@GetMapping("/misconsultas")
	public String misconsultas() {
	return "/pagina_cliente/misconsultas";	
	}
	
	@GetMapping("/cambiarContrasenaCliente")
	public String cambiarContrasenaCliente(
			@RequestParam(name = "email") String email,
			@RequestParam(name = "exito", required = false) String exito,
			@RequestParam(name = "error", required = false) String error,
			ModelMap model) {
		
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		
		model.addAttribute("datosCliente", datosCliente);
		model.addAttribute("exito", exito);
		model.addAttribute("error", error);
		return "/pagina_cliente/cambiarContrasenaCliente";
	}
		
	@GetMapping("/completarDatosCliente")
	public String completarDatos(
			@RequestParam(name = "email") String emailUsuario,
			@RequestParam(name = "error", required = false) String error,
			ModelMap modelo) {
		
		modelo.addAttribute("emailUsuario", emailUsuario);
		modelo.addAttribute("error", error);
		return "/pagina_cliente/completarDatosCliente";
	}
	
	//Devuelve la pagina homeCLiente con los datos del usuario que le pasemos por mmail
	@GetMapping("/homeCliente")
	public String homeCliente(
			@RequestParam(name = "email") String email, Model model) {
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosCliente", datosCliente);
		return "/pagina_cliente/homeCliente";	
		
	}
	
	//Muestra todos los datos personales de la persona en la pagina misdatosCliente
	@GetMapping("/misdatosCliente")
	public String misdatosCliente(
			@RequestParam(name="email", required = false) String email,
			Model model) { // el valor del parametro email viene del html homeCliente
		
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosCliente", datosCliente);
	return "/pagina_cliente/misdatosCliente";	
	}
	
	
	
	@PostMapping("guardarDatosCliente")
	public String guardarDatosCliente(
			@RequestParam(name = "nombre", required = false) String nombre,
			@RequestParam(name = "apellido", required = false) String apellido,
			@RequestParam(name = "numeroDoc", required = false) String dni,
			@RequestParam(name = "sexo", required = false) String sexo,
			@RequestParam(name = "telefono", required = false) String telefono,
			@RequestParam(name = "direccion", required = false) String direccion,
			@RequestParam(name = "ocupacion", required = false) String ocupacion,
			@RequestParam(name = "emailUsuario", required = false) String emailUsuario, //Esta valor viene del input oculto de la hoja completarDatos, que a su vez viene del meotodo Login en ControladorPagina
			ModelMap model
			) throws MiExcepcion {
		
		try {
			
			//Guardamos los datos del formulario que lleno el nuevo cliente
			servicioCliente.registrarCliente(emailUsuario, dni, nombre, apellido, ocupacion, direccion, telefono, sexo);
			return "redirect:/homeCliente?email=" + emailUsuario; //redirecionamos al metodo homeCliente enviando la varibale mail
				
		} catch (MiExcepcion e) {
			String error = e.getMessage();
			model.addAttribute("emailUsuario", emailUsuario);
			model.addAttribute("nombre", nombre);
			model.addAttribute("apellido", apellido);
			model.addAttribute("numeroDoc", dni);
            model.addAttribute("sexo", sexo);
            model.addAttribute("telefono", telefono);
            model.addAttribute("direccion", direccion);
            model.addAttribute("ocupacion", ocupacion);
            model.addAttribute("error", error);
            model.addAttribute("showModalError", true);
			return "pagina_cliente/completarDatosCliente";
			
		}
	}
	
	
	@GetMapping("/formularioTurnos")
	public String formularioTurnos(
			@RequestParam(name = "email") String email,
			@RequestParam(name = "error", required = false) String error,
			ModelMap modelo) {
			
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);		
		modelo.addAttribute("datosCliente", datosCliente);
		modelo.addAttribute("error", error);
		return "/pagina_cliente/formularioPreguntas";
	}
	
	/*Registra y valida los input del formulario de preguntas en la base de datos.
	 * Tambien guarda los valores ingresado por el usuario, en caso de que haya algun error
	 * en los datos ingresados, se vuelva a cargar la pagina con el mensaje de error y 
	 * con los datos previamente ingresado, para que el usuario no los tenga que volver a cargar*/
	@PostMapping("/guardarFormularioTurnos")
	public String guardarFormularioTurnos(
			
			@RequestParam(name="fuma", required = false) String fuma,
			@RequestParam(name="drogas", required = false) String drogas,
			@RequestParam(name="alcohol", required = false) String alcohol,
			@RequestParam(name="deportes", required = false) String deportes,
			@RequestParam(name="ejercicios", required = false) String ejercicios,
			@RequestParam(name="medicamentos", required = false) String medicamentos,
			@RequestParam(name="nombreMedicamento", required = false) String nombreMedicamento,
			@RequestParam(name="tratamiento") String tratamiento,
			@RequestParam(name="idCliente", required = false) String idCliente,
			@RequestParam(name="email", required = false) String email,
			@RequestParam(name="embarazo", required = false) String embarazo,
			@RequestParam(name="amamantando", required = false) String amamantando,
			@RequestParam(name="ciclo_menstrual", required = false) String ciclo_menstrual,
			@RequestParam(name="alteracion_hormonal", required = false) String alteracion_hormonal,
			@RequestParam(name="vitaminas", required = false) String vitaminas,
			@RequestParam(name="corticoides", required = false) String corticoides,
			@RequestParam(name="hormonas", required = false) String hormonas,
			@RequestParam(name="metodo_anticonceptivo", required = false) String metodo_anticonceptivo,
			@RequestParam(name="sufre_enfermedad", required = false) String sufre_enfermedad,
			@RequestParam(name="cual_enfermedad", required = false) String cual_enfermedad,
			@RequestParam(name="tiroides", required = false) String tiroides,
			@RequestParam(name="paciente_oncologica", required = false) String paciente_oncologica,
			@RequestParam(name="fractura_facial", required = false) String fractura_facial,
			@RequestParam(name="cirugia_estetica", required = false) String cirugia_estetica,
			@RequestParam(name="indique_cirugia_estetica", required = false) String indique_cirugia_estetica,
			@RequestParam(name="tiene_implantes", required = false) String tiene_implantes,
			@RequestParam(name="marca_pasos", required = false) String marca_pasos,
			@RequestParam(name="horas_sueno", required = false) String horas_sueno,
			@RequestParam(name="exposicion_sol", required = false) String exposicion_sol,
			@RequestParam(name="protector_solar", required = false) String protector_solar,
			@RequestParam(name="reaplica_protector", required = false) String reaplica_protector,
			@RequestParam(name="consumo_carbohidratos", required = false) String consumo_carbohidratos,
			@RequestParam(name="tratamientosFacialesAnteriores", required = false) String tratamientos_faciales_anteriores,
			@RequestParam(name="resultados_tratamiento_anterior", required = false) String resultados_tratamiento_anterior,
			@RequestParam(name="cuidado_de_piel", required = false) String cuidado_de_piel,
			@RequestParam(name="motivo_consulta", required = false) String motivo_consulta,
			Model model) throws MiExcepcion{
		
		
		
		try {
			servicioCliente.formularioTurnos(idCliente, email, fuma, drogas, alcohol, deportes, ejercicios,
					medicamentos, nombreMedicamento, embarazo, amamantando, ciclo_menstrual, alteracion_hormonal,
					vitaminas, corticoides, hormonas, metodo_anticonceptivo, sufre_enfermedad,
					cual_enfermedad, tiroides, paciente_oncologica, fractura_facial, cirugia_estetica, 
					indique_cirugia_estetica, tiene_implantes, marca_pasos, horas_sueno, exposicion_sol,
					protector_solar, reaplica_protector, consumo_carbohidratos, tratamientos_faciales_anteriores,
					resultados_tratamiento_anterior, cuidado_de_piel, motivo_consulta);
			
			if (tratamiento.equals("facial")) {
				return "redirect:/reservaDeTurnoClienteFacial?email=" + email;
			}else if(tratamiento.equals("corporal")){
				return "redirect:/reservaDeTurnoClienteCorporal?email=" + email;
			}else {
				return "";
			}
				
			/*En esta excepcion cargamos los datos que ya el usuario ha ingresado arriba, en caso
			 * de que haya algun error de validacion, se vuelvan a carlos los datos seleccionados
			 * y no tenga de ingresarlos nuevamente*/
		} catch (MiExcepcion e) {
			List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
			String error = e.getMessage();
			model.addAttribute("datosCliente", datosCliente);
			model.addAttribute("tratamiento", tratamiento);
            model.addAttribute("fuma", fuma);
            model.addAttribute("drogas", drogas);
            model.addAttribute("alcohol", alcohol);
            model.addAttribute("deportes", deportes);
            model.addAttribute("ejercicios", ejercicios);
            model.addAttribute("medicamentos", medicamentos);
            model.addAttribute("nombreMedicamento", nombreMedicamento);
            model.addAttribute("embarazo", embarazo);
            model.addAttribute("amamantando", amamantando);
            model.addAttribute("ciclo_menstrual", ciclo_menstrual);
            model.addAttribute("alteracion_hormonal", alteracion_hormonal);
            model.addAttribute("vitaminas", vitaminas);
            model.addAttribute("corticoides", corticoides);
            model.addAttribute("hormonas", hormonas);
            model.addAttribute("metodo_anticonceptivo", metodo_anticonceptivo);
            model.addAttribute("sufre_enfermedad", sufre_enfermedad);
            model.addAttribute("cual_enfermedad", cual_enfermedad);
            model.addAttribute("tiroides", tiroides);
            model.addAttribute("paciente_oncologica", paciente_oncologica);
            model.addAttribute("fractura_facial", fractura_facial);
            model.addAttribute("cirugia_estetica", cirugia_estetica);
            model.addAttribute("indique_cirugia_estetica", indique_cirugia_estetica);
            model.addAttribute("tiene_implantes", tiene_implantes);
            model.addAttribute("marca_pasos", marca_pasos);
            model.addAttribute("horas_sueno", horas_sueno);
            model.addAttribute("exposicion_sol", exposicion_sol);
            model.addAttribute("protector_solar", protector_solar);
            model.addAttribute("reaplica_protector", reaplica_protector);
            model.addAttribute("consumo_carbohidratos", consumo_carbohidratos);
            model.addAttribute("tratamientosFacialesAnteriores", tratamientos_faciales_anteriores);
            model.addAttribute("resultados_tratamiento_anterior", resultados_tratamiento_anterior);
            model.addAttribute("cuidado_de_piel", cuidado_de_piel);
            model.addAttribute("motivo_consulta", motivo_consulta);
            model.addAttribute("error", error);
            model.addAttribute("showModal", true);
			return "/pagina_cliente/formularioPreguntas";
		}
		
	}
	
	/*Redirecciona al formulario de preguntas en caso de haber un error en la validacion de los input*/
/*	@GetMapping("/formularioPreguntas")
	public String formularioPreguntas(
			@RequestParam(name="tratamiento") String tratamiento,
			@RequestParam(name="email") String email,
			@RequestParam(name="error", required = false) String error,
			ModelMap model) {
		
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosCliente", datosCliente);
		model.addAttribute("tratamiento", tratamiento);
		model.addAttribute("error", error);
		return "/pagina_cliente/formularioPreguntas";
	} */
	
	@GetMapping("/reservaDeTurnoClienteFacial")
	public String reservaDeTurnoClienteFacial(
			@RequestParam(name="email") String email,
			Model modelo){
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);		
		modelo.addAttribute("datosCliente", datosCliente);
		return "/pagina_cliente/reservaDeTurnoClienteFacial";
	}
	
	@GetMapping("/reservaDeTurnoClienteCorporal")
	public String reservaDeTurnoClienteCorporal(
			@RequestParam(name="email") String email,
			Model modelo){
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);		
		modelo.addAttribute("datosCliente", datosCliente);
		return "/pagina_cliente/reservaDeTurnoClienteCorporal";
	}

	
	@PostMapping("/actualizarDatosCliente")
	public String actualizarDatosCliente(
			@RequestParam(name="idCliente") String idCliente, //este atributo es enviado en un input oculto de la pag misdatosCliente
			@RequestParam(name="ocupacion", required = false) String ocupacion, // Este y los demas atributos los puse como no requeridos para poder personalizar las excepciones
		    @RequestParam(name="email", required = false) String email,
			@RequestParam(name="domicilio", required = false) String domicilio,
			@RequestParam(name="sexo", required = false) String sexo,
			@RequestParam(name="telefono", required = false) String telefono,
			ModelMap model) throws MiExcepcion {
		
		//Buscamos mediante el id el mail anterior del cliente y lo guardamos en la variable emailAnterior por si acaso deja el campo de email vacio o coloca un email no valido
		// entonces usamos este mail anterior para poder pasarlo al controlador de misdatosClientes y poder visualizar los datos del cliente
		// Tambien buscamos los valores previamente guardados en la base de datos para poder compararlos con los nuevos
		String ocupacionAnterior = null;
		String emailAnterior = null;
		String domicilioAnterior = null;
		Sexo sexoAnterior = null;
		String nuevoSexo = null;
		String telefonoAnterior = null;
		
		Optional<Cliente> identificarCliente = repositorioCliente.findById(idCliente);
		if (identificarCliente.isPresent()) {
			Cliente datosAnteriorCliente = identificarCliente.get();
			emailAnterior = datosAnteriorCliente.getEmail();
			ocupacionAnterior = datosAnteriorCliente.getOcupacion();
			domicilioAnterior = datosAnteriorCliente.getDomicilio();
			sexoAnterior = datosAnteriorCliente.getSexo();
			nuevoSexo = sexoAnterior.toString();
			telefonoAnterior = datosAnteriorCliente.getTelefono();
		}
		
		//Teniendo el valos de los datos guardados y los que envian al presionar guardar en el formualario podemos comparar si se hiz alguna modificaicon
				//de los datos, si presiona guardar y no se modifico nada, recargar la misma pagina y no muestra ningun mensaje
		if (ocupacionAnterior.equals(ocupacion) && emailAnterior.equals(email) && domicilioAnterior.equals(domicilio) && nuevoSexo.equals(sexo) && telefonoAnterior.equals(telefono)) {
			return "redirect:/misdatosCliente?email=" + email;
		}
		
		try {
			//este metodo verifica valida el mail y los nuevos datos del cliente y los remplaza en la base de datos
			servicioCliente.modificarCliente(idCliente, ocupacion, email, emailAnterior, domicilio, sexo, telefono );
			List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email); //Buscamos todos los datos pertenecientes al cliente despues de haber sido actualizados en la base de datos y los mostramos en el campo correspondiente
			String exito = "Datos actualizados correctamente";
			model.addAttribute("datosCliente",datosCliente);
			model.addAttribute("exito",exito);
			model.addAttribute("showModalExito", true);
			return "/pagina_cliente/misdatosCliente";
			
		} catch (Exception e) {
			String error = e.getMessage(); // en la exepcion e.getmessage obtenenos el valor de la exepcion personalizada que se de y la enviamos al controlador de misdatosCliente para ser monstrada en pantalla
			List <Usuario> datosClienteAnterior = servicioUsuario.buscarPorEmail(emailAnterior); //Buscamos los datos ateriores a la excepcion y los mostramos en caso de que haya un error por parte del usuario
			model.addAttribute("datosCliente",datosClienteAnterior);
			model.addAttribute("error",error);
			model.addAttribute("showModalError", true);
			return "/pagina_cliente/misdatosCliente";
			
			
			
		}
	}
	
			
	@PostMapping("actualizarContrasenaCliente")
	public String actualizarContrasenaCliente(
			@RequestParam(name = "emailCliente") String emailCliente, //Esta variable viene de un input oculto de la pag cambiarContrasenaCliente
			@RequestParam(name = "idCliente") String idCliente, //Esta variable viene de un input oculto de la pag de la pag cambiarContrasenaCliente
			@RequestParam(name = "oldPass") String oldPass, //A partir de estas viene del formulario
			@RequestParam(name = "newPass") String newPass,
			@RequestParam(name = "repeatNewPass") String repeatNewPass,
			ModelMap model) throws MiExcepcion {
		
		String error = null;
		try {
			servicioUsuario.modificarContrasena(idCliente,oldPass, newPass, repeatNewPass);
			return "/index";
		} catch (Exception e) {
			error = e.getMessage();
			return "redirect:/cambiarContrasenaCliente?email=" + emailCliente + "&error=" + error;
		}
		
	}
			
			
			
	
			
				
		
		
		
		
		
}
