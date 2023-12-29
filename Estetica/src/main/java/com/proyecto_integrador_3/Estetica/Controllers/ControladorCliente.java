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
	
	
	@GetMapping("/reservaDeTurnoCliente")
	public String reservaDeTurnoCliente(
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "idCliente") String idCliente,
			ModelMap model) {
		
		Boolean usoDeFormulario = null;
		Optional<Cliente> formularioDeDatos = repositorioCliente.findById(idCliente);
		if (formularioDeDatos.isPresent()) {
			Cliente validarUsoFormulario = formularioDeDatos.get();
			usoDeFormulario = validarUsoFormulario.getFomularioDatos();
		}
		
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
			
		if (usoDeFormulario) {
			model.addAttribute("datosCliente", datosCliente);
			return "/pagina_cliente/reservaDeTurnoCliente";	
		}else {
			model.addAttribute("datosCliente", datosCliente);
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
		System.out.println("EMAILLLLL DENTRO: " + emailUsuario);
		modelo.addAttribute("emailUsuario", emailUsuario);
		modelo.addAttribute("error", error);
		return "/pagina_cliente/completarDatosCliente";
	}
	
	//Devuelve la pagina homeCLiente con los datos del usuario que le pasemos por mmail
	@GetMapping("/homeCliente")
	public String homeCliente(@RequestParam(name = "email") String email, Model model) {
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosCliente", datosCliente);
		return "/pagina_cliente/homeCliente";	
		
	}
	
	//Muestra todos los datos personales de la persona en la pagina misdatosCliente
	@GetMapping("/misdatosCliente")
	public String misdatosCliente(
			@RequestParam(name="email") String email,
			@RequestParam(name="exito", required = false) String exito,
			@RequestParam(name="error", required = false) String error,
			Model model) { // el valor del parametro email viene del html homeCliente
		
		List <Usuario> datosCliente = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("exito", exito);
		model.addAttribute("error", error);
		model.addAttribute("datosCliente", datosCliente);
	return "/pagina_cliente/misdatosCliente";	
	}
	
	@PostMapping("guardarDatosCliente")
	public String guardarDatosCliente(
			@RequestParam(name = "nombre") String nombre,
			@RequestParam(name = "apellido") String apellido,
			@RequestParam(name = "numeroDoc") String dni,
			@RequestParam(name = "sexo") String sexo,
			@RequestParam(name = "telefono", required = false) Integer telefono,
			@RequestParam(name = "direccion") String direccion,
			@RequestParam(name = "ocupacion") String ocupacion,
			@RequestParam(name = "emailUsuario") String emailUsuario, //Esta valor viene del input oculto de la hoja completarDatos, que a su vez viene del meotodo Login en ControladorPagina
			ModelMap model
			) throws MiExcepcion {
		
		System.out.println("EMAILLLLL: " + emailUsuario);
		try {
			
			//Guardamos los datos del formulario que lleno el nuevo cliente
			servicioCliente.registrarCliente(emailUsuario, dni, nombre, apellido, ocupacion, direccion, telefono, sexo);
			return "redirect:/homeCliente?email=" + emailUsuario; //redirecionamos al metodo homeCliente enviando la varibale mail
				
		} catch (MiExcepcion e) {
			String error = e.getMessage();
			//return "pagina_cliente/completarDatosCliente";
			return "redirect:/completarDatosCliente?email=" + emailUsuario + "&error=" + error;
		}
	}

	
	@PostMapping("/actualizarDatosCliente")
	public String actualizarDatosCliente(
			@RequestParam(name="idCliente") String idCliente, //este atributo es enviado en un input oculto de la pag misdatosCliente
			@RequestParam(name="ocupacion", required = false) String ocupacion, // Este y los demas atributos los puse como no requeridos para poder personalizar las excepciones
		    @RequestParam(name="email", required = false) String email,
			@RequestParam(name="domicilio", required = false) String domicilio,
			@RequestParam(name="sexo", required = false) String sexo,
			@RequestParam(name="telefono", required = false) Integer telefono,
			ModelMap model) throws MiExcepcion {
		
		//Buscamos mediante el id el mail anterior del cliente y lo guardamos en la variable emailAnterior por si acaso deja el campo de email vacio o coloca un email no valido
		// entonces usamos este mail anterior para poder pasarlo al controlador de misdatosClientes y poder visualizar los datos del cliente
		// Tambien buscamos los valores previamente guardados en la base de datos para poder compararlos con los nuevos
		String ocupacionAnterior = null;
		String emailAnterior = null;
		String domicilioAnterior = null;
		Sexo sexoAnterior = null;
		String nuevoSexo = null;
		Integer telefonoAnterior = null;
		
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
			String exito = "Datos actualizados correctamente";
			return "redirect:/misdatosCliente?email=" + email + "&exito=" + exito; //si todo sale bien redireccionamos al metodo misdatosCliente con el mail actualizado y un mensaje de exito
			
		} catch (Exception e) {
			String error = e.getMessage(); // en la exepcion e.getmessage obtenenos el valor de la exepcion personalizada que se de y la enviamos al controlador de misdatosCliente para ser monstrada en pantalla
			return "redirect:/misdatosCliente?email=" + emailAnterior + "&error=" + error; // si se produce alguna exepcion en algun campo enviamos el mail anterior del usuario y un mensaje de error al metodo misdatosCliente
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
