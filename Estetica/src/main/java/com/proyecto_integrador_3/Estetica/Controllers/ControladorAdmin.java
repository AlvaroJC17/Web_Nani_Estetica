package com.proyecto_integrador_3.Estetica.Controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioAdmin;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioAdmin;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCliente;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioPersona;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

import jakarta.transaction.Transactional;


@Controller
//@RestController
public class ControladorAdmin {

	@Autowired
	public RepositorioAdmin repositorioAdmin;
	
	@Autowired
	public ServicioAdmin servicioAdmin;
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	@Autowired
	public RepositorioUsuario repositorioUsuario;
	
	@Autowired
	public RepositorioPersona repositorioPersona;
	
	@Autowired
	public ServicioPersona servicioPersona;
	
	@Autowired
	public ServicioCliente servicioCliente;
	
	
	@GetMapping("/portalAdmin")
	public String portalAdmin() {
	return "/pagina_admin/portalAdmin";	
	}
	
	@GetMapping("/cambiarContrasenaAdmin")
	public String cambiarContrasenaAdmin() {
	return "/pagina_admin/cambiarContrasenaAdmin";	
	}
	
	
	@GetMapping("/misdatosAdmin")
	public String misdatosAdmin(@RequestParam(name = "email") String email, ModelMap model) {
		List <Usuario> datosPersonaUsuario = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosAdmin", datosPersonaUsuario);
		return "/pagina_admin/misdatosAdmin";	
	}
	
	@GetMapping("/homeAdmin")
	public String homeAdmin(@RequestParam(name = "email") String email, ModelMap model) { //El valor de esta variable viene del metodo login en controladorPagina
		List <Usuario> datosAdmin = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosAdmin", datosAdmin);
		return "/pagina_admin/homeAdmin";	
	}
	
	@PostMapping("/guardarDatosAdmin")
	public String guardarDatosAdmin(
			@RequestParam(name = "nombre") String nombre,
			@RequestParam(name = "apellido") String apellido,
			@RequestParam(name = "numeroDoc") String dni,
			@RequestParam(name = "sexo") String sexo,
			@RequestParam(name = "nacimiento") String fechaNacimiento,
			@RequestParam(name = "telefono", required = false) Integer telefono,
			@RequestParam(name = "direccion") String direccion,
			@RequestParam(name = "ocupacion") String ocupacion,
			@RequestParam(name = "emailUsuario") String emailUsuario, //Esta valor viene del input oculto de la hoja completarDatos, que a su vez viene del meotodo Login en ControladorPagina
			ModelMap model) throws MiExcepcion {
		
		try {
			
			//Guardamos los datos del form que lleno el nuevo admin
			servicioAdmin.registrarAdmin(emailUsuario, dni, nombre, apellido, ocupacion, direccion, telefono, fechaNacimiento, sexo);
				
		} catch (MiExcepcion e) {
			System.out.println(e.getMessage());
			model.put("error", e.getMessage());
			return "/pagina_admin/completarDatosAdmin";
		}
		return "redirect:/homeAdmin?email=" + emailUsuario; //redirecionamos al metodo homeAdmin enviando la varibale mail
	}
			
	
	
	
	//A este metodo le paso la variable mail y la lista por dos model diferentes para que cuando cargue
	//la pagina se pueda visualizar el nav y la lista de usuarios, sino le paso el mail el nav no se ve
	@GetMapping("/listarUsuariosVisibles")
    public String listarUsuariosVisibles(@RequestParam(name = "email") String email, ModelMap model) throws MiExcepcion {
		List<Usuario> usuarios = repositorioUsuario.joinUsuarioPersona();
		model.addAttribute("usuariosEmail", email);
		model.addAttribute("usuarios", usuarios);
		return "/pagina_admin/portalAdmin";
	}
	
	
	
	
	
