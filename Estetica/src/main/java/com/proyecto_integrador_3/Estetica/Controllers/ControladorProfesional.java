package com.proyecto_integrador_3.Estetica.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioProfesional;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioProfesional;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

@Controller
public class ControladorProfesional {
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	@Autowired
	public ServicioProfesional servicioProfesional;
	
	@Autowired
	public RepositorioProfesional repositorioProfesional;

		
	@GetMapping("/homeProfesional")
	public String homeProfesional(@RequestParam(name = "email") String email, Model model) {
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosProfesional", datosProfesional);
		return "/pagina_profesional/homeProfesional";	
	}

	@GetMapping("/misdatosProfesional")
	public String misdatosProfesional(
			@RequestParam(name = "email") String email,
			@RequestParam(name = "exito", required = false) String exito,
			@RequestParam(name = "error", required = false) String error,
			ModelMap model) {
		
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosProfesional", datosProfesional);
		model.addAttribute("exito", exito);
		model.addAttribute("error", error);
		return "/pagina_profesional/misdatosProfesional";	
	}
	
	
	
	@PostMapping("/guardarDatosProfesional")
	public String guardarDatosProfesional(
			@RequestParam(name = "matricula") String matricula,
			@RequestParam(name = "especialidad") String especialidad,
			@RequestParam(name = "sexo") String sexo,
			@RequestParam(name = "telefono") Integer telefono,
			@RequestParam(name = "direccion") String direccion,
			@RequestParam(name = "emailUsuario") String emailUsuario, //Esta valor viene del input oculto de la hoja completarDatos, que a su vez viene del meotodo Login en ControladorPagina
			ModelMap model) throws MiExcepcion {
		
		try {
			
			//Guardamos los datos del formulario que lleno el nuevo cliente
			servicioProfesional.registrarProfesional(emailUsuario, matricula, especialidad, direccion, telefono, sexo);
				
		} catch (MiExcepcion e) {
			System.out.println(e.getMessage());
			model.put("error", e.getMessage());
			return "pagina_profesional/completarDatosProfesional";
		}
		return "redirect:/homeProfesional?email=" + emailUsuario; //redirecionamos al metodo homeProfesional enviando la varibale mail
	}
	
	@PostMapping("/actualizarDatosProfesional")
	public String actualizarDatosProfesional(
			@RequestParam(name="idProfesional") String idProfesional, //este atributo es enviado en un input oculto de la pag misdatosProfesional
		    @RequestParam(name="email", required = false) String email, // Este y los demas atributos los puse como no requeridos para poder personalizar las excepciones
			@RequestParam(name="domicilio", required = false) String domicilio,
			@RequestParam(name="sexo", required = false) String sexo,
			@RequestParam(name="telefono", required = false) Integer telefono,
			ModelMap model) throws MiExcepcion {
		
		//Buscamos mediante el id el mail anterior del admin y lo guardamos en la variable emailAnterior por si acaso deja el campo de email vacio o coloca un email no valido
		// entonces usamos este mail anterior para poder pasarlo al controlador de misdatosClientes y poder visualizar los datos del cliente
		// Tambien buscamos los valores previamente guardados en la base de datos para poder compararlos con los nuevos
		String emailAnterior = null;
		String domicilioAnterior = null;
		Sexo sexoAnterior = null;
		String nuevoSexo = null;
		Integer telefonoAnterior = null;
		
		Optional<Profesional> identificarProfesional = repositorioProfesional.findById(idProfesional);
		if (identificarProfesional.isPresent()) {
			Profesional datosAnteriorProfesional = identificarProfesional.get();
			emailAnterior = datosAnteriorProfesional.getEmail();
			domicilioAnterior = datosAnteriorProfesional.getDomicilio();
			sexoAnterior = datosAnteriorProfesional.getSexo();
			nuevoSexo = sexoAnterior.toString();
			telefonoAnterior = datosAnteriorProfesional.getTelefono();
			
		}
		
//		System.out.println("EMAIL: " + email);
//		System.out.println("EMAIL ANTERIOR: " + emailAnterior);
//		System.out.println("DOMICILIO: " + domicilio);
//		System.out.println("DOMICILIO ANTERIOR: " + domicilioAnterior);
//		System.out.println("SEXO: " + sexo);
//		System.out.println("SEXO ANTERIOR: " + nuevoSexo);
//		System.out.println("TELEFONO: " + telefono);
//		System.out.println("TELEFONO ANTERIOR: " + telefonoAnterior);
		
		//Teniendo el valos de los datos guardados y los que envian al presionar guardar en el formualario podemos comparar si se hiz alguna modificaicon
		//de los datos, si presiona guardar y no se modifico nada, recargar la misma pagina y no muestra ningun mensaje
		if (emailAnterior.equals(email) && domicilioAnterior.equals(domicilio) && nuevoSexo.equals(sexo) && telefonoAnterior.equals(telefono)) {
			return "redirect:/misdatosProfesional?email=" + email;
		}
		
		try {
			//este metodo verifica valida el mail y los nuevos datos del cliente y los remplaza en la base de datos
			servicioProfesional.modificarProfesional(idProfesional, email, emailAnterior, domicilio, sexo, telefono );
			String exito = "Datos actualizados correctamente";
			return "redirect:/misdatosProfesional?email=" + email + "&exito=" + exito; //si todo sale bien redireccionamos al metodo misdatosProfesional con el mail actualizado y un mensaje de exito
			
		} catch (Exception e) {
			String error = e.getMessage(); // en la exepcion e.getmessage obtenenos el valor de la exepcion personalizada que se de y la enviamos al controlador de misdatosProfesional para ser monstrada en pantalla
			return "redirect:/misdatosProfesional?email=" + emailAnterior + "&error=" + error; // si se produce alguna exepcion en algun campo enviamos el mail anterior del usuario y un mensaje de error al metodo misdatosProfesional
		}
	}
	
	//metodo relacionado con actualizarContrasenaProfesional
	@GetMapping("/cambiarContrasenaProfesional")
	public String cambiarContrasenaProfesional(
			@RequestParam(name = "email") String email,
			@RequestParam(name = "exito", required = false) String exito,
			@RequestParam(name = "error", required = false) String error,
			ModelMap model) {
		
		System.out.println("EMAIL: " + email);
		System.out.println("EXITO: " + exito);
		System.out.println("ERROR: " + error);
		
		List <Usuario> datosProfesional = servicioUsuario.buscarPorEmail(email);
		
		model.addAttribute("datosProfesional", datosProfesional);
		model.addAttribute("exito", exito);
		model.addAttribute("error", error);
		return "/pagina_profesional/cambiarContrasenaProfesional";
	}
	
	//Metodo relacionado con cambiarContrasenaProfesional
	@PostMapping("actualizarContrasenaProfesional")
	public String actualizarContrasenaProfesional(
			@RequestParam(name = "emailProfesional") String emailProfesional, //Esta variable viene de un input oculto de la pag cambiarContrasenaProfesional
			@RequestParam(name = "idProfesional") String idProfesional, //Esta variable viene de un input oculto de la pag de la pag cambiarContrasenaCliente
			@RequestParam(name = "oldPass") String oldPass, //A partir de estas viene del formulario
			@RequestParam(name = "newPass") String newPass,
			@RequestParam(name = "repeatNewPass") String repeatNewPass,
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