	//Muestra una pagina con la lista de usuarios oculta, esto porque queremos que el admin
	//busque los usuarios por nombre, dni o email y no que los busque en una lista
	@GetMapping("/listarUsuariosOcultos")
    public String listarUsuarios(
    		@RequestParam(name = "email") String email, //Esta variable proviene de homeAdmin
    		@RequestParam(name = "exito", required = false) String exito, //El requiered=false le indica al metodo que el valor de esta variable es opcional y puede llegar null, asi no se rempe el programa
    		@RequestParam(name = "error", required = false) String error,
    		Model model) throws MiExcepcion {

		model.addAttribute("usuariosEmail", email); //Con este mail mostramos el nav en este metodo
		model.addAttribute("exito", exito); //inyectamos mensaje de exito si es necesario
		model.addAttribute("error", error); //inyectamos mensaje de error si es necesario
		return "/pagina_admin/portalAdmin";
	}
	
	
	
	
	
	//Buscamos usuario por dni, nombre o email, la variable emailAdmin es para pasar el mail de admin y poder visualizar el nav
	@PostMapping("/buscarDNIoNombre")
	public String buscarDniNombreEmail(
			@RequestParam(name = "dato") String dato,  //la variable dato puede ser un nombre, dni o mail
			@RequestParam(name = "emailAdmin") String emailAdmin,
			Model model) {
		
		String exito;
		String error;
		String datoSinEspacios = dato.trim(); //Le quitamos los espacios en blanco al principio y final de la palabra

		//Validamos que la busqueda no se haya hecho en blanco y mostramos un mensaje
		if (datoSinEspacios.isEmpty() || Objects.equals(datoSinEspacios, null)) {
			error = "Indique un numero de DNI, nombre o email para realizar la busqueda";
			return "redirect:/listarUsuariosOcultos?email=" + emailAdmin + "&error=" + error;
		}
		
		//Validamos que el usuario buscado exista en la base de datos, sino mandamos un mensaje de error
		if (!servicioUsuario.buscarPorDniOptional(datoSinEspacios).isPresent() && !servicioUsuario.buscarPorNombreOptional(datoSinEspacios).isPresent() && !servicioUsuario.buscarPorEmailOptional(datoSinEspacios).isPresent()) {
			error = "Usuario no se encuentra registrado";
			return "redirect:/listarUsuariosOcultos?email=" + emailAdmin + "&error=" + error;
		}
		
		// Si cumple las condiciones para pasar los condicionales de arriba, entonces
		//usamos el valor de dato, segun corresponda
		List<Usuario> usuarioDni = servicioUsuario.buscarDni(datoSinEspacios);
		List<Usuario>	usuarioNombre = servicioUsuario.buscarNombre(datoSinEspacios);
		List<Usuario> usuarioEmail = servicioUsuario.buscarPorEmail(datoSinEspacios);
		
		if (!usuarioDni.isEmpty()) {
			model.addAttribute("usuarios", usuarioDni); // asignamos el valor de la variable administradoresDni a la variable html administradores y asi poder iterarla en el documento
			model.addAttribute("usuariosEmail", emailAdmin);
			return "/pagina_admin/portalAdmin";
		}	
		if (!usuarioNombre.isEmpty()) {
			model.addAttribute("usuarios", usuarioNombre);
			model.addAttribute("usuariosEmail", emailAdmin);
			return "/pagina_admin/portalAdmin";
		}	
		if (!usuarioEmail.isEmpty()) {
			model.addAttribute("usuarios", usuarioEmail);
			model.addAttribute("usuariosEmail", emailAdmin);
			return "/pagina_admin/portalAdmin";
		}
		model.addAttribute("usuariosEmail", emailAdmin);
		return "/pagina_admin/portalAdmin";
	}
		
	
	
	
	// En este metodo unificamos la edicion y eliminacion de un usuario a traves de un solo formulario
	// usando el action como valor para las diferentes condiciones
	@PostMapping("/modificarUsuario")
	public String modificarUsuario(
			@RequestParam(name = "idUsuario", required = false) String id,
			@RequestParam(name = "nuevoRol") String nuevoRolNombre,
            @RequestParam(name = "emailAdministrador") String emailAdministrador,
            @RequestParam(name = "action") String action,
            Model model) {
		
		String exito;
		String error;
		
		//Validamos que el id no venga vacio, esto pasa cuando le dan a una opcion sin seleccionar a un usuario
		if (id == null) {
			model.addAttribute("error", "Es necesario seleccionar un usuario!!");
			model.addAttribute("usuariosEmail", emailAdministrador);
			return "/pagina_admin/portalAdmin";
		}
		
		//Validamos que la opcion rol no sea "Modifical rol" y pasamos el valor de String a Rol
		Rol nuevoRol = null;
		if (!"Modifical rol".equals(nuevoRolNombre)) {
			try {
	            // Convertir el nuevoRol solo si no es "Modificar Rol"
	            nuevoRol = Rol.valueOf(nuevoRolNombre.toUpperCase());
	        } catch (IllegalArgumentException e) {
	            System.out.println(e.getMessage());
	        }
		}
					
	     //Ya teniendo el mail de usuario puedo inyectarlo en el resultado de cada if
		// para que ademas de cumplir la funcion de cada condicional tambien la pag
		// me recargue con la barra de nav. Recordar que como la barra de nav este en un
		//condicional, sino le paso un valor de mail esta no se me activo y no puedo verla
		
		if (action.equals("modificarRol") && nuevoRol != null) {
			//metodo para moficar el rol
			servicioUsuario.modificarRol(id, nuevoRol);
			return "redirect:/listarUsuariosPorId?id=" + id + "&email=" + emailAdministrador;
		}
															 
		if (action.equals("altaUsuario")) {
			try {
				servicioUsuario.altaUsuario(id);
				return "redirect:/listarUsuariosPorId?id=" + id + "&email=" + emailAdministrador;
				
			} catch (Exception e) {
				System.out.println("No se actualizo el alta del Usuario....");
				e.printStackTrace();
			}
		}
		
		if (action.equals("bajaUsuario")) {
			try {
				//metodo para modificar el activo del usuario a False
				servicioUsuario.bajaUsuario(id);
				return "redirect:/listarUsuariosPorId?id=" + id + "&email=" + emailAdministrador;
				
			} catch (Exception e) {
				System.out.println("No se actualizo la baja del usuario....");
				e.printStackTrace();
			}
		}
						
		if (action.equals("eliminarUsuario")) {
			//Meotod para eliminar a un usuario de la base de datos
			try {
				servicioUsuario.borrarUsuario(id);
				servicioPersona.borrarPersona(id);
				exito = "Usuario eliminado correctamente";
				return "redirect:/listarUsuariosOcultos?email=" + emailAdministrador + "&exito=" + exito; //Enviamos mail y mensaje de exito
			} catch (Exception e) {
				error = "Usuario no se pudo eliminar";
				return "redirect:/listarUsuariosOcultos?email=" + emailAdministrador + "&error=" + error; //Enviamos mail y mensaje de error;
			}
		}
		return "/pagina_admin/portalAdmin";
	}
		
	
	
	
	
	//Lista usuarios y persona haciendo un join de tablas por ID
	//Este metodo esta relacionado con el de modificarUsuario y la variable mail viene de él
	@GetMapping("/listarUsuariosPorId")
	public String listarUsuariosPorId(String id, String email, Model model) {
		List<Usuario> usuarios = servicioUsuario.buscarId(id);
		model.addAttribute("usuariosEmail", email);
		model.addAttribute("usuarios", usuarios);
		model.addAttribute("exito", "Modificación realizada exitosamente!!");
		return "/pagina_admin/portalAdmin";
	}
	
		
	
	
		
}
		
		
			
		
		
	
	
